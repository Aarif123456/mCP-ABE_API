package com.gcp;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.MalformedAttributesException;
import com.mitu.utils.exceptions.MalformedPolicyException;

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
import static com.junwei.cpabe.Cpabe.*;

public class CpabeAPIRequestMethod extends CpabeQueries {
    public void respondQuery(HttpRequest request, PrintWriter writer) throws IOException {
        var method = "";
        Optional<String> methodParam = request.getFirstQueryParameter("method");
        if (methodParam.isEmpty()) return;
        method = methodParam.get();
        runQueries(request, writer, method);
    }

    public JsonObject setupQuery(HttpRequest request, Gson gson) {
        Optional<String> properties = request.getFirstQueryParameter("properties");
        var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
        return setup(loadMap);
    }

    public JsonObject keygenQuery(HttpRequest request, Gson gson) {
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
            try {
                js = keygen(publicKey, masterKey, userAttributes, loadMap);
            } catch (NoSuchAlgorithmException | MalformedAttributesException e) {
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for the keygen function");
        }
        return js;
    }

    public JsonObject encryptQuery(HttpRequest request, Gson gson) {
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
            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | MalformedPolicyException e) {
                e.printStackTrace();
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for encrypt function");
        }
        return js;
    }

    public JsonObject decryptQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> privateKeyParam = request.getFirstQueryParameter("privateKey");
        Optional<String> encryptedFileParam = request.getFirstQueryParameter("encryptedFile");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (publicKeyParam.isPresent() && privateKeyParam.isPresent() &&
                encryptedFileParam.isPresent()) {
            var publicKey = publicKeyParam.get();
            var privateKey = privateKeyParam.get();
            var encryptedFile = encryptedFileParam.get();
            try {
                var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
                js = decrypt(publicKey, privateKey, encryptedFile, loadMap);
            } catch (IllegalBlockSizeException | NoSuchAlgorithmException | IOException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | AttributesNotSatisfiedException e) {
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for the decrypt function");
        }
        return js;
    }

    public JsonObject generateCurveQuery(HttpRequest request, Gson gson) {
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