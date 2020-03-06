package nl.tudelft.ipv8.android.demo.ui.transfer

import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import nl.tudelft.ipv8.attestation.trustchain.TrustChainBlock
import java.util.*

class TransferBlockParser {
    val UNKNOWN_SEQ = 0u


    fun proposalToString(block: TrustChainBlock): String {

        val serializableBlock: SerializableBlock = SerializableBlock(
            block.blockId,
            block.type,
            block.publicKey,
            block.sequenceNumber.toInt(),
            block.linkPublicKey,
            block.previousHash,
            block.signature,
            block.rawTransaction,
            block.timestamp.time
        )
        val json = Json(JsonConfiguration.Stable)
        return json.stringify(SerializableBlock.serializer(), serializableBlock)
    }

    fun stringToProposal(jsonString: String): TrustChainBlock {
        val json = Json(JsonConfiguration.Stable)
        val serializableBlock = json.parse(SerializableBlock.serializer(), jsonString)
        return TrustChainBlock(serializableBlock.type, serializableBlock.transaction, serializableBlock.publicKey, serializableBlock.sequenceNumber.toUInt(),
        serializableBlock.linkPublicKey, UNKNOWN_SEQ, serializableBlock.previousHash, serializableBlock.signature, Date(serializableBlock.timeStamp))
    }
}
