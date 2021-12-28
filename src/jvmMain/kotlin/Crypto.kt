package tech.uwchrysalis.crypto

import com.goterl.lazysodium.*
import com.goterl.lazysodium.interfaces.Sign

actual object Crypto {
    private enum class Sodium(val requiredClasses: Set<String>) {
        ANDROID(setOf("com.goterl.lazysodium.LazySodiumAndroid", "com.goterl.lazysodium.SodiumAndroid")) {
            override fun load() = LazySodiumAndroid(SodiumAndroid())
        },
        JAVA(setOf("com.goterl.lazysodium.LazySodiumJava", "com.goterl.lazysodium.SodiumJava")) {
            override fun load() = LazySodiumJava(SodiumJava())
        };

        fun exists(): Boolean = try {
            requiredClasses.forEach { Class.forName(it) }
            true
        } catch (_: ClassNotFoundException) {
            false
        }

        abstract fun load(): LazySodium
    }

    private val libsodium = when {
        Sodium.ANDROID.exists() -> Sodium.ANDROID.load()
        Sodium.JAVA.exists() -> Sodium.JAVA.load()
        else -> throw ClassNotFoundException("Require either LazySodiumJava or LazySodiumAndroid")
    }

    actual fun sign(msg: ByteArray, sk: ByteArray): ByteArray {
        val signature = ByteArray(Sign.BYTES)
        if (!libsodium.cryptoSignDetached(signature, msg, msg.size.toLong(), sk)) {
            throw Exception("Libsodium signing failed")
        }
        return signature;
    }

    actual fun verify(msg: ByteArray, signature: ByteArray, pk: ByteArray): Boolean {
        return libsodium.cryptoSignVerifyDetached(signature, msg, msg.size, pk)
    }
}