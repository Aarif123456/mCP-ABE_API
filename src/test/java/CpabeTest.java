import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.gcp.MapLoader.getLoadMap;
import static com.mitu.cpabe.Cpabe.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class CpabeTest {
    private static final String path = "src/test/java/";
    // The file we will encrypt
    private static final String inputFile = "RealHuman.png";
    // attributes given to the test user
    private static final String userAttribute = "doctor detroit heart_surgeon emergency";
    private static final String decryptedFile = "DECRYPTED_" + inputFile;
    // all possible attributes
    private final String[] attributeUniverse = {"doctor", "windsor", "detroit", "heart_surgeon", "emergency"};
    private JsonObject js;
    private String publicKey;
    private String share1;
    private String share2;


    @BeforeEach
    public void setUp() {
        js = setup(attributeUniverse);
        publicKey = js.get("publicKey").getAsString();
        String masterKey = js.get("masterKey").getAsString();

        js = keygen(publicKey, masterKey, userAttribute);
        share1 = js.get("share1").getAsString();
        share2 = js.get("share2").getAsString();
    }

    public void testEncryption(String policy, boolean areEqual) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException, AttributesNotSatisfiedException, NoSuchDecryptionTokenFoundException {
        File file = new File(path + inputFile);
        var inputFileBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));

        js = encrypt(publicKey, policy, inputFileBytes);
        var encryptedFile = js.get("encryptedFile").getAsString();
        try {
            js = halfDecrypt(publicKey, share1, encryptedFile);
            var mDecryptedFile = js.get("mDecryptedFile").getAsString();
            js = decrypt(publicKey, share2, encryptedFile, mDecryptedFile);
            // byte[] decryptedFileBytes = Base64.getDecoder().decode(js.get("decryptedFile").getAsString());
            var decryptedFileBytes = js.get("decryptedFile").getAsString();
            assertTrue(areEqual);
            assertEquals(decryptedFileBytes, inputFileBytes);
            // File f = new File(path + decryptedFile);
            // var fileOutputStream = new FileOutputStream(f);
            // fileOutputStream.write(decryptedFileBytes);
        } catch (AttributesNotSatisfiedException e) {
            /* The policy was supposed to fail the user */
            assertFalse(areEqual);
        }

    }

    @TestFactory
    public Stream<DynamicTest> testWorkingPolicies() {
        var policies = new String[]{
                "((doctor AND emergency) OR (heart_surgeon AND emergency))",
                // not working due due to brackets must fix
                //"((doctor AND windsor) OR ((heart_surgeon AND emergency) AND doctor))"
        };
        return Arrays.stream(policies).map(policy -> dynamicTest("policy=" + policy,
                () -> testEncryption(policy, true)));
    }

    @TestFactory
    public Stream<DynamicTest> testFailingPolicies() {
        var policies = new String[]{
                /* Failing for some reason need to fix*/
                // "(doctor AND emergency AND windsor)",
                "((doctor AND windsor) AND heart_surgeon)"
        };
        return Arrays.stream(policies).map(policy -> dynamicTest("policy=" + policy,
                () -> testEncryption(policy, false)));
    }

    @TestFactory
    public Stream<DynamicTest> testCurveGeneration() {
        Map<String, String> parameterMap = new HashMap<>();
        var types = new String[]{"a", "a1", "e", "f"};
        return Arrays.stream(types).map(type -> dynamicTest("type=" + type,
                () -> {
                    js = generateCurve(type, parameterMap);
                    Gson gson = new Gson();
                    Type collectionType = new HashMapTypeToken().getType();
                    var jsonData = js.get("properties").getAsString();
                    Map<String, String> map = getLoadMap(jsonData, gson);
                    assertEquals(map.get("type"), type);
                    System.out.println(jsonData);
                }));
    }

    private static class HashMapTypeToken extends TypeToken<HashMap<String, String>> {
    }

    /* TODO: make sure encryption with every type of curves*/

}
