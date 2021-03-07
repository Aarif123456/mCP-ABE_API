
package com.gcp;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.mitu.cpabe.Cpabe.*;

public class CpabeAPI implements HttpFunction {

    private static final Gson gson = new Gson();

    /* Responds to an HTTP request using data from the request body parsed according to the
       "content-type" header.*/
    @Override
    public void service(HttpRequest request, HttpResponse response)
            throws IOException {

        // Default values avoid null issues (with switch/case) and exceptions from get() (optionals)
        String contentType = request.getContentType().orElse("");
        var writer = new PrintWriter(response.getWriter());
        String method;
        switch (contentType) {
            case "application/json":
                JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);
                method = respondMethod(body, writer);
                break;
            case "application/x-www-form-urlencoded":
                method = respondQuery(request, writer);
                break;
            default:
                // Invalid or missing "Content-Type" header
                response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
                return;
        }

        // Verify that a method was provided
        if (method == null) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        }

    }

    private String respondQuery(HttpRequest request, PrintWriter writer) {
        var method = "";
        Gson gson =new Gson();
        JsonObject js = new JsonObject();
        Optional<String> methodParam = request.getFirstQueryParameter("method");
        if (methodParam.isPresent()) {
            method = methodParam.get();
            switch(method){
                case "setup":
                    Optional<String> attributeUniverseParam = request.getFirstQueryParameter("attributeUniverse");
                    if(attributeUniverseParam.isPresent()){
                        String[] attributeUniverse = gson.fromJson(attributeUniverseParam.get(), String[].class);
                        js = setup(attributeUniverse);
                    } else {
                        js.addProperty("Error", "Missing argument for the setup function");
                    }
                    break;

                case "keygen":{
                    Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
                    Optional<String> masterKeyParam = request.getFirstQueryParameter("masterKey");
                    Optional<String> userAttributesParam = request.getFirstQueryParameter("userAttributes");
                    if(publicKeyParam.isPresent() && masterKeyParam.isPresent() && userAttributesParam.isPresent() ){
                        var publicKey = publicKeyParam.get();
                        var masterKey = masterKeyParam.get();
                        var userAttributes = userAttributesParam.get();
                        js = keygen(publicKey, masterKey, userAttributes);
                    }
                    else{
                        js.addProperty("Error", "Missing argument for the keygen function");
                    }
                    break;
                }
                case "encrypt": {
                    Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
                    Optional<String> policyParam = request.getFirstQueryParameter("policy");
                    Optional<String> inputFileParam = request.getFirstQueryParameter("inputFile");
                    if(publicKeyParam.isPresent() && policyParam.isPresent() && inputFileParam.isPresent() ) {
                        var publicKey = publicKeyParam.get();
                        var policy = policyParam.get();
                        var inputFile = inputFileParam.get();
                        try {
                            js = encrypt(publicKey, policy, inputFile);
                        } catch (IOException |
                                NoSuchPaddingException |
                                NoSuchAlgorithmException |
                                InvalidKeyException |
                                IllegalBlockSizeException |
                                BadPaddingException e) {
                            e.printStackTrace();
                            js.addProperty("Error", e.getMessage());
                        }
                    } else{
                        js.addProperty("Error", "Missing argument for encrypt function");
                    }
                    break;
                }
                case "halfDecrypt":{
                    Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
                    Optional<String> share1Param = request.getFirstQueryParameter("share1");
                    Optional<String> encryptedFileParam = request.getFirstQueryParameter("encryptedFile");
                    Optional<String> professionalIdParam = request.getFirstQueryParameter("professionalId");
                    if(publicKeyParam.isPresent() && share1Param.isPresent() &&
                            encryptedFileParam.isPresent()  && professionalIdParam.isPresent()) {
                        var publicKey = publicKeyParam.get();
                        var share1 = share1Param.get();
                        var encryptedFile = encryptedFileParam.get();
                        var professionalId = professionalIdParam.get();
                        try {
                            js = halfDecrypt(publicKey, share1, encryptedFile, professionalId);
                        } catch (IOException |
                                AttributesNotSatisfiedException |
                                NoSuchDecryptionTokenFoundException e) {
                            e.printStackTrace();
                            js.addProperty("Error", e.getMessage());
                        }
                    } else{
                        js.addProperty("Error", "Missing argument for the half-decrypt function");
                    }
                    break;
                }
                case "decrypt": {
                    Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
                    Optional<String> share2Param = request.getFirstQueryParameter("share2");
                    Optional<String> encryptedFileParam = request.getFirstQueryParameter("encryptedFile");
                    Optional<String> mDecryptedFileParam = request.getFirstQueryParameter("mDecryptedFile");
                    Optional<String> professionalIdParam = request.getFirstQueryParameter("professionalId");
                    if(publicKeyParam.isPresent() && share2Param.isPresent() &&
                            encryptedFileParam.isPresent()  && mDecryptedFileParam.isPresent() &&
                            professionalIdParam.isPresent()) {
                        var publicKey = publicKeyParam.get();
                        var share2 = share2Param.get();
                        var encryptedFile = encryptedFileParam.get();
                        var mDecryptedFile = mDecryptedFileParam.get();
                        var professionalId = professionalIdParam.get();
                        try {
                            js = decrypt(publicKey, share2, encryptedFile, mDecryptedFile, professionalId);
                        } catch (IllegalBlockSizeException |
                                NoSuchAlgorithmException |
                                IOException |
                                BadPaddingException |
                                NoSuchPaddingException |
                                InvalidKeyException e) {
                            js.addProperty("Error", e.getMessage());
                        }
                    } else{
                        js.addProperty("Error", "Missing argument for the decrypt function");
                    }
                    break;
                }
                default:
                    js.addProperty("Error", "Method type not recognized of type\""+method+"\"");
            }
        }
        writer.write(gson.toJson(js));
        return method;
    }

    private String respondMethod(JsonObject body, PrintWriter writer) {
        var method = "";
        Gson gson =new Gson();
        JsonObject js = new JsonObject();
        if (body.has("method")) {
            method = body.get("method").getAsString();
            switch(method) {
                case "setup":
                    if (body.has("attributeUniverse")) {
                        String[] attributeUniverse = gson.fromJson(body.get("attributeUniverse"), String[].class);
                        js = setup(attributeUniverse);
                    } else {
                        js.addProperty("Error", "Missing argument for the setup function");
                    }
                    break;

                case "keygen": {
                    if (body.has("publicKey") && body.has("masterKey") && body.has("userAttributes")) {
                        var publicKey = body.get("publicKey").getAsString();
                        var masterKey = body.get("masterKey").getAsString();
                        var userAttributes = body.get("userAttributes").getAsString();
                        js = keygen(publicKey, masterKey, userAttributes);
                    } else {
                        js.addProperty("Error", "Missing argument for the keygen function");
                    }
                    break;
                }
                case "encrypt": {
                    if (body.has("publicKey") && body.has("policy") && body.has("inputFile")) {
                        var publicKey = body.get("publicKey").getAsString();
                        var policy = body.get("policy").getAsString();
                        var inputFile = body.get("inputFile").getAsString();
                        try {
                            js = encrypt(publicKey, policy, inputFile);
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
                    break;
                }
                case "halfDecrypt": {
                    if (body.has("publicKey") && body.has("share1") &&
                            body.has("encryptedFile") && body.has("professionalId")) {
                        var publicKey = body.get("publicKey").getAsString();
                        var share1 = body.get("share1").getAsString();
                        var encryptedFile = body.get("encryptedFile").getAsString();
                        var professionalId = body.get("professionalId").getAsString();
                        try {
                            js = halfDecrypt(publicKey, share1, encryptedFile, professionalId);
                        } catch (IOException |
                                AttributesNotSatisfiedException |
                                NoSuchDecryptionTokenFoundException e) {
                            e.printStackTrace();
                            js.addProperty("Error", e.getMessage());
                        }
                    } else {
                        js.addProperty("Error", "Missing argument for the half-decrypt function");
                    }
                    break;
                }
                case "decrypt": {
                    if (body.has("publicKey") && body.has("share2") &&
                            body.has("encryptedFile") && body.has("mDecryptedFile") &&
                            body.has("professionalId")) {
                        var publicKey = body.get("publicKey").getAsString();
                        var share2 = body.get("share2").getAsString();
                        var encryptedFile = body.get("encryptedFile").getAsString();
                        var mDecryptedFile = body.get("mDecryptedFile").getAsString();
                        var professionalId = body.get("professionalId").getAsString();
                        try {
                            js = decrypt(publicKey, share2, encryptedFile, mDecryptedFile, professionalId);
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
                    break;
                }
                default:
                    js.addProperty("Error", "Method type not recognized of type\""+method+"\"");
            }
        }
        writer.write(gson.toJson(js));
        return method;
    }
}