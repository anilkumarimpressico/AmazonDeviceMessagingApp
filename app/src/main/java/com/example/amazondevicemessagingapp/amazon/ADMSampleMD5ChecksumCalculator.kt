package com.example.amazondevicemessagingapp.amazon



import android.util.Base64
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * This class provides a method to calculate the checksum on a collection of
 * key-value pairs by the algorithm defined in the ADM documentation.
 */
object ADMSampleMD5ChecksumCalculator {
    /** The byte encoding used in the MD5 computation. */
    private const val ENCODING = "UTF-8"

    /** The algorithm used in the MD5 computation. */
    private const val ALGORITHM = "MD5"

    /**
     * Calculates the MD5 checksum of the provided collection of key-value pairs
     * according to the algorithm defined in the ADM documentation:
     *
     * 1. Sort the key-value pairs using a UTF-8 code unit-based comparison of
     *    the keys.
     *
     * 2. Concatenate the series of pairs in the format:
     *    a. "key1:value1,key2:value2"
     *    b. There should be no whitespace between the ':' character and either the
     *       keys or values. There should also be no whitespace in between each pair
     *       and the ',' character.
     *
     * 3. Compute the MD5 using the UTF-8 bytes of the string produced in step 2
     *    according to the algorithm defined in RFC 1321.
     *
     * 4. Base-64 encode the 128-bit output of the MD5 algorithm that was
     *    computed in step 3.
     *
     * @param input The input to compute the MD5 checksum on.
     * @return The MD5 checksum of the input.
     */
    fun calculateChecksum(input: Map<String, String>): String {
        val serializedMapData = getSerializedMap(input)
        val md5Bytes = getMd5Bytes(serializedMapData)
        val base64Digest = Base64.encodeToString(md5Bytes, Base64.DEFAULT)
        return base64Digest
    }

    /**
     * This method sorts the collection of key-value pairs by a UTF-8 code unit
     * comparison, and serializes the result.
     *
     * @param input The input to serialize.
     * @return The serialized version of the input.
     */
    private fun getSerializedMap(input: Map<String, String>): String {
        val sortedMap = TreeMap<String, String>(UTF8CodeUnitStringComparator())
        sortedMap.putAll(input)

        val builder = StringBuilder()
        var numElements = 1
        for ((key, value) in sortedMap) {
            builder.append("$key:$value")
            if (numElements++ < sortedMap.size) {
                builder.append(",")
            }
        }
        return builder.toString()
    }

    /**
     * Generates an MD5 using the UTF-8 bytes of a string.
     *
     * @param input The string used to generate the MD5 digest.
     * @return The bytes of the MD5 result.
     */
    private fun getMd5Bytes(input: String): ByteArray {
        val serializedBytes: ByteArray
        try {
            serializedBytes = input.toByteArray(charset(ENCODING))
        } catch (e: UnsupportedEncodingException) {
            throw UnsupportedOperationException("$ENCODING not supported!", e)
        }
        val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance(ALGORITHM)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.UnsupportedOperationException(
                ENCODING
                        + " not supported!", e
            )
        }
        return serializedBytes
    }

    private class UTF8CodeUnitStringComparator : Comparator<String>, Serializable {

        override fun compare(str1: String, str2: String): Int {
            try {
                val bytes1 = str1.toByteArray(charset(ENCODING))
                val bytes2 = str2.toByteArray(charset(ENCODING))
                val loopBounds = Math.min(bytes1.size, bytes2.size)
                for (i in 0 until loopBounds) {
                    val ub1 = bytes1[i].toInt() and 0xFF
                    val ub2 = bytes2[i].toInt() and 0xFF
                    if (ub1 != ub2) {
                        return ub1 - ub2
                    }
                }
                return bytes1.size - bytes2.size
            } catch (e: UnsupportedEncodingException) {
                throw UnsupportedOperationException("$ENCODING not supported!", e)
            }
        }
    }

}

