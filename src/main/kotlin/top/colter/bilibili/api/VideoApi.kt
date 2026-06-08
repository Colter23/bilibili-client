package top.colter.bilibili.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.utils.io.readAvailable
import top.colter.bilibili.client.BILI_BROWSER_USER_AGENT
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCookiesStorage
import top.colter.bilibili.client.getDataWithWbi
import top.colter.bilibili.data.video.BiliDownloadProgress
import top.colter.bilibili.data.video.BiliVideo
import top.colter.bilibili.data.video.BiliVideoDashStream
import top.colter.bilibili.data.video.BiliVideoDownloadOptions
import top.colter.bilibili.data.video.BiliVideoDownloadResult
import top.colter.bilibili.data.video.BiliVideoDownloadStreamType
import top.colter.bilibili.data.video.BiliVideoPage
import top.colter.bilibili.data.video.BiliVideoPlayUrl
import top.colter.bilibili.data.video.BiliVideoQuality
import top.colter.bilibili.data.video.requestCode
import top.colter.bilibili.exception.BiliDownloadException
import java.io.File

/////////////////////////////////////////////
//                 视频相关                  //
/////////////////////////////////////////////

/**
 * ## 获取视频详情
 *
 * **Method:** GET
 *
 * **Params:** aid=avid(不带av) | bvid=BVid  (两个都有以 bvid 为准)
 *
 * **Response:** [top.colter.bilibili.client.BiliCommonResult] / [BiliVideo]
 *
 * **From:** https://www.bilibili.com/video/BV1SV411t7G5
 *
 * **Example:** https://api.bilibili.com/x/web-interface/view?aid=416215637&bvid=BV1SV411t7G5
 */
public const val VIDEO_DETAIL_WBI: String = "$BASE_API/x/web-interface/wbi/view"

/**
 * ## 获取视频取流地址
 */
public const val VIDEO_PLAY_URL_WBI: String = "$BASE_API/x/player/wbi/playurl"

/**
 * ## 获取视频详情
 *
 * @param aid avid，不带 av；[aid] 与 [bvid] 至少传入一个
 * @param bvid BVid；[aid] 与 [bvid] 至少传入一个，同时存在时接口以 bvid 为准
 *
 * @see VIDEO_DETAIL_WBI
 */
public suspend fun BiliClient.getVideoDetail(aid: Long? = null, bvid: String? = null): BiliVideo {
    require(aid != null || !bvid.isNullOrBlank()) { "至少需要传入 aid 或 bvid" }
    return getDataWithWbi(VIDEO_DETAIL_WBI) {
        aid?.let { parameter("aid", it) }
        bvid?.takeIf { it.isNotBlank() }?.let { parameter("bvid", it) }
    }
}

/**
 * ## 获取普通投稿视频分 P 的取流信息
 *
 * @param aid avid，不带 av；[aid] 与 [bvid] 至少传入一个
 * @param bvid BVid；[aid] 与 [bvid] 至少传入一个
 * @param cid 视频分 P 的 cid
 * @param quality 清晰度选择；默认自动选择最高可用画质
 * @param fnval 取流格式标记，默认请求 DASH 等完整流信息
 * @param fourk 是否允许 4K 取流
 */
public suspend fun BiliClient.getVideoPlayUrl(
    aid: Long? = null,
    bvid: String? = null,
    cid: Long,
    quality: BiliVideoQuality = BiliVideoQuality.AUTO_HIGHEST,
    fnval: Int = 4048,
    fourk: Boolean = true
): BiliVideoPlayUrl {
    require(aid != null || !bvid.isNullOrBlank()) { "至少需要传入 aid 或 bvid" }
    return getDataWithWbi(VIDEO_PLAY_URL_WBI) {
        aid?.let { parameter("avid", it) }
        bvid?.takeIf { it.isNotBlank() }?.let { parameter("bvid", it) }
        parameter("cid", cid)
        quality.requestCode?.let { parameter("qn", it) }
        parameter("fnval", fnval)
        parameter("fnver", 0)
        parameter("fourk", if (fourk) 1 else 0)
    }
}

