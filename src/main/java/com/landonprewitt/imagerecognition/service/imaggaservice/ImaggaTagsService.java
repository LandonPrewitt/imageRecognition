package com.landonprewitt.imagerecognition.service.imaggaservice;

import com.landonprewitt.imagerecognition.config.ImaggaConfig;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
public class ImaggaTagsService {

    private final RestTemplate restTemplate;
    private final ImaggaConfig imaggaConfig;

    private static final String ENDPOINT = "/tags";

    public ImaggaTagsService(RestTemplateBuilder restTemplateBuilder, ImaggaConfig imaggaConfig) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(imaggaConfig.getUser(), imaggaConfig.getPassword())
                .build();
        this.imaggaConfig = imaggaConfig;
    }

    public List<String> getTagsByURL(String imageData) {
            return parseTagsResponse(sendGETRequestUsingImageURL(imageData));
    }

    public List<String> getTagsByFile(MultipartFile fileToUpload) throws IOException {
        return parseTagsResponse(sendPOSTRequestUsingImageData(fileToUpload));
    }

    public List<String> parseTagsResponse(String response) {
        List<String> tags = new ArrayList<>();
        JSONObject result = new JSONObject(response).getJSONObject("result");
        JSONArray jsonArray = result.getJSONArray("tags");
        for (int i=0; i<jsonArray.length(); i++) {
            tags.add(jsonArray.getJSONObject(i).getJSONObject("tag").getString("en").trim());
        }
        return tags;
    }

    public String sendGETRequestUsingImageURL(String imageURL) {
        try {
            String thresholdParam = "threshold=" + imaggaConfig.getThreshold();
            String imageUrlParam = "image_url=" + imageURL;
            String url = imaggaConfig.getBaseUrl() + ENDPOINT + "?" + thresholdParam + "&" + imageUrlParam;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            log.info(String.format("Executing ImaggaService Request: %n"
                    + "-TYPE: Get %n"
                    + "-URL: %s%n"
                    + "-HEADERS: %s%n" , url, headers));
            return restTemplate.getForObject(url, String.class, entity);
        } catch (HttpStatusCodeException e) {
            HttpHeaders headers = e.getResponseHeaders();
            return String.format("HttpResponse Exception: %n"
                            + "-Status Text: %s%n"
                            + "-Body: %s%n"
                            + "-Content-Type: %s%n"
                            + "-Server: %s",
                    e.getStatusText(), e.getResponseBodyAsString(), headers.get("Content-Type"), headers.get("Server"));
        }
    }

    public String sendPOSTRequestUsingImageData(MultipartFile fileToUpload) throws IOException {

        String credentialsToEncode = imaggaConfig.getUser() + ":" + imaggaConfig.getPassword();
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "Image Upload";
        String thresholdParam = "threshold=" + imaggaConfig.getThreshold();

        URL urlObject = new URL("https://api.imagga.com/v2" + ENDPOINT + "?" + thresholdParam);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);

        log.info(String.format("Executing ImaggaService Request: %n"
                + "-TYPE: Post %n"
                + "-URL: %s%n"
                + "-HEADERS: %s%n" , urlObject, connection.getRequestProperties()));

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        String requestBody = "";

        requestBody = requestBody + (twoHyphens + boundary + crlf);
        requestBody = requestBody + ("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
        requestBody = requestBody + (crlf);

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName() + "\"" + crlf);
        request.writeBytes(crlf);

//        InputStream inputStream = new FileInputStream(fileToUpload);
        InputStream inputStream = fileToUpload.getInputStream();
        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            request.write(dataBuffer, 0, bytesRead);
            requestBody = requestBody + (dataBuffer.toString());
        }

        requestBody.concat(crlf);
        requestBody.concat(twoHyphens + boundary + twoHyphens + crlf);

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

        request.flush();
        request.close();

        InputStream responseStream = new BufferedInputStream(connection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        String line = "";
        StringBuilder stringBuilder = new StringBuilder();

        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();

        String response = stringBuilder.toString();

        responseStream.close();
        connection.disconnect();

        return response;

    }

    //    public String sendPOSTRequestUsingImageData(byte[] imageData, Double threshold) {
//
//        try {
//            String crlf = "\r\n";
//            String twoHyphens = "--";
//            String boundary =  "Image Upload";
//            String requestBody = "";
//
//            requestBody = requestBody + (twoHyphens + boundary + crlf);
//            requestBody = requestBody + ("Content-Disposition: form-data; name=\"image\";" + "\"" + crlf);
//            requestBody = requestBody + (crlf);
//            requestBody = requestBody + (imageData);
//
//            String thresholdParam = "threshold=" + threshold;
//            String url = imaggaConfig.getBaseUrl() + ENDPOINT + "?" + thresholdParam ;
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Type", "multipart/form-data;boundary=" + boundary);
//            headers.setConnection("Keep-Alive");
//            headers.setCacheControl("no-cache");
//
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
//            log.info(String.format("Executing ImaggaService Request: %n"
//                    + "-TYPE: Get %n"
//                    + "-URL: %s%n"
//                    + "-HEADERS: %s%n" , url, headers));
//            return restTemplate.postForObject(url,entity,String.class);
//        } catch (HttpStatusCodeException e) {
//            HttpHeaders headers = e.getResponseHeaders();
//            return String.format("HttpResponse Exception: %n"
//                            + "-Status Text: %s%n"
//                            + "-Body: %s%n"
//                            + "-Content-Type: %s%n"
//                            + "-Server: %s",
//                    e.getStatusText(), e.getResponseBodyAsString(), headers.get("Content-Type"), headers.get("Server"));
//        }
//    }

}
