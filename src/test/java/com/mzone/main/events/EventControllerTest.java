package com.mzone.main.events;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.internal.extensions.MimeBodyPublisherAdapter;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class EventControllerTest {

    @Test
    public void test() throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        final String body = "{\n" +
                "  \"name\": \"event_name_1\",\n" +
                "  \"description\": \"desc must be at leasr 10 char\",\n" +
                "  \"address\": \"address_1\",\n" +
                "  \"categories\": [\n" +
                "    1\n" +
                "  ],\n" +
                "  \"availability\": 0,\n" +
                "  \"location\": {\n" +
                "    \"lat\": 49.9537676,\n" +
                "    \"lng\": 36.3681454\n" +
                "  },\n" +
                "  \"startTime\": \"2001-07-04T12:08:56.235-07:00\",\n" +
                "  \"endTime\": \"2001-07-04T13:08:56.235-07:00\"\n" +
                "}";

        final MultipartBodyPublisher dto = MultipartBodyPublisher.newBuilder()
                .formPart("data", new MimeBodyPublisherAdapter(
                        HttpRequest.BodyPublishers.ofByteArray(body.getBytes()), MediaType.APPLICATION_JSON))
                .build();

        final String boundary = dto.boundary();
        final HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/v1/events/new"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Authorization", "Bearer eyJyZWZyZXNoIjpmYWxzZSwiYWxnIjoiSFMyNTYifQ.eyJzdWIiOiIxMXdhY2sxMUBnbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY1NDAxOTU0NywiZXhwIjoxNjU0MDI2NzQ3fQ.Bewlb7A4BBMKqsv8AUOJO8GnAN8GiEctcnPh4W1R0rw")
                .POST(dto)
                .build();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("code: " + response.statusCode());
        System.out.println("body: " + response.body());
    }

}