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
    val type: OfficialVerifyType,
    val desc: String = "",
    val title: String = "",
)

/**
 * 官方认证类型
 *
 * [NONE] 无认证
 *
 * [PERSONA] 个人认证
 *
 * [ORGANIZATION] 机构认证
 */
@Serializable(OfficialVerifyTypeSerializer::class)
public enum class OfficialVerifyType(public val value: Int, public val text: String){
    NONE(-1, "无认证"),
    PERSONA(0, "个人认证"),
    ORGANIZATION(1, "机构认证")
}

private object OfficialVerifyTypeSerializer: KSerializer<OfficialVerifyType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(OfficialVerifyType::class.qualifiedName!!, PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): OfficialVerifyType {
        return decoder.decodeInt().run {
            OfficialVerifyType.values().find { it.value == this } ?: throw IndexOutOfBoundsException("认证类型错误: $this")
        }
   }
    override fun serialize(encoder: Encoder, value: OfficialVerifyType) = encoder.encodeInt(value.value)
}