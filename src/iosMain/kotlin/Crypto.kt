package tech.uwchrysalis.crypto

actual class Crypto {
    actual fun sign(msg: ByteArray, sk: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }

    actual fun verify(msg: ByteArray, signature: ByteArray, pk: ByteArray): Boolean {
        TODO("Not yet implemented")
    }
}