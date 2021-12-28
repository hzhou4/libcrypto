package tech.uwchrysalis.crypto
import com.goterl.lazysodium.*
import com.goterl.lazysodium.interfaces.Sign

actual class Crypto(){
    companion object{
        private fun libsodiumJava(): Boolean =
            try {
                Class.forName("com.goterl.lazysodium.LazySodiumJava")
                Class.forName("com.goterl.lazysodium.SodiumJava")
                true
            } catch (_: ClassNotFoundException) {
                false
            }

        private fun libsodiumAndroid(): Boolean =
            try {
                Class.forName("com.goterl.lazysodium.LazySodiumAndroid")
                Class.forName("com.goterl.lazysodium.SodiumAndroid")
                true
            } catch (_: ClassNotFoundException) {
                false
            }

        val libsodium =
            when {
                libsodiumJava() -> LazySodiumJava(SodiumJava())
                libsodiumAndroid() -> LazySodiumAndroid(SodiumAndroid())
                else -> throw ClassNotFoundException("Require either LazySodiumJava or LazySodiumAndroid")
            }
    }
    actual fun sign(msg: ByteArray, sk: ByteArray): ByteArray {
        val signature=ByteArray(Sign.BYTES)
        if(!libsodium.cryptoSignDetached(signature, msg, msg.size.toLong(), sk)){
            throw Exception("Libsodium signing failed")
        }
        return signature;
    }

    actual fun verify(msg: ByteArray, signature: ByteArray, pk: ByteArray): Boolean {
        return libsodium.cryptoSignVerifyDetached(signature, msg, msg.size, pk)
    }
}