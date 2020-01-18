package nl.tudelft.ipv8.messaging

import nl.tudelft.ipv8.Address
import nl.tudelft.ipv8.Peer
import nl.tudelft.ipv8.exception.PacketDecodingException
import nl.tudelft.ipv8.keyvault.LibNaClPK
import nl.tudelft.ipv8.messaging.payload.BinMemberAuthenticationPayload

class Packet(
    val source: Address,
    val data: ByteArray
) {
    /**
     * Strips the prefix, message type, and returns the payload.
     */
    fun getPayload(): ByteArray {
        return data.copyOfRange(PREFIX_SIZE + 1, data.size)
    }

    /**
     * Checks the signature of an authenticated packet payload and throws [PacketDecodingException] if invalid. Returns
     * the peer initialized using the packet source address and the public key from [BinMemberAuthenticationPayload],
     * and the payload excluding [BinMemberAuthenticationPayload] and the signature.
     *
     * @throws PacketDecodingException If the packet is authenticated and the signature is invalid.
     */
    @Throws(PacketDecodingException::class)
    fun getAuthPayload(): Pair<Peer, ByteArray> {
        // prefix + message type
        val authOffset = PREFIX_SIZE + 1
        val (auth, authSize) = BinMemberAuthenticationPayload.deserialize(data, authOffset)
        val publicKey = LibNaClPK.fromBin(auth.publicKey)
        val signatureOffset = data.size - publicKey.getSignatureLength()
        val signature = data.copyOfRange(signatureOffset, data.size)

        // Verify signature
        val message = data.copyOfRange(0, signatureOffset)
        val isValidSignature = publicKey.verify(signature, message)
        if (!isValidSignature)
            throw PacketDecodingException("Incoming packet has an invalid signature")

        // Return the peer and remaining payloads
        val peer = Peer(LibNaClPK.fromBin(auth.publicKey), source)
        val remainder = data.copyOfRange(authOffset + authSize, data.size - publicKey.getSignatureLength())
        return Pair(peer, remainder)
    }

    companion object {
        private const val PREFIX_SIZE = 22
    }
}