package at.yawk.fiction.impl.fimfiction;

import lombok.Getter;

/**
 * @author yawkat
 */
public class FimAuthenticationException extends Exception {
    @Getter private final String kind;

    public FimAuthenticationException(String kind) {
        super("Failed to log in: " + kind);
        this.kind = kind;
    }
}
