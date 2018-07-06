package nl.playground.cloud.config.server.rest.http;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    /**
     * Post an empty body to the clients actuator refresh endpoint
     * @param url
     */
    public static void refreshClient(String url) {
        HttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response = httpClient.execute(request);
            httpClient.close();
            logger.info("refreshed client at {}", url);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
