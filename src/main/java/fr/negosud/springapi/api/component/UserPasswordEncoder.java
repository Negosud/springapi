package fr.negosud.springapi.api.component;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
final public class UserPasswordEncoder {

    private final Argon2PasswordEncoder passwordEncoder;

    public UserPasswordEncoder() {
        this.passwordEncoder = new Argon2PasswordEncoder(32, 128, 8, 65535, 4);
    }

    public String hashUserPassword(String password) {
        password = this.passwordEncoder.encode(password);
        return password;
    }

    public boolean matchUserPassword(String inputUserPassword, String hashedUserPassword) {
        return this.passwordEncoder.matches(inputUserPassword, hashedUserPassword);
    }
}
