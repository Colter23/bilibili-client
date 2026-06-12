package top.colter.bilibili.data.video

import java.io.File

public data class BiliVideoDownloadOptions(
    val directory: File,
    val aid: Long? = null,
    val bvid: String? = null,
    val cid: Long? = null,
    val page: Int = 1,
    val quality: BiliVideoQuality = BiliVideoQuality.AUTO_HIGHEST,
    val fnval: Int = 4048,
    val fourk: Boolean = true,
    val fileName: String? = null,
    val ffmpegPath: String? = null,
    val overwrite: Boolean = false,
    val keepStreams: Boolean = false
)

public data class BiliVideoDownloadSizeOptions(
    val aid: Long? = null,
    val bvid: String? = null,
    val cid: Long? = null,
    val page: Int = 1,
    val quality: BiliVideoQuality = BiliVideoQuality.AUTO_HIGHEST,
    val fnval: Int = 4048,
    val fourk: Boolean = true
)

public enum class BiliVideoDownloadStreamType {
    VIDEO,
    AUDIO,
    DURL
}

public data class BiliDownloadProgress(
    val type: BiliVideoDownloadStreamType,
    val downloadedBytes: Long,
    val totalBytes: Long?,
    val targetFile: File
)

public data class BiliVideoDownloadResult(
    val playUrl: BiliVideoPlayUrl,
    val videoFile: File? = null,
    val audioFile: File? = null,
    val finalFile: File? = null,
    val durlFiles: List<File> = emptyList(),
    val videoStream: BiliVideoDashStream? = null,
    val audioStream: BiliVideoDashStream? = null,
    val durlSegments: List<BiliVideoDurlSegment> = emptyList()
)

public data class BiliVideoStreamSize(
    val type: BiliVideoDownloadStreamType,
    val sizeBytes: Long?,
    val url: String
)

public data class BiliVideoDownloadSize(
    val playUrl: BiliVideoPlayUrl,
    val totalBytes: Long?,
    val video: BiliVideoStreamSize? = null,
    val audio: BiliVideoStreamSize? = null,
    val durl: List<BiliVideoStreamSize> = emptyList(),
    val videoStream: BiliVideoDashStream? = null,
    val audioStream: BiliVideoDashStream? = null,
    val durlSegments: List<BiliVideoDurlSegment> = emptyList()
)
