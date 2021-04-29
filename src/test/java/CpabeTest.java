import com.google.gson.JsonObject;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.mitu.cpabe.Cpabe.*;

public class CpabeTest {
    @Test
    public static void main(String[] args) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchDecryptionTokenFoundException, AttributesNotSatisfiedException {
        // all possible attributes
        String[] attributeUniverse = {"doctor", "windsor", "detroit", "heart_surgeon", "emergency"};
        var userAttribute = "heart_surgeon doctor detroit"; // attributes given to the test user
        var policy = "(doctor AND detroit AND heart_surgeon)"; // Policy used for encrypting file
        var path = "src/test/java/";
        var inputFile = "RealHuman.png"; // The file we will encrypt
        var decryptedFile = "DECRYPTED_" + inputFile;
        JsonObject js;

        js = setup(attributeUniverse);
        var publicKey = js.get("publicKey").getAsString();
        var masterKey = js.get("masterKey").getAsString();

        js = keygen(publicKey, masterKey, userAttribute);
        var share1 = js.get("share1").getAsString();
        var share2 = js.get("share2").getAsString();

        File file = new File(path + inputFile);
        var inputFileBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));

        js = encrypt(publicKey, policy, inputFileBytes);
        var encryptedFile = js.get("encryptedFile").getAsString();

        js = halfDecrypt(publicKey, share1, encryptedFile);
        var mDecryptedFile = js.get("mDecryptedFile").getAsString();

        js = decrypt(publicKey, share2, encryptedFile, mDecryptedFile);
        byte[] d = Base64.getDecoder().decode(js.get("decryptedFile").getAsString());
        File f = new File(path + decryptedFile);
        var fileOutputStream = new FileOutputStream(f);
        fileOutputStream.write(d);
        System.out.println();
    }

}
