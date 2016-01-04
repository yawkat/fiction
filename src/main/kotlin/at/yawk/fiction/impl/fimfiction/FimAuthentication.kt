package at.yawk.fiction.impl.fimfiction

/**
 * @author yawkat
 */
data class FimAuthentication(
        val username: String,
        val password: String,
        val otp: String,
        val isRemember: Boolean
) {
    constructor(username: String, password: String) : this(username, password, "", true)
}
