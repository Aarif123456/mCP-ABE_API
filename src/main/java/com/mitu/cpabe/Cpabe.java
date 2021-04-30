package com.mitu.cpabe;

import com.google.gson.JsonObject;
import com.mitu.abe.*;
import com.mitu.cpabe.policy.LangPolicy;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cpabe {
    static final Map<String, String> defaultMap = new HashMap<>() {{
        put("type", "a");
        put("q", "8780710799663312522437781984754049815806883199414208211028653399266475630880222957078625179422662221423155858769582317459277713367317481324925129998224791");
        put("h", "12016012264891146079388821366740534204802954401251311822919615131047207289359704531102844802183906537786776");
        put("r", "730750818665451621361119245571504901405976559617");
        put("exp2", "159");
        put("exp1", "107");
        put("sign1", "1");
        put("sign0", "1");
    }};

    /*TODO: Create method */
    // public static JsonObject generateCurve(String type="a", int rBits = 160, int qBits = 512){}

    private static String createJsonForParameterMap(String mapString){
        var sb = new StringBuilder();
        sb.append("{");
        var mapStringProperties =  mapString.split("\n");
        for(int i=0; i<mapStringProperties.length; i++){
            var entryAndVal = mapStringProperties[i].split(":");
            sb.append("\"").
                    append(entryAndVal[0]).
                    append("\":\"").
                    append(entryAndVal[1]).
                    append("\"");
            if(i<mapStringProperties.length-1){
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    public static JsonObject generateCurve(String type, Map<String, String> parameterMap){
    	var jsonObject = new JsonObject();
    	PairingParametersGenerator generator;
    	
    	switch (type) {
            case "a": {
                /*Get arguments, parse them, */
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
                throw new IllegalArgumentException("Invalid Type of curve: "+type);
        }

    	PairingParameters parameters = generator.generate();
    	jsonObject.addProperty("properties", createJsonForParameterMap(parameters.toString(":")));
    	return jsonObject;

    }
    public static JsonObject setup(String[] attrs) {
        return setup(attrs, defaultMap);
    }

    /* Set up- takes a list of all possible attributes and the initial parameter and then returns the public
     *  encryption key and the master key (used to create private keys)
     */
    /*API RETURN: {publicKey:string, masterKey:string}*/
    public static JsonObject setup(String[] attrs, Map<String, String> parameterMap) {
        var jsonObject = new JsonObject();
        byte[] pub_byte, msk_byte;

        AbePub pub = new AbePub();
        AbeMsk msk = new AbeMsk();
        Abe.setup(pub, msk, attrs, parameterMap);

        /* store public-key in JSON object to return to server*/
        pub_byte = SerializeUtils.serializeBswabePub(pub);
        jsonObject.addProperty("publicKey", Base64.getEncoder().encodeToString(pub_byte));

        /* store AbeMsk into masterKeyFile */
        msk_byte = SerializeUtils.serializeBswabeMsk(msk);
        jsonObject.addProperty("masterKey", Base64.getEncoder().encodeToString(msk_byte));
        return jsonObject;
    }

    public static JsonObject keygen(String publicKey, String masterKey, String attr_str) {
        return keygen(publicKey, masterKey, attr_str, defaultMap);
    }

    /* Takes the public key and master key both serialized as string then return both shares of the user */
    /*API RETURN: {share1:string, share2:string}*/
    public static JsonObject keygen(String publicKey, String masterKey, String attr_str, Map<String, String> loadMap) {
        AbePub pub;
        AbeMsk msk;

        byte[] pub_byte, msk_byte, prv_bytePart1, prv_bytePart2;

        /* get AbePub from publicKeyFile */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);

        /* get AbeMsk from masterKeyFile */
        msk_byte = Base64.getDecoder().decode(masterKey);
        msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);

        String[] attr_arr = null;
        try {
            attr_arr = LangPolicy.parseAttribute(attr_str);
        } catch (Exception ex) {
            Logger.getLogger(Cpabe.class.getName()).log(Level.SEVERE, null, ex);
        }
        assert attr_arr != null;
        AbePrv prv = Abe.keygen(pub, msk, attr_arr);

        var jsonObject = new JsonObject();
        /* store AbePrv into return object  */
        prv_bytePart1 = SerializeUtils.serializeBswabePrvPart1(prv.prv1);
        jsonObject.addProperty("share1", Base64.getEncoder().encodeToString(prv_bytePart1));
        prv_bytePart2 = SerializeUtils.serializeBswabePrvPart2(prv.prv2);
        jsonObject.addProperty("share2", Base64.getEncoder().encodeToString(prv_bytePart2));
        return jsonObject;
    }

    public static JsonObject encrypt(String publicKey, String policy, String inputFileSerialized) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        return encrypt(publicKey, policy, inputFileSerialized, defaultMap);
    }

    /* Use the public key to encrypt the given file use the given policy return the byte
     *	representation of the encrypted file
     */
    /*API RETURN: {encryptedFile:string}*/
    public static JsonObject encrypt(String publicKey, String policy, String inputFileSerialized, Map<String, String> loadMap)
            throws IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException,
            NoSuchPaddingException,
            BadPaddingException {

        AbePub pub;
        AbeCph cph;
        AbeCphKey keyCph;

        byte[] cphBuf;
        byte[] aesBuf;
        byte[] pub_byte;
        Element m;

        byte[] inputFile = Base64.getDecoder().decode(inputFileSerialized);
        /* get AbePub from publicKeyFile */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);

        keyCph = Abe.enc(pub, policy);
        cph = keyCph.cph;
        pub.p.getGT().newElement();
        m = keyCph.key.duplicate();

        if (cph == null) {
            Logger.getLogger(Cpabe.class.getName()).log(Level.SEVERE, "Error happened in enc");
            System.exit(0);
        }

        cphBuf = SerializeUtils.bswabeCphSerialize(cph);
        aesBuf = AESCoder.encrypt(m.toBytes(), inputFile);
        var os = Common.writeCpabeData(cphBuf, aesBuf);
        byte[] encryptedFileBytes = os.toByteArray();
        var jsonObject = new JsonObject();
        jsonObject.addProperty("encryptedFile", Base64.getEncoder().encodeToString(encryptedFileBytes));
        return jsonObject;
    }

    public static JsonObject halfDecrypt(String publicKey, String share1, String encFile) throws NoSuchDecryptionTokenFoundException, IOException, AttributesNotSatisfiedException {
        return halfDecrypt(publicKey, share1, encFile, defaultMap);
    }

    /**
     * mdecrypt - intermediate decryption process done by the revocation server. It checks the revocation list and
     * only runs decryption if user has the appropriate permission
     * Returns half decrypted file
     */
    /*API RETURN: {mDecrypt:string}*/
    public static JsonObject halfDecrypt(String publicKey, String share1, String encFile, Map<String, String> loadMap)
            throws AttributesNotSatisfiedException,
            IOException {

        byte[] cphBuf;
        byte[] share1_byte;
        byte[] pub_byte, mDecByte;
        byte[][] tmp;

        AbeCph cph;
        AbePub pub;
        AbePrvPart1 privateKeyPart1;
        AbeMDec mDec;

        /* get AbePub from publicKeyFile */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);
        var jsonObject = new JsonObject();

        /* read ciphertext */
        tmp = Common.readCpabeData(new ByteArrayInputStream(Base64.getDecoder().decode(encFile)));
        cphBuf = tmp[1];
        cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

        /* get AbePrvPart1 form prvfilePart1 */
        share1_byte = Base64.getDecoder().decode(share1);
        privateKeyPart1 = SerializeUtils.unserializeBswabePrvPart1(pub, share1_byte);

        mDec = Abe.m_dec(pub, privateKeyPart1, cph);
        mDecByte = SerializeUtils.serializeBswabeMDec(mDec);
        jsonObject.addProperty("mDecryptedFile", Base64.getEncoder().encodeToString(mDecByte));
        return jsonObject;
    }

    public static JsonObject decrypt(String publicKey, String share2, String encFile, String mDecFile) throws IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        return decrypt(publicKey, share2, encFile, mDecFile, defaultMap);
    }

    /*Use the half decrypted file, the encrypted file and the second part of the decryption key to make the file*/
    /*API RETURN: {decryptedFile:string}*/
    public static JsonObject decrypt(String publicKey, String share2, String encFile, String mDecFile,
                                     Map<String, String> loadMap)
            throws IOException, // Regular file IO exception
            IllegalBlockSizeException, // Problem from cp-abe jbr library
            InvalidKeyException, // Exception thrown if key was in wrong format
            BadPaddingException, // Exception comes from the AES encryption used under the hood
            NoSuchAlgorithmException, // Thrown in case the environment running the server doesn't have the requested algorithm
            NoSuchPaddingException // Thrown in case a padding is requested but does not exist in environment
    {

        byte[] aesBuf, cphBuf;
        byte[] share2_byte;
        byte[] pub_byte;
        byte[][] tmp;

        AbeCph cph;
        AbePub pub;
        AbePrvPart2 privateKeyPart2;
        AbeMDec mDec = null;

        /* get AbePub from publicKeyFile */
        pub_byte = Base64.getDecoder().decode(publicKey);
        pub = SerializeUtils.unserializeBswabePub(pub_byte, loadMap);

        /* read ciphertext */
        tmp = Common.readCpabeData(new ByteArrayInputStream(Base64.getDecoder().decode(encFile)));
        aesBuf = tmp[0];
        cphBuf = tmp[1];
        cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

        share2_byte = Base64.getDecoder().decode(share2);
        privateKeyPart2 = SerializeUtils.unserializeBswabePrvPart2(pub, share2_byte);

        try {
            mDec = SerializeUtils.unserializeBswabeMDec(pub, Base64.getDecoder().decode(mDecFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        var jsonObject = new JsonObject();
        byte[] plt;
        Element m;
        pub.p.getGT().newElement();
        assert mDec != null;
        m = Abe.dec(pub, privateKeyPart2, cph, mDec).duplicate();
        plt = AESCoder.decrypt(m.toBytes(), aesBuf); 

        jsonObject.addProperty("decryptedFile", Base64.getEncoder().encodeToString(plt));
        return jsonObject;
    }

}