/**
 * ## 选择最合适的 DASH 视频流
 *
 * 支持自动最高、自动最低、指定清晰度、最高不超过某档清晰度。
 */
public fun BiliVideoPlayUrl.selectBestVideoStream(
    quality: BiliVideoQuality = BiliVideoQuality.AUTO_HIGHEST
): BiliVideoDashStream {
    val streams = dash?.video.orEmpty().filter { it.urls.isNotEmpty() }
    if (streams.isEmpty()) {
        throw BiliDownloadException("没有可用的 DASH 视频流")
    }

    val scoped = when (quality) {
        BiliVideoQuality.AutoHighest -> streams
        BiliVideoQuality.AutoLowest -> streams
        is BiliVideoQuality.Exact -> streams.filter { it.id == quality.code }.ifEmpty {
            throw BiliDownloadException("指定清晰度不可用：${quality.description}")
        }
        is BiliVideoQuality.AtMost -> streams.filter { it.id <= quality.max.code }.ifEmpty {
            throw BiliDownloadException("没有不超过 ${quality.max.description} 的可用清晰度")
        }
    }

    val highQualityComparator =
        compareByDescending<BiliVideoDashStream> { it.id }
            .thenBy { it.codecPriority() }
            .thenByDescending { it.bandwidth ?: 0L }
    val lowQualityComparator =
        compareBy<BiliVideoDashStream> { it.id }
            .thenBy { it.codecPriority() }
            .thenByDescending { it.bandwidth ?: 0L }

    return scoped.sortedWith(
        if (quality == BiliVideoQuality.AutoLowest) lowQualityComparator else highQualityComparator
    ).first()
}

/**
 * ## 选择最合适的 DASH 音频流
 *
 * 当取流响应没有音频流时返回 null。
 */
public fun BiliVideoPlayUrl.selectBestAudioStream(): BiliVideoDashStream? {
    return dash?.audio.orEmpty()
        .filter { it.urls.isNotEmpty() }
        .maxWithOrNull(compareBy<BiliVideoDashStream> { it.bandwidth ?: 0L }.thenBy { it.id })
}

/**
 * ## 下载普通投稿视频
 *
 * 不传 [quality] 时默认自动选择最高可用画质。
 */
public suspend fun BiliClient.downloadVideo(
    directory: File,
    aid: Long? = null,
    bvid: String? = null,
    cid: Long? = null,
    page: Int = 1,
    fnval: Int = 4048,
    fourk: Boolean = true,
    fileName: String? = null,
    ffmpegPath: String? = null,
    overwrite: Boolean = false,
    keepStreams: Boolean = false,
    quality: BiliVideoQuality = BiliVideoQuality.AUTO_HIGHEST,
    onProgress: ((BiliDownloadProgress) -> Unit)? = null
): BiliVideoDownloadResult {
    return downloadVideo(
        BiliVideoDownloadOptions(
            directory = directory,
            aid = aid,
            bvid = bvid,
            cid = cid,
            page = page,
            quality = quality,
            fnval = fnval,
            fourk = fourk,
            fileName = fileName,
            ffmpegPath = ffmpegPath,
            overwrite = overwrite,
            keepStreams = keepStreams
        ),
        onProgress
    )
}

/**
 * ## 下载普通投稿视频
 */
