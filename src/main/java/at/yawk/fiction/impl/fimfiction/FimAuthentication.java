package at.yawk.fiction.impl.fimfiction;

import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author yawkat
 */
@Value
@RequiredArgsConstructor
public class FimAuthentication {
    private final String username;
    private final String password;
    private final String otp;
    private final boolean remember;

    public FimAuthentication(String username, String password) {
        this(username, password, "", true);
    }
}
