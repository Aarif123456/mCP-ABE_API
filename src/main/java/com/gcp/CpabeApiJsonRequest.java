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

import static com.gcp.MapLoader.getLoadMap;
import static com.junwei.cpabe.Cpabe.*;

public class CpabeApiJsonRequest extends CpabeQueries {
    private JsonObject body;

    public void respondQuery(HttpRequest request, PrintWriter writer) throws IOException {
        Gson gson = new Gson();
        body = gson.fromJson(request.getReader(), JsonObject.class);
        var method = "";
        if (!body.has("method")) return;
        method = body.get("method").getAsString();
        runQueries(request, writer, method, gson);
    }

    public JsonObject setupQuery(HttpRequest _request, Gson gson) {
        var loadMap = body.has("properties") ? getLoadMap(body.get("properties").getAsString(), gson) : defaultMap;
        return setup(loadMap);
    }

    public JsonObject keygenQuery(HttpRequest _request, Gson gson) {
        JsonObject js = new JsonObject();
        var loadMap = body.has("properties") ? getLoadMap(body.get("properties").getAsString(), gson) : defaultMap;
        if (body.has("publicKey") && body.has("masterKey") && body.has("userAttributes")) {
            var publicKey = body.get("publicKey").getAsString();
            var masterKey = body.get("masterKey").getAsString();
            var userAttributes = body.get("userAttributes").getAsString();
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

    public JsonObject encryptQuery(HttpRequest _request, Gson gson) {
        JsonObject js = new JsonObject();
        var loadMap = body.has("properties") ? getLoadMap(body.get("properties").getAsString(), gson) : defaultMap;
        if (body.has("publicKey") && body.has("policy") && body.has("inputFile")) {
            var publicKey = body.get("publicKey").getAsString();
            var policy = body.get("policy").getAsString();
            var inputFile = body.get("inputFile").getAsString();
            try {
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

    public JsonObject decryptQuery(HttpRequest _request, Gson gson) {
        JsonObject js = new JsonObject();
        var loadMap = body.has("properties") ? getLoadMap(body.get("properties").getAsString(), gson) : defaultMap;
        if (body.has("publicKey") && body.has("privateKey") &&
                body.has("encryptedFile")) {
            var publicKey = body.get("publicKey").getAsString();
            var privateKey = body.get("privateKey").getAsString();
            var encryptedFile = body.get("encryptedFile").getAsString();
            try {
                js = decrypt(publicKey, privateKey, encryptedFile, loadMap);
            } catch (IllegalBlockSizeException | NoSuchAlgorithmException | IOException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | AttributesNotSatisfiedException e) {
                js.addProperty("Error", e.getMessage());
            }
        } else {
            js.addProperty("Error", "Missing argument for the decrypt function");
        }
        return js;
    }

    public JsonObject generateCurveQuery(HttpRequest _request, Gson gson) {
        JsonObject js = new JsonObject();
        Map<String, String> parameterMap = body.has("parameterMap") ?
                getLoadMap(body.get("parameterMap").getAsString(), gson) : new HashMap<>();
        if (body.has("type")) {
            var type = body.get("type").getAsString();
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