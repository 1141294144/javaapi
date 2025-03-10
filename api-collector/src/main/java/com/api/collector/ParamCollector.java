//package com.api.collector;
//
//public class ParamCollector {
//}


package com.api.collector;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class ParamCollector {
    private static final Gson gson = new Gson();
    private static final String OUTPUT_FILE = "api-collector.log";

    public static void captureRequest(HttpServletRequest req,
                                      HttpServletResponse resp) {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("timestamp", Instant.now().toString());
        data.put("uri", req.getRequestURI());
        data.put("method", req.getMethod());
        data.put("clientIP", req.getRemoteAddr());

        Map<String, String[]> params = new HashMap<>();
        if ("GET".equalsIgnoreCase(req.getMethod())) {
            params.putAll(req.getParameterMap());
        } else if ("POST".equalsIgnoreCase(req.getMethod())) {
            params.putAll(parsePostParams(req));
        }
        data.put("params", params);

        try (FileWriter fw = new FileWriter(OUTPUT_FILE, true)) {
            fw.write(gson.toJson(data) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String[]> parsePostParams(HttpServletRequest req) {
        String contentType = req.getContentType();
        if (contentType == null) return Collections.emptyMap();

        if (contentType.contains("application/x-www-form-urlencoded")) {
            return req.getParameterMap();
        } else if (contentType.contains("application/json")) {
            try {
                String json = IOUtils.toString(req.getInputStream(),
                        req.getCharacterEncoding());
                return parseJSONParams(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyMap();
    }

    private static Map<String, String[]> parseJSONParams(String json) {
        Map<String, String[]> params = new HashMap<>();
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        obj.entrySet().forEach(e ->
                params.put(e.getKey(), new String[]{e.getValue().getAsString()}));
        return params;
    }
}