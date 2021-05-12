package com.gcp;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Writer;

public abstract class CpabeQueries implements CpabeQueriesInterface {
    public void runQueries(HttpRequest request, Writer writer, String method) throws IOException {
        Gson gson = new Gson();
        runQueries(request, writer, method, gson);
    }

    public void runQueries(HttpRequest request, Writer writer, String method, Gson gson) throws IOException {
        var js = new JsonObject();
        switch (method) {
            case "setup":
                js = setupQuery(request, gson);
                break;
            case "keygen": {
                js = keygenQuery(request, gson);
                break;
            }
            case "encrypt": {
                js = encryptQuery(request, gson);
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
                js.addProperty("Error", "Method type not recognized of type\"" + method + "\"");
        }
        writer.write(gson.toJson(js));
    }

}