package top.colter.bilibili.data.user

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * 官方认证
 * @param type 认证类型 [OfficialVerifyType]
 * @param desc 描述
 * @param title 标题
 *
 * @see OfficialVerifyType
 */
@Serializable
public data class OfficialVerify(
    val role: OfficialVerifyRole? = null,
    val type: OfficialVerifyType,
    val desc: String = "",
    val title: String? = null
)

/**
 * 官方认证类型
 *
 * [NONE] 无认证
 *
 * [INDIVIDUAL] 个人认证
 *
 * [INSTITUTION] 机构认证
 */
@Serializable(OfficialVerifyTypeSerializer::class)
public enum class OfficialVerifyType(public val value: Int, public val text: String){
    NONE(-1, "无认证"),
    INDIVIDUAL(0, "个人认证"),
    INSTITUTION(1, "机构认证")
}

public object OfficialVerifyTypeSerializer: KSerializer<OfficialVerifyType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(OfficialVerifyType::class.qualifiedName!!, PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): OfficialVerifyType {
        return decoder.decodeInt().run {
            OfficialVerifyType.entries.find { it.value == this } ?: throw IndexOutOfBoundsException("认证类型错误: $this")
        }
   }
    override fun serialize(encoder: Encoder, value: OfficialVerifyType): Unit = encoder.encodeInt(value.value)
}


/**
 * 官方认证角色
 *
 * [NONE] 无认证
 *
 * [INDIVIDUAL_POPULAR_UPLOADER] 个人认证-知名UP主
 * [INDIVIDUAL_TOP_INFLUENCER] 个人认证-大V达人
 * [INSTITUTION_ENTERPRISE] 机构认证-企业
 * [INSTITUTION_ORGANIZATION] 机构认证-组织
 * [INSTITUTION_MEDIA] 机构认证-媒体
 * [INSTITUTION_GOVERNMENT] 机构认证-政府
 * [INDIVIDUAL_HIGH_ENERGY_STREAMER] 个人认证-高能主播
 * [INDIVIDUAL_PUBLIC_FIGURE] 个人认证-社会知名人士
 *
 */
@Serializable(OfficialVerifyRoleSerializer::class)
public enum class OfficialVerifyRole(public val value: Int, public val text: String){
    NONE(0, "无"),
    INDIVIDUAL_POPULAR_UPLOADER(1, "个人认证-知名UP主"),
    INDIVIDUAL_TOP_INFLUENCER(2, "个人认证-大V达人"),
    INSTITUTION_ENTERPRISE(3, "机构认证-企业"),
    INSTITUTION_ORGANIZATION(4, "机构认证-组织"),
    INSTITUTION_MEDIA(5, "机构认证-媒体"),
    INSTITUTION_GOVERNMENT(6, "机构认证-政府"),
    INDIVIDUAL_HIGH_ENERGY_STREAMER(7, "个人认证-高能主播"),
    INDIVIDUAL_PUBLIC_FIGURE(9, "个人认证-社会知名人士"),
}

public object OfficialVerifyRoleSerializer: KSerializer<OfficialVerifyRole> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(OfficialVerifyRole::class.qualifiedName!!, PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): OfficialVerifyRole {
        return decoder.decodeInt().run {
            OfficialVerifyRole.entries.find { it.value == this } ?: throw IndexOutOfBoundsException("认证类型错误: $this")
        }
    }
    override fun serialize(encoder: Encoder, value: OfficialVerifyRole): Unit = encoder.encodeInt(value.value)
}
