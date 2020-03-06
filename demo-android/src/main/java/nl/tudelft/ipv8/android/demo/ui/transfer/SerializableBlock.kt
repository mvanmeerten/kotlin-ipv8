package nl.tudelft.ipv8.android.demo.ui.transfer

import kotlinx.serialization.*
import java.util.*

@Serializable
data class SerializableBlock(val blockId: String,
                             val type: String,
                             val publicKey: ByteArray,
                             val sequenceNumber: UInt,
                             val linkPublicKey: ByteArray,
                             val transaction: ByteArray,
                             val timestamp: Date
)
