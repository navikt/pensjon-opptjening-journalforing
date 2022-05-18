package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util

import java.security.MessageDigest


class Md5Hash {
    companion object {
        internal fun createHashString(input: String): String = MessageDigest.getInstance("md5").digest(input.toByteArray()).toHex()
    }
}

private fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }