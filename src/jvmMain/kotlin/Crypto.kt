package tech.uwchrysalis.crypto
import com.goterl.lazysodium.LazySodium
import com.goterl.lazysodium.interfaces.Sign

actual class Crypto(private val libsodium: LazySodium){
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