public suspend fun BiliClient.downloadVideo(
    options: BiliVideoDownloadOptions,
    onProgress: ((BiliDownloadProgress) -> Unit)? = null
): BiliVideoDownloadResult {
    require(options.aid != null || !options.bvid.isNullOrBlank()) { "至少需要传入 aid 或 bvid" }
    require(options.page > 0) { "page 必须大于 0" }

    val detail = if (options.cid == null || options.fileName.isNullOrBlank()) {
        getVideoDetail(options.aid, options.bvid)
    } else {
        null
    }
    val resolvedAid = options.aid ?: detail?.aid
    val resolvedBvid = options.bvid?.takeIf { it.isNotBlank() } ?: detail?.bvid
    val resolvedCid = options.cid ?: detail?.selectPage(options.page)?.cid
        ?: throw BiliDownloadException("无法解析第 ${options.page} 个分 P 的 cid")
    val resolvedFileName = sanitizeVideoFileName(
        options.fileName ?: buildVideoFileName(detail, resolvedAid, resolvedBvid, resolvedCid, options.page)
    )
    val playUrl = getVideoPlayUrl(
        aid = resolvedAid,
        bvid = resolvedBvid,
        cid = resolvedCid,
        quality = options.quality,
        fnval = options.fnval,
        fourk = options.fourk
    )
    return downloadResolvedVideo(
        playUrl = playUrl,
        directory = options.directory,
        fileName = resolvedFileName,
        referer = videoReferer(resolvedAid, resolvedBvid),
        quality = options.quality,
        ffmpegPath = options.ffmpegPath,
        overwrite = options.overwrite,
        keepStreams = options.keepStreams,
        onProgress = onProgress
    )
}

/**
 * ## 使用已有取流信息下载视频
 */
public suspend fun BiliClient.downloadVideo(
    playUrl: BiliVideoPlayUrl,
    directory: File,
    fileName: String = "video",
    bvid: String? = null,
    aid: Long? = null,
    ffmpegPath: String? = null,
    overwrite: Boolean = false,
    keepStreams: Boolean = false,
    quality: BiliVideoQuality = BiliVideoQuality.AUTO_HIGHEST,
    onProgress: ((BiliDownloadProgress) -> Unit)? = null
): BiliVideoDownloadResult {
    return downloadResolvedVideo(
        playUrl = playUrl,
        directory = directory,
        fileName = sanitizeVideoFileName(fileName),
        referer = videoReferer(aid, bvid),
        quality = quality,
        ffmpegPath = ffmpegPath,
        overwrite = overwrite,
        keepStreams = keepStreams,
        onProgress = onProgress
    )
}

private suspend fun BiliClient.downloadResolvedVideo(
    playUrl: BiliVideoPlayUrl,
    directory: File,
    fileName: String,
    referer: String,
    quality: BiliVideoQuality,
    ffmpegPath: String?,
    overwrite: Boolean,
    keepStreams: Boolean,
    onProgress: ((BiliDownloadProgress) -> Unit)?
): BiliVideoDownloadResult {
    prepareDownloadDirectory(directory)

    return createDownloadClient().use { streamClient ->
        if (playUrl.dash?.video.orEmpty().any { it.urls.isNotEmpty() }) {
            val videoStream = playUrl.selectBestVideoStream(quality)
            val audioStream = playUrl.selectBestAudioStream()
            val videoFile = directory.resolve("$fileName.video.m4s")
            val audioFile = audioStream?.let { directory.resolve("$fileName.audio.m4s") }

            streamClient.downloadStream(videoStream.urls, videoFile, BiliVideoDownloadStreamType.VIDEO, referer, overwrite, onProgress)
            audioStream?.let {
                streamClient.downloadStream(it.urls, audioFile!!, BiliVideoDownloadStreamType.AUDIO, referer, overwrite, onProgress)
            }

            if (!ffmpegPath.isNullOrBlank()) {
                val finalFile = directory.resolve("$fileName.mp4")
                ensureWritableTarget(finalFile, overwrite)
                mergeWithFfmpeg(ffmpegPath, videoFile, audioFile, finalFile, overwrite)
                if (!keepStreams) {
                    videoFile.delete()
                    audioFile?.delete()
                }
                return@use BiliVideoDownloadResult(
                    playUrl = playUrl,
                    videoFile = videoFile.takeIf { keepStreams },
                    audioFile = audioFile?.takeIf { keepStreams },
                    finalFile = finalFile,
                    videoStream = videoStream,
                    audioStream = audioStream
                )
            }

            return@use BiliVideoDownloadResult(
                playUrl = playUrl,
                videoFile = videoFile,
                audioFile = audioFile,
                videoStream = videoStream,
                audioStream = audioStream
            )
        }

        if (playUrl.durl.isNotEmpty()) {
            return@use streamClient.downloadDurlSegments(playUrl, directory, fileName, referer, overwrite, onProgress)
        }

        throw BiliDownloadException("没有可下载的视频流")
    }
}

