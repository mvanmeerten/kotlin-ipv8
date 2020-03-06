package nl.tudelft.ipv8.android.demo.ui.transfer

import ch.qos.logback.core.encoder.ByteArrayUtil
import nl.tudelft.ipv8.attestation.trustchain.TrustChainBlock
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import kotlinx.serialization.json.*
import kotlinx.serialization.json.JSON.Companion.unquoted

class TransferBlockParser {
    fun toStringProposal(block: TrustChainBlock): Map<String, Any> {

        val serializableBlock: SerializableBlock = SerializableBlock(
            block.blockId,
            block.type,
            block.publicKey,
            block.sequenceNumber,
            block.linkPublicKey,
            block.rawTransaction,
            block.timestamp
        )

        val json = JSON(JSON.. Stable . copy (unquoted = true))
    }
}
