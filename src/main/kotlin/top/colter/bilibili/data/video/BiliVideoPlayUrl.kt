package top.colter.bilibili.data.video

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
public data class BiliVideoPlayUrl(
    @SerialName("from")
    val source: String? = null,
    val result: String? = null,
    val message: String? = null,
    val quality: Int? = null,
    val format: String? = null,
    @SerialName("timelength")
    val timeLength: Long? = null,
    @SerialName("accept_format")
    val acceptFormat: String? = null,
    @SerialName("accept_description")
    val acceptDescription: List<String> = emptyList(),
    @SerialName("accept_quality")
    val acceptQuality: List<Int> = emptyList(),
    @SerialName("video_codecid")
    val videoCodecid: Int? = null,
    @SerialName("seek_param")
    val seekParam: String? = null,
    @SerialName("seek_type")
    val seekType: String? = null,
    val durl: List<BiliVideoDurlSegment> = emptyList(),
    val dash: BiliVideoDash? = null,
    @SerialName("support_formats")
    val supportFormats: List<BiliVideoSupportFormat> = emptyList(),
    @SerialName("last_play_time")
    val lastPlayTime: Long? = null,
    @SerialName("last_play_cid")
    val lastPlayCid: Long? = null
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BiliVideoDash(
    val duration: Long? = null,
    @SerialName("minBufferTime")
    @JsonNames("min_buffer_time")
    val minBufferTime: Double? = null,
    val video: List<BiliVideoDashStream> = emptyList(),
    val audio: List<BiliVideoDashStream>? = null
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BiliVideoDashStream(
    val id: Int,
    @SerialName("baseUrl")
    @JsonNames("base_url")
    val baseUrl: String? = null,
    @SerialName("backupUrl")
    @JsonNames("backup_url")
    val backupUrl: List<String>? = null,
    val bandwidth: Long? = null,
    @SerialName("mimeType")
    @JsonNames("mime_type")
    val mimeType: String? = null,
    val codecs: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    @SerialName("frameRate")
    @JsonNames("frame_rate")
    val frameRate: String? = null,
    val sar: String? = null,
    @SerialName("startWithSap")
    @JsonNames("start_with_sap")
    val startWithSap: Int? = null,
    @SerialName("SegmentBase")
    @JsonNames("segment_base")
    val segmentBase: BiliVideoSegmentBase? = null,
    val codecid: Int? = null
) {
    public val urls: List<String>
        get() = listOfNotNull(baseUrl) + backupUrl.orEmpty()
}

@Serializable
public data class BiliVideoSegmentBase(
    val initialization: String? = null,
    @SerialName("index_range")
    val indexRange: String? = null
)

@Serializable
public data class BiliVideoDurlSegment(
    val order: Int,
    val length: Long? = null,
    val size: Long? = null,
    val ahead: String? = null,
    val vhead: String? = null,
    val url: String,
    @SerialName("backup_url")
    val backupUrl: List<String>? = null
) {
    public val urls: List<String>
        get() = listOf(url) + backupUrl.orEmpty()
}

@Serializable
public data class BiliVideoSupportFormat(
    val quality: Int,
    val format: String? = null,
    @SerialName("new_description")
    val newDescription: String? = null,
    @SerialName("display_desc")
    val displayDesc: String? = null,
    val superscript: String? = null,
    val codecs: List<String>? = null
)