private suspend fun HttpClient.downloadDurlSegments(
    playUrl: BiliVideoPlayUrl,
    directory: File,
    fileName: String,
    referer: String,
    overwrite: Boolean,
    onProgress: ((BiliDownloadProgress) -> Unit)?
): BiliVideoDownloadResult {
    val segments = playUrl.durl.sortedBy { it.order }
    val files = segments.map { segment ->
        val target = if (segments.size == 1) {
            directory.resolve("$fileName.mp4")
        } else {
            directory.resolve("$fileName.part${segment.order}.mp4")
        }
        downloadStream(segment.urls, target, BiliVideoDownloadStreamType.DURL, referer, overwrite, onProgress)
        target
    }
    return BiliVideoDownloadResult(
        playUrl = playUrl,
        finalFile = files.singleOrNull(),
        durlFiles = files,
        durlSegments = segments
    )
}

private suspend fun HttpClient.downloadStream(
    urls: List<String>,
    targetFile: File,
    type: BiliVideoDownloadStreamType,
    referer: String,
    overwrite: Boolean,
    onProgress: ((BiliDownloadProgress) -> Unit)?
) {
    ensureWritableTarget(targetFile, overwrite)
    val errors = mutableListOf<Throwable>()
    for (url in urls.filter { it.isNotBlank() }) {
        try {
            downloadUrl(url, targetFile, type, referer, onProgress)
            return
        } catch (e: Exception) {
            targetFile.delete()
            errors += e
        }
    }
    val message = errors.lastOrNull()?.message ?: "没有提供可用下载地址"
    throw BiliDownloadException("下载 $type 流失败：$message")
}

private suspend fun HttpClient.downloadUrl(
    url: String,
    targetFile: File,
    type: BiliVideoDownloadStreamType,
    referer: String,
    onProgress: ((BiliDownloadProgress) -> Unit)?
) {
    val response = get(url) {
        headers[HttpHeaders.Referrer] = referer
        headers[HttpHeaders.UserAgent] = BILI_BROWSER_USER_AGENT
    }
    val totalBytes = response.headers[HttpHeaders.ContentLength]?.toLongOrNull()
    val channel = response.bodyAsChannel()
    var downloadedBytes = 0L
    val buffer = ByteArray(DEFAULT_DOWNLOAD_BUFFER_SIZE)
    targetFile.outputStream().buffered().use { output ->
        while (true) {
            val read = channel.readAvailable(buffer, 0, buffer.size)
            if (read <= 0) break
            output.write(buffer, 0, read)
            downloadedBytes += read
            onProgress?.invoke(BiliDownloadProgress(type, downloadedBytes, totalBytes, targetFile))
        }
    }
}

private suspend fun BiliClient.createDownloadClient(): HttpClient {
    val cookieStorage = copyDownloadCookiesStorage()
    return HttpClient(OkHttp) {
        install(HttpCookies) {
            storage = cookieStorage
        }
        expectSuccess = true
    }
}

private suspend fun BiliClient.copyDownloadCookiesStorage(): AcceptAllCookiesStorage {
    val copy = AcceptAllCookiesStorage()
    storage.exportCookies().forEach { cookie ->
        copy.addCookie(cookie.requestUrl(), cookie)
    }
    return copy
}

