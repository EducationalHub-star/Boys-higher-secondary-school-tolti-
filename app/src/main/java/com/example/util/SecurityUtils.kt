package com.example.util

import java.security.MessageDigest
import java.util.UUID

/**
 * Cryptographic utility for secure password hashing and verification
 * for the Boys Higher Secondary School Tolti Admin Panel.
 */
object SecurityUtils {

    /**
     * Hashes password using SHA-256 with a unique salt.
     */
    fun hashPassword(password: String, salt: String): String {
        val input = salt + password + "bhss_tolti_pepper_2026"
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Generates a random cryptographic salt.
     */
    fun generateSalt(): String {
        return UUID.randomUUID().toString().replace("-", "").take(16)
    }

    /**
     * Verifies that the provided plain password matches the stored hash and salt.
     */
    fun verifyPassword(plainPassword: String, salt: String, storedHash: String): Boolean {
        val computedHash = hashPassword(plainPassword, salt)
        return computedHash.equals(storedHash, ignoreCase = true)
    }
}
