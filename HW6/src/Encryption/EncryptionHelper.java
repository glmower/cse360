package Encryption;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionHelper {

    private static final String PROVIDER = "BC";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    private Cipher cipher;
    private SecretKey key;
    private byte[] keyBytes = new byte[]{
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17
    };

    public EncryptionHelper() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        key = new SecretKeySpec(keyBytes, "AES");
    }

    public byte[] encrypt(String plainText, byte[] iv) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public String decrypt(byte[] cipherText, byte[] iv) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(cipherText);
        return new String(decryptedBytes, "UTF-8");
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        for (int i = 0; i < IV_LENGTH; i++) {
            iv[i] = (byte) i;  // Example IV
        }
        return iv;
    }
}