private fun mergeWithFfmpeg(
    ffmpegPath: String,
    videoFile: File,
    audioFile: File?,
    finalFile: File,
    overwrite: Boolean
) {
    val command = mutableListOf(ffmpegPath, if (overwrite) "-y" else "-n", "-i", videoFile.absolutePath)
    audioFile?.let {
        command += listOf("-i", it.absolutePath)
    }
    command += listOf("-c", "copy", finalFile.absolutePath)

    val process = try {
        ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()
    } catch (e: Exception) {
        throw BiliDownloadException("无法启动 ffmpeg：${e.message}")
    }
    val output = process.inputStream.bufferedReader().readText()
    val code = process.waitFor()
    if (code != 0 || !finalFile.exists()) {
        finalFile.delete()
        throw BiliDownloadException("ffmpeg 合并失败，退出码：$code；输出：${output.takeLast(2000)}")
    }
}

private fun BiliVideoDashStream.codecPriority(): Int {
    val codec = codecs.orEmpty().lowercase()
    return when {
        codecid == 7 || codec.startsWith("avc1") -> 0
        codecid == 12 || codec.startsWith("hev1") || codec.startsWith("hvc1") -> 1
        codecid == 13 || codec.startsWith("av01") -> 2
        else -> 3
    }
}

private fun BiliVideo.selectPage(page: Int): BiliVideoPage? {
    return pages.firstOrNull { it.page == page }
        ?: pages.getOrNull(page - 1)
        ?: takeIf { page == 1 }?.let { BiliVideoPage(cid = cid, page = 1, title = title, duration = duration, uploadTime = uploadTime) }
}

private fun buildVideoFileName(
    detail: BiliVideo?,
    aid: Long?,
    bvid: String?,
    cid: Long,
    page: Int
): String {
    if (detail == null) {
        return bvid?.takeIf { it.isNotBlank() } ?: aid?.let { "av$it-$cid" } ?: "video-$cid"
    }
    val pageInfo = detail.selectPage(page)
    return if (detail.pages.size > 1 && pageInfo != null) {
        "${detail.title}-P${pageInfo.page}-${pageInfo.title}"
    } else {
        detail.title
    }
}

private fun videoReferer(aid: Long?, bvid: String?): String {
    return when {
        !bvid.isNullOrBlank() -> "https://www.bilibili.com/video/$bvid"
        aid != null -> "https://www.bilibili.com/video/av$aid"
        else -> "https://www.bilibili.com"
    }
}

private fun sanitizeVideoFileName(fileName: String): String {
    val clean = fileName.trim()
        .replace(Regex("[\\\\/:*?\"<>|]+"), "_")
        .replace(Regex("\\s+"), " ")
        .trim('.', ' ')
        .take(160)
        .trim('.', ' ')
    return clean.ifBlank { "video" }
}

private fun prepareDownloadDirectory(directory: File) {
    if (directory.exists()) {
        if (!directory.isDirectory) {
            throw BiliDownloadException("下载目标不是目录：${directory.absolutePath}")
        }
        return
    }
    if (!directory.mkdirs()) {
        throw BiliDownloadException("无法创建下载目录：${directory.absolutePath}")
    }
}

private fun ensureWritableTarget(file: File, overwrite: Boolean) {
    if (file.exists()) {
        if (!overwrite) {
            throw BiliDownloadException("目标文件已存在：${file.absolutePath}")
        }
        if (file.isDirectory || !file.delete()) {
            throw BiliDownloadException("无法覆盖目标文件：${file.absolutePath}")
        }
    }
    file.parentFile?.mkdirs()
}

private fun Cookie.requestUrl(): Url {
    val domain = domain?.removePrefix(".")
    val path = path?.takeIf { it.isNotBlank() } ?: "/"
    return Url(if (domain.isNullOrBlank()) BiliCookiesStorage.DEFAULT_URL else "https://$domain$path")
}

private const val DEFAULT_DOWNLOAD_BUFFER_SIZE: Int = 64 * 1024
