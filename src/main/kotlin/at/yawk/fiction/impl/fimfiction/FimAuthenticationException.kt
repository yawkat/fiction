package at.yawk.fiction.impl.fimfiction

/**
 * @author yawkat
 */
class FimAuthenticationException(val kind: String) : Exception("Failed to log in: " + kind)
