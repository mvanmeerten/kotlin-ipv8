package nl.tudelft.ipv8.android.demo.ui.transfer

import kotlinx.serialization.Serializable

@Serializable
data class SerializableBlock(val blockId: String,
                             val type: String,
                             val publicKey: ByteArray,
                             val sequenceNumber: Int,
                             val linkPublicKey: ByteArray,
                             val previousHash: ByteArray,
                             val signature: ByteArray,
                             val transaction: ByteArray,
                             val timeStamp: Long
)
