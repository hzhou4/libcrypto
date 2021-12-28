package tech.uwchrysalis.crypto

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File

fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

internal class JvmCryptoTest {

    data class Ed25519(val sk: ByteArray, val pk: ByteArray, val msg: ByteArray, val sign: ByteArray) {
        override fun equals(other: Any?): Boolean {
            throw NotImplementedError();
        }

        override fun hashCode(): Int {
            throw NotImplementedError();
        }
    }

    companion object {
        @JvmStatic
        private fun ed25519SignInput(): Iterator<Ed25519> {
            val file = File("./src/jvmTest/resources/ed25519_sign.txt").bufferedReader();
            return generateSequence {
                when (val line = file.readLine()) {
                    null -> null
                    else -> {
                        val (sk, pk, msg, sign) = line.split(":").map { it.decodeHex() }
                        Ed25519(sk, pk, msg, sign.sliceArray(0 until sign.size - msg.size))
                    }
                }
            }.iterator()
        }
    }

    @ParameterizedTest
    @MethodSource("ed25519SignInput")
    fun testSignatures(testData: Ed25519) {
        val actualSignature = Crypto.sign(testData.msg, testData.sk)
        assertArrayEquals(actualSignature, testData.sign)
        assertTrue(Crypto.verify(testData.msg, actualSignature, testData.pk))
    }
}