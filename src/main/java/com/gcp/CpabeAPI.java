package com.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import static com.gcp.CpabeAPIRequestMethod.respondQuery;
import static com.gcp.CpabeApiJsonRequest.respondMethod;

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

}