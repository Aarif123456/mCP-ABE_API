package com.junwei.cpabe;

import com.google.gson.JsonObject;
import com.junwei.bswabe.*;
import com.junwei.cpabe.policy.LangPolicy;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.MalformedAttributesException;
import com.mitu.utils.exceptions.MalformedPolicyException;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeECurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Cpabe {
    public static final Map<String, String> defaultMap = new HashMap<>() {{
        put("type", "a");
        put("q", "6369360761975870121762638569043728784537618524939795659581725459764519487632130011828250189407458902559556082799450871019292319559501351774206580668498539");
        put("h", "8716186967272981946338881743674542758348861153366380201939968258588498384514697695543719672616341557740140");
        put("r", "730750818665451459101842416358141509827966402561");
        put("exp2", "159");
        put("exp1", "17");
        put("sign1", "1");
        put("sign0", "1");
    }};

    private static String createJsonForParameterMap(String mapString) {
        var sb = new StringBuilder();
        sb.append("{");
        var mapStringProperties = mapString.split("\n");
        for (int i = 0; i < mapStringProperties.length; i++) {
            var entryAndVal = mapStringProperties[i].split(":");
            sb.append("\"").
                    append(entryAndVal[0]).
                    append("\":\"").
                    append(entryAndVal[1]).
                    append("\"");
            if (i < mapStringProperties.length - 1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /*API RETURN: {properties:string}*/
    public static JsonObject generateCurve(String type, Map<String, String> parameterMap) {
        var jsonObject = new JsonObject();
        PairingParametersGenerator generator;

        switch (type) {
            case "a": {
                var rBits = Integer.parseInt(parameterMap.getOrDefault("rBits", "160"));
                var qBits = Integer.parseInt(parameterMap.getOrDefault("qBits", "512"));
                generator = new TypeACurveGenerator(rBits, qBits);
                break;
            }
            case "a1":
                var numPrimes = Integer.parseInt(parameterMap.getOrDefault("numPrimes", "2"));
                var bits = Integer.parseInt(parameterMap.getOrDefault("bits", "512"));
                generator = new TypeA1CurveGenerator(numPrimes, bits);
                break;
            case "e": {
                var rBits = Integer.parseInt(parameterMap.getOrDefault("rBits", "160"));
                var qBits = Integer.parseInt(parameterMap.getOrDefault("qBits", "512"));
                generator = new TypeECurveGenerator(rBits, qBits);
                break;
            }
            case "f": {
                var rBits = Integer.parseInt(parameterMap.getOrDefault("rBits", "160"));
                generator = new TypeFCurveGenerator(rBits);
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid Type of curve: " + type);
        }

        PairingParameters parameters = generator.generate();
        jsonObject.addProperty("properties", createJsonForParameterMap(parameters.toString(":")));
        return jsonObject;

    }

    /**********************************************************************************************************************/
    public static JsonObject setup() {
        return setup(defaultMap);
    }

    /* Set up- takes a list of all possible attributes and the initial parameter and then returns the public
     *  encryption key and the master key (used to create private keys)
     */
    /*API RETURN: {publicKey:string, masterKey:string}*/
    public static JsonObject setup(Map<String, String> parameterMap) {
        var jsonObject = new JsonObject();
        byte[] pub_byte, msk_byte;
        BswabePub pub = new BswabePub();
        BswabeMsk msk = new BswabeMsk();
        Bswabe.setup(pub, msk, parameterMap);

        pub_byte = SerializeUtils.serializeBswabePub(pub);
        jsonObject.addProperty("publicKey", Base64.getEncoder().encodeToString(pub_byte));

        msk_byte = SerializeUtils.serializeBswabeMsk(msk);
        jsonObject.addProperty("masterKey", Base64.getEncoder().encodeToString(msk_byte));
        return jsonObject;
    }

    /**********************************************************************************************************************/
    public static JsonObject keygen(String publicKey, String masterKey, String attr_str) throws NoSuchAlgorithmException, MalformedAttributesException {
        return keygen(publicKey, masterKey, attr_str, defaultMap);
    }

    /* Takes the public key and master key both serialized as string then return both shares of the user */
    /*API RETURN: {share1:string}*/
    public static JsonObject keygen(String publicKey, String masterKey, String attr_str, Map<String, String> loadMap) throws NoSuchAlgorithmException, MalformedAttributesException {
        var jsonObject = new JsonObject();
        BswabePub pub;
        BswabeMsk msk;
        byte[] pub_byte, msk_byte, prv_byte;

        /* get BswabePub from publicKey */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);

        /* get BswabeMsk from masterKey */
        msk_byte = Base64.getDecoder().decode(masterKey);
        msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);

        String[] attr_arr = LangPolicy.parseAttribute(attr_str);
        BswabePrv prv = Bswabe.keygen(pub, msk, attr_arr);

        /* store BswabePrv into privateKey */
        prv_byte = SerializeUtils.serializeBswabePrv(prv);
        jsonObject.addProperty("privateKey", Base64.getEncoder().encodeToString(prv_byte));

        return jsonObject;
    }

    /**********************************************************************************************************************/
    public static JsonObject encrypt(String publicKey, String policy, String inputFileSerialized) throws IllegalBlockSizeException, NoSuchAlgorithmException, MalformedPolicyException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, IOException {
        return encrypt(publicKey, policy, inputFileSerialized, defaultMap);
    }

    /* Use the public key to encrypt the given file use the given policy return the byte
     *	representation of the encrypted file
     */
    /* API RETURN: {encryptedFile:string} */
    public static JsonObject encrypt(String publicKey, String policy, String inputFileSerialized, Map<String, String> loadMap) throws IllegalBlockSizeException, NoSuchAlgorithmException, MalformedPolicyException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, IOException {

        var jsonObject = new JsonObject();

        BswabePub pub;
        BswabeCph cph;
        BswabeCphKey keyCph;
        byte[] cphBuf;
        byte[] aesBuf;
        byte[] pub_byte;
        Element m;

        byte[] inputFile = Base64.getDecoder().decode(inputFileSerialized);

        /* get BswabePub from publicKey */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);

        keyCph = Bswabe.encrypt(pub, policy);
        cph = keyCph.cph;
        m = keyCph.key;

        if (cph == null) {
            throw new InvalidKeyException("Error happened in encrypt");
        }

        cphBuf = SerializeUtils.bswabeCphSerialize(cph);
        aesBuf = AESCoder.encrypt(m.toBytes(), inputFile);
        var os = Common.writeCpabeData(cphBuf, aesBuf);
        byte[] encryptedFileBytes = os.toByteArray();

        jsonObject.addProperty("encryptedFile", Base64.getEncoder().encodeToString(encryptedFileBytes));
        return jsonObject;
    }

    /**********************************************************************************************************************/
    public static JsonObject decrypt(String publicKey, String privateKey, String encryptedFile) throws IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, IOException, AttributesNotSatisfiedException {
        return decrypt(publicKey, privateKey, encryptedFile, defaultMap);
    }

    public static JsonObject decrypt(String publicKey, String privateKey, String encryptedFile, Map<String, String> loadMap)
            throws IOException,
            IllegalBlockSizeException, // Problem from cp-abe jbr library
            InvalidKeyException, // Exception thrown if key was in wrong format
            BadPaddingException, // Exception comes from the AES encryption used under the hood
            NoSuchAlgorithmException, // Thrown in case the environment running the server doesn't have the requested algorithm
            NoSuchPaddingException, AttributesNotSatisfiedException // Thrown in case a padding is requested but does not exist in environment
    {
        var jsonObject = new JsonObject();

        byte[] aesBuf, cphBuf;
        byte[] prv_byte;
        byte[] pub_byte;
        byte[] decryptedFileBytes;
        byte[][] tmp;
        BswabeCph cph;
        BswabePrv prv;
        BswabePub pub;

        /* get BswabePub from publicKey */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);

        /* read ciphertext */
        tmp = Common.readCpabeData(new ByteArrayInputStream(Base64.getDecoder().decode(encryptedFile)));
        aesBuf = tmp[0];
        cphBuf = tmp[1];
        cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

        /* get BswabePrv form privateKey */
        prv_byte = Base64.getDecoder().decode(privateKey);
        prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

        BswabeElementBoolean beb = Bswabe.decrypt(pub, prv, cph);
        decryptedFileBytes = AESCoder.decrypt(beb.e.toBytes(), aesBuf);

        jsonObject.addProperty("decryptedFile", Base64.getEncoder().encodeToString(decryptedFileBytes));
        return jsonObject;
    }
}