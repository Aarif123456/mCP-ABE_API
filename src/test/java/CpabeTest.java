import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.MalformedAttributesException;
import com.mitu.utils.exceptions.MalformedPolicyException;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.gcp.MapLoader.getLoadMap;
import static com.junwei.cpabe.Cpabe.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class CpabeTest {
    private static final String path = "src/test/java/";
    // The file we will encrypt
    private static final String inputFile = "RealHuman.png";
    // attributes given to the test user
    private static final String doctorAttributes = "profession:doctor location:detroit specialization:heartSurgeon department:emergency";
    private static final String studentAttributes = "objectClass:inetOrgPerson objectClass:organizationalPerson "
            + "sn:student2 cn:student2 uid:student2 userPassword:student2 "
            + "ou:idp o:computer mail:student2@sdu.edu.cn title:student";
    private static final String studentPolicy = "sn:student2 cn:student2 uid:student2 3of3";
    private static final String testPolicy = "a:foo b:bar c:fim 2of3 d:baf 1of2";


    // all possible attributes
    private JsonObject js;
    private String publicKey, masterKey;


    @BeforeEach
    public void setUp() {
        js = setup();
        publicKey = js.get("publicKey").getAsString();
        masterKey = js.get("masterKey").getAsString();
    }

    public void cpabeTest(String policy, String userAttribute, boolean areEqual) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException, MalformedPolicyException, MalformedAttributesException {
        File file = new File(path + inputFile);
        var inputFileBytes = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        js = keygen(publicKey, masterKey, userAttribute);
        String privateKey = js.get("privateKey").getAsString();
        js = encrypt(publicKey, policy, inputFileBytes);
        var encryptedFile = js.get("encryptedFile").getAsString();
        try {
            js = decrypt(publicKey, privateKey, encryptedFile);
            var decryptedFileBytes = js.get("decryptedFile").getAsString();
            assertTrue(areEqual);
            assertEquals(decryptedFileBytes, inputFileBytes);
        } catch (AttributesNotSatisfiedException e) {
            /* The policy was supposed to fail the user */
            assertFalse(areEqual);
        }

    }

    @TestFactory
    public Stream<DynamicTest> testWorkingPolicies() {
        var policies = new String[]{
                testPolicy,
                "profession:doctor location:windsor 2of2 specialization:heartSurgeon department:emergency profession:doctor 3of3 1of2",
                "profession:doctor department:emergency 2of2 specialization:heartSurgeon department:emergency 2of2 1of2",
                studentPolicy
        };

        var userAttributes = new String[]{
                "c:fim a:foo",
                doctorAttributes,
                doctorAttributes,
                studentAttributes
        };

        int testCases = policies.length;
        return IntStream.range(0, testCases)
                .mapToObj(testCase -> dynamicTest("testCase=" + testCase,
                        () -> cpabeTest(policies[testCase], userAttributes[testCase], true))
                );
    }

    @TestFactory
    public Stream<DynamicTest> testFailingPolicies() {
        var policies = new String[]{
                testPolicy,
                testPolicy,
                "profession:doctor department:emergency location:windsor 3of3",
                "profession:doctor location:windsor specialization:heartSurgeon 3of3"
        };

        var userAttributes = new String[]{
                "c:fim",
                "d:baf1 c:fim1 a:foo",
                doctorAttributes,
                doctorAttributes
        };

        int testCases = policies.length;
        return IntStream.range(0, testCases)
                .mapToObj(testCase -> dynamicTest("testCase=" + testCase,
                        () -> cpabeTest(policies[testCase], userAttributes[testCase], false))
                );
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

    /* TODO: make sure encryption works with every type of curve*/

}
