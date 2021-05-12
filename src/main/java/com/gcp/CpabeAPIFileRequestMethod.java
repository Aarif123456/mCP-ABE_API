package com.gcp;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.MalformedPolicyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.gcp.MapLoader.getLoadMap;
import static com.junwei.cpabe.Cpabe.*;

public class CpabeAPIFileRequestMethod extends CpabeAPIRequestMethod {
    public final List<Path> filePaths = new ArrayList<>();
    public final Logger logger = Logger.getLogger(CpabeAPI.class.getName());

    public static String readFileBytes(Path filePath) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(filePath));
    }

    @Override
    public void respondQuery(HttpRequest request, PrintWriter writer) throws IOException {
        processFiles(request);
        super.respondQuery(request, writer);
        for (var filePath : filePaths) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                logger.info(String.format("Failed to delete file at %s", filePath));
            }
        }
    }

    public void processFiles(HttpRequest request) {
        String tempDirectory = System.getProperty("java.io.tmpdir");
        for (HttpRequest.HttpPart httpPart : request.getParts().values()) {
            String filename = httpPart.getFileName().orElse(null);
            if (filename == null) continue;
            logger.info(String.format("Processed file: %s", filename));
            /* NOTE: GCF's temp directory is an in-memory file system
             *      Thus, any files in it must fit in the instance's memory.
             */
            Path filePath = Paths.get(tempDirectory, filename).toAbsolutePath();

            /* NOTE: files saved to a GCF instance itself may not persist across executions.
             *      Persistent files should be stored elsewhere, e.g. a Cloud Storage bucket.
             */

            try {
                Files.copy(httpPart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(String.format("Failed to create an input stream for %s", filename));
            }
            filePaths.add(filePath);
        }
    }

    public JsonObject encryptQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> policyParam = request.getFirstQueryParameter("policy");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (filePaths.isEmpty()) js.addProperty("Error", "You need to upload a file to encrypt");
        for (var filePath : filePaths) {
            if (publicKeyParam.isPresent() && policyParam.isPresent()) {
                try {
                    var inputFile = readFileBytes(filePath);
                    var publicKey = publicKeyParam.get();
                    var policy = policyParam.get();
                    var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
                    js = encrypt(publicKey, policy, inputFile, loadMap);
                } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | MalformedPolicyException e) {
                    e.printStackTrace();
                    js.addProperty("Error", e.getMessage());
                }
            } else {
                js.addProperty("Error", "Missing argument for encrypt function");
            }
        }
        return js;
    }

    public JsonObject decryptQuery(HttpRequest request, Gson gson) {
        JsonObject js = new JsonObject();
        Optional<String> publicKeyParam = request.getFirstQueryParameter("publicKey");
        Optional<String> privateKeyParam = request.getFirstQueryParameter("privateKey");
        Optional<String> properties = request.getFirstQueryParameter("properties");
        if (filePaths.isEmpty()) js.addProperty("Error", "You need to upload a file to decrypt");
        for (var filePath : filePaths) {
            if (publicKeyParam.isPresent() && privateKeyParam.isPresent()) {
                try {
                    var publicKey = publicKeyParam.get();
                    var privateKey = privateKeyParam.get();
                    var encryptedFile = readFileBytes(filePath);
                    var loadMap = properties.isPresent() ? getLoadMap(properties.get(), gson) : defaultMap;
                    js = decrypt(publicKey, privateKey, encryptedFile, loadMap);
                } catch (IllegalBlockSizeException | NoSuchAlgorithmException | IOException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | AttributesNotSatisfiedException e) {
                    js.addProperty("Error", e.getMessage());
                }
            } else {
                js.addProperty("Error", "Missing argument for the decrypt function");
            }
        }
        return js;
    }

}