package nl.tudelft.ipv8.peerdiscovery.payload

import nl.tudelft.ipv8.messaging.*

data class PingPayload(
    val identifier: Int
) : Serializable {
    override fun serialize(): ByteArray {
        return serializeUShort(identifier % 65536)
    }

    companion object : Deserializable<PingPayload> {
        override fun deserialize(buffer: ByteArray, offset: Int): Pair<PingPayload, Int> {
            val identifier = deserializeUShort(buffer, offset)
            return Pair(PingPayload(identifier), offset + SERIALIZED_USHORT_SIZE)
        }
    }
}
