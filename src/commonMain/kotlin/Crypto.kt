package tech.uwchrysalis.crypto

expect object Crypto{
    fun sign(msg: ByteArray, sk: ByteArray): ByteArray
    fun verify(msg: ByteArray, signature: ByteArray, pk: ByteArray): Boolean
}
