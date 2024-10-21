package Encryption;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class EncryptionUtils {

    private static final int IV_SIZE = 16;

    public static char[] toCharArray(byte[] bytes) {
        CharBuffer charBuffer = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes));
        return Arrays.copyOf(charBuffer.array(), charBuffer.limit());
    }

    public static byte[] toByteArray(char[] chars) {
        ByteBuffer byteBuffer = Charset.defaultCharset().encode(CharBuffer.wrap(chars));
        return Arrays.copyOf(byteBuffer.array(), byteBuffer.limit());
    }

    public static byte[] generateInitializationVector() {
        byte[] iv = new byte[IV_SIZE];
        for (int i = 0; i < IV_SIZE; i++) {
            iv[i] = (byte) i;  // Example IV
        }
        return iv;
    }
}
