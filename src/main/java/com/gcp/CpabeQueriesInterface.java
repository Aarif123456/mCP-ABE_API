package com.gcp;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;

public interface CpabeQueriesInterface {
    void respondQuery(HttpRequest request, PrintWriter writer) throws IOException;

    JsonObject setupQuery(HttpRequest request, Gson gson);

    JsonObject keygenQuery(HttpRequest request, Gson gson);

    JsonObject encryptQuery(HttpRequest request, Gson gson);

    JsonObject decryptQuery(HttpRequest request, Gson gson);

    JsonObject generateCurveQuery(HttpRequest request, Gson gson);


}