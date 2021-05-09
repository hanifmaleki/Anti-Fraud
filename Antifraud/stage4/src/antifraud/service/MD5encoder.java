package antifraud.service;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MD5encoder {
        private final MessageDigest messageDigest;

    public MD5encoder() {
        try {
            this.messageDigest  = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not create MD5-digest instance");
        }
    }

    public String encode(String input){
        messageDigest.update(input.getBytes());
        byte[] digest = messageDigest.digest();
        return new String(digest);
    }

}
