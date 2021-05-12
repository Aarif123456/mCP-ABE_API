package com.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.logging.Logger;


public class CpabeAPI implements HttpFunction {

    private static final Logger logger = Logger.getLogger(CpabeAPI.class.getName());

    /* Responds to an HTTP request using data from the request body parsed according to the
       "content-type" header.*/
    @Override
    public void service(HttpRequest request, HttpResponse response)
            throws IOException {

        if (!"POST".equals(request.getMethod())) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_METHOD);
            return;
        }

        // Default values avoid null issues (with switch/case) and exceptions from get() (optionals)
        String contentType = request.getContentType().orElse("");
        var writer = new PrintWriter(response.getWriter());
        CpabeQueries queryMethod;
        if(contentType.startsWith("application/json"))
            queryMethod = new CpabeApiJsonRequest();
        else if(contentType.startsWith("application/x-www-form-urlencoded"))
            queryMethod = new CpabeAPIRequestMethod();
        else if(contentType.startsWith("multipart/form-data"))
            queryMethod = new CpabeAPIFileRequestMethod();
        else{
            logger.info(String.format("Content Type: %s", contentType));
            // Invalid or missing "Content-Type" header
            response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
            return;
        }
                
        queryMethod.respondQuery(request, writer);
    }

}