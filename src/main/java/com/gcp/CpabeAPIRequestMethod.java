package com.gcp;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.gcp.MapLoader.getLoadMap;
import static com.mitu.cpabe.Cpabe.*;

public class CpabeAPIRequestMethod {
    public static String respondQuery(HttpRequest request, PrintWriter writer) {
        var method = "";
        Gson gson = new Gson();
        JsonObject js;
        Optional<String> methodParam = request.getFirstQueryParameter("method");
        if (methodParam.isEmpty()) return null;
        method = methodParam.get();
        switch (method) {
            case "setup":
                js = setupRequest(request, gson);
                break;
            case "keygen": {
                js = keygenQuery(request, gson);
                break;
            }
            case "encrypt": {
                js = encryptQuery(request, gson);
                break;
            }
            case "halfDecrypt": {
                js = halfDecryptQuery(request, gson);
                break;
            }
            case "decrypt": {
                js = decryptQuery(request, gson);
                break;
            }
            case "generateProperties": {
                js = generateCurveQuery(request, gson);
                break;
            }
            default:
                js = new JsonObject();
                js.addProperty("Error", "Method type not recognized of type\"" + method + "\"");
        }
        writer.write(gson.toJson(js));
        return method;
    }

    private static JsonObject setupRequest(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> attributeUniverseParam = request.getFirstQueryParameter("attributeUniverse");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (attributeUniverseParam.isPresent()) {
            String[] attributeUniverse = gson.fromJson(attributeUniverseParam.get(), String[].class);
            var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
            js = setup(attributeUniverse, loadMap);
        } else {
            js.addProperty("Error", "Missing argument for the setup function");
        }

        return js;
    }

    private static JsonObject keygenQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> masterKeyParam = request.getFirstQueryParameter("masterKey");
        Optional<String> userAttributesParam = request.getFirstQueryParameter("userAttributes");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (publicKeyParam.isPresent() && masterKeyParam.isPresent() && userAttributesParam.isPresent()) {
            var publicKey = publicKeyParam.get();
            var masterKey = masterKeyParam.get();
            var userAttributes = userAttributesParam.get();
            var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
            js = keygen(publicKey, masterKey, userAttributes, loadMap);
        } else {
            js.addProperty("Error", "Missing argument for the keygen function");
        }
        return js;
    }

    private static JsonObject encryptQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> policyParam = request.getFirstQueryParameter("policy");
        Optional<String> inputFileParam = request.getFirstQueryParameter("inputFile");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (publicKeyParam.isPresent() && policyParam.isPresent() && inputFileParam.isPresent()) {
            var publicKey = publicKeyParam.get();
            var policy = policyParam.get();
            var inputFile = inputFileParam.get();
            try {
                var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
                js = encrypt(publicKey, policy, inputFile, loadMap);
            } catch (IOException |
                    NoSuchPaddingException |
                    NoSuchAlgorithmException |
                    InvalidKeyException |
                    IllegalBlockSizeException |
                    BadPaddingException e) {
                e.printStackTrace();
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for encrypt function");
        }
        return js;
    }

    private static JsonObject halfDecryptQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> share1Param = request.getFirstQueryParameter("share1");
        Optional<String> encryptedFileParam = request.getFirstQueryParameter("encryptedFile");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (publicKeyParam.isPresent() && share1Param.isPresent() &&
                encryptedFileParam.isPresent()) {
            var publicKey = publicKeyParam.get();
            var share1 = share1Param.get();
            var encryptedFile = encryptedFileParam.get();
            try {
                var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
                js = halfDecrypt(publicKey, share1, encryptedFile, loadMap);
            } catch (IOException |
                    AttributesNotSatisfiedException e) {
                e.printStackTrace();
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for the half-decrypt function");
        }
        return js;
    }

    private static JsonObject decryptQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> share2Param = request.getFirstQueryParameter("share2");
        Optional<String> encryptedFileParam = request.getFirstQueryParameter("encryptedFile");
        Optional<String> mDecryptedFileParam = request.getFirstQueryParameter("mDecryptedFile");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (publicKeyParam.isPresent() && share2Param.isPresent() &&
                encryptedFileParam.isPresent() && mDecryptedFileParam.isPresent()) {
            var publicKey = publicKeyParam.get();
            var share2 = share2Param.get();
            var encryptedFile = encryptedFileParam.get();
            var mDecryptedFile = mDecryptedFileParam.get();
            try {
                var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
                js = decrypt(publicKey, share2, encryptedFile, mDecryptedFile, loadMap);
            } catch (IllegalBlockSizeException |
                    NoSuchAlgorithmException |
                    IOException |
                    BadPaddingException |
                    NoSuchPaddingException |
                    InvalidKeyException e) {
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for the decrypt function");
        }
        return js;
    }

    private static JsonObject generateCurveQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> typeParam = request.getFirstQueryParameter("type");
        Optional<String> parameterMapParam = request.getFirstQueryParameter("parameterMap");
        if (typeParam.isPresent()) {
            var type = typeParam.get();
            Map<String, String> parameterMap = parameterMapParam.isPresent() ?
                    getLoadMap(parameterMapParam.get(), gson) :
                    new HashMap<>();
            try {
                js = generateCurve(type, parameterMap);
            } catch (NumberFormatException ignored) {
                js.addProperty("Error", "ERROR: All parameters need to be an Integer");
            }
        } else {
            js.addProperty("Error", "Missing argument for the generate properties function");
        }

        return js;
    }
}