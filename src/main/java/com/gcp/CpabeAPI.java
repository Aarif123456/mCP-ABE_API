/*

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedWriter;

public class Example implements HttpFunction {
  @Override
  public void service(HttpRequest request, HttpResponse response) throws Exception {
    BufferedWriter writer = response.getWriter();
    writer.write("Hello world!");
  }
}*/
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
import java.util.ArrayList;
import java.util.List;
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
        String method = null;
        switch (contentType) {
            case "application/json":
                JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);
                method = respondMethod(body, writer);
                break;
            /*case "application/x-www-form-urlencoded":
                // 'name=John' in the body of a POST request (not the URL)
                Optional<String> nameParam = request.getFirstQueryParameter("name");
                if (nameParam.isPresent()) {
                    name = nameParam.get();
                }
                break;*/
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

    private String respondMethod(JsonObject body, PrintWriter writer) {
        var method = "";
        Gson gson =new Gson();
        JsonObject js = new JsonObject();
        if (body.has("method")) {
            method = body.get("name").getAsString();
            switch(method){
                case "setup":
                    List<String> attributeUniverse = new ArrayList<>();
                    var jsonUniverse = body.get("attributeUniverse").getAsJsonArray();
                    for(var attribute: jsonUniverse){
                        attributeUniverse.add(attribute.getAsString());
                    }
                    js = setup(attributeUniverse.toArray(String[]::new));
                    break;

                case "keygen":{
                    var publicKey = body.get("publicKey").getAsString();
                    var masterKey = body.get("masterKey").getAsString();
                    var userAttributes = body.get("userAttributes").getAsString();
                    js = keygen(publicKey, masterKey, userAttributes);
                    break;
                }
                case "encrypt": {
                    var publicKey = body.get("publicKey").getAsString();
                    var policy = body.get("policy").getAsString();
                    var inputFile = body.get("inputFile").getAsString();
                    try{
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
                    break;
                }
                case "halfDecrypt":{
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
                    break;
                }
                case "decrypt": {
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