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
package com.example;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

class ParseContentType implements HttpFunction {

  private static final Gson gson = new Gson();

  /* Responds to an HTTP request using data from the request body parsed according to the
     "content-type" header.*/
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException {
    String name = null;

    // Default values avoid null issues (with switch/case) and exceptions from get() (optionals)
    String contentType = request.getContentType().orElse("");

    switch (contentType) {
      case "application/json":
        // '{"name":"John"}'
        JsonObject body = gson.fromJson(request.getReader(), JsonObject.class);
        if (body.has("name")) {
          name = body.get("name").getAsString();
        }
        break;
      case "text/plain":
        // 'John'
        name = request.getReader().readLine();
        break;
      case "application/x-www-form-urlencoded":
        // 'name=John' in the body of a POST request (not the URL)
        Optional<String> nameParam = request.getFirstQueryParameter("name");
        if (nameParam.isPresent()) {
          name = nameParam.get();
        }
        break;
      default:
        // Invalid or missing "Content-Type" header
        response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
        return;
    }

    // Verify that a name was provided
    if (name == null) {
      response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    // Respond with a name
    var writer = new PrintWriter(response.getWriter());
    writer.printf("Hello %s!", name);
  }
}