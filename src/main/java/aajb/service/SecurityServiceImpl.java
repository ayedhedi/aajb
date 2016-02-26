package aajb.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Created by ayed.h on 24/02/2016.
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService {


    @Qualifier("environment")
    @Autowired
    private Environment environment;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean isMatches(String password, String encoded) {
        return passwordEncoder.matches(password, encoded);
    }


    @Override
    public String aesDecrypt(String encryptedString) {
        Cipher cipher;
        byte[] arrayBytes;
        SecretKey key;
        String UNICODE_FORMAT = "UTF8";
        String DESEDE_ENCRYPTION_SCHEME = "DESede";
        String myEncryptionKey = environment.getProperty("aes.encrypt.key");

        try {
            arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
            cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
            key = skf.generateSecret(ks);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
           return new String(plainText);

        } catch (UnsupportedEncodingException | InvalidKeySpecException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String generateMailCode() {
        int size = Integer.parseInt(environment.getProperty("mail.code.size"));
        String charSet = environment.getProperty("mail.code.chars");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<size;i++) {
           sb.append(charSet.charAt((int)(Math.random()*charSet.length())));
        }
        return sb.toString();
    }
}
