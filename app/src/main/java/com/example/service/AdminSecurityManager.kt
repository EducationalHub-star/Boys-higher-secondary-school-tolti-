package com.example.service

import com.example.data.model.AdminSession
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID

/**
 * Enterprise-grade security helper for Admin authentication.
 * Implements salted SHA-256 password hashing, cryptographically random salt generation,
 * session token verification, and automatic session expiration.
 */
object AdminSecurityManager {

    private const val SESSION_DURATION_MILLIS = 25 * 60 * 1000L // 25 minutes of active session

    /**
     * Generates a 16-byte cryptographically secure random salt in hex string format.
     */
    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return bytesToHex(salt)
    }

    /**
     * Produces a salted hash using multi-round SHA-256.
     * Prevents rainbow tables and dictionary attacks.
     */
    fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        var currentBytes = (salt + password + salt).toByteArray(Charsets.UTF_8)
        // 1000 rounds of hashing
        repeat(1000) {
            currentBytes = digest.digest(currentBytes)
        }
        return bytesToHex(currentBytes)
    }

    /**
     * Constant-time comparison to protect against timing attacks.
     */
    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val computedHash = hashPassword(password, salt)
        return MessageDigest.isEqual(
            computedHash.toByteArray(Charsets.UTF_8),
            expectedHash.toByteArray(Charsets.UTF_8)
        )
    }

    /**
     * Creates a new timed AdminSession with an expiry timestamp.
     */
    fun createSession(username: String, role: String): AdminSession {
        val now = System.currentTimeMillis()
        return AdminSession(
            token = UUID.randomUUID().toString(),
            username = username,
            role = role,
            loginTimestamp = now,
            expiryTimestamp = now + SESSION_DURATION_MILLIS
        )
    }

    /**
     * Validates whether the given session token is still active and unexpired.
     */
    fun isSessionValid(session: AdminSession?): Boolean {
        if (session == null) return false
        val now = System.currentTimeMillis()
        return now < session.expiryTimestamp
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        val hexArray = "0123456789ABCDEF".toCharArray()
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}
