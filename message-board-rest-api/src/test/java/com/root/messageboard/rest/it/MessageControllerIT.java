package com.root.messageboard.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.root.messageboard.core.dto.MessageRequestDTO;
import com.root.messageboard.core.dto.MessageV1DTO;
import com.root.messageboard.core.dto.MessageV2DTO;
import com.root.messageboard.core.wrapper.MessageV2ListWrapper;
import com.root.messageboard.rest.data.Version;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerIT {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetMessagesWithVersion1_thenReturnsListOfV1DTO() {
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/messages/list")
                .queryParam("version", Version.V1.name())
                .queryParam("format", "json")
                .toUriString();
        ResponseEntity<List<MessageV1DTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MessageV1DTO>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<MessageV1DTO> messages = response.getBody();
        assertEquals(3, messages.size());
        assertEquals("Message 1", messages.get(0).getTitle());
        assertEquals("Bob", messages.get(1).getSender());
    }

    @Test
    void whenGetMessagesWithVersion2_thenReturnsListOfV2DTO() {
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/messages/list")
                .queryParam("version", Version.V2.name())
                .queryParam("format", "json")
                .toUriString();
        ResponseEntity<List<MessageV2DTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MessageV2DTO>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<MessageV2DTO> messages = response.getBody();
        assertEquals(3, messages.size());
        assertEquals("http://example1.com", messages.get(0).getUrl());
        assertEquals("Bob", messages.get(1).getSender());
    }

    @Test
    void whenPostValidMessage_thenReturns201() {
        MessageRequestDTO request = new MessageRequestDTO("Title", "Content", "Bob", "https://example.com");
        HttpEntity<MessageRequestDTO> entity = new HttpEntity<>(request);
        ResponseEntity<Void> response = restTemplate.exchange("/messages/create", HttpMethod.POST, entity, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void whenPostInvalidMessage_thenReturns400() {
        MessageRequestDTO invalidRequest = new MessageRequestDTO("", "Content", "Bob", "not-a-url");
        HttpEntity<MessageRequestDTO> entity = new HttpEntity<>(invalidRequest);
        ResponseEntity<String> response = restTemplate.exchange("/messages/create", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assert response.getBody().contains("Title is mandatory");
        assert response.getBody().contains("Invalid URL format");
    }

    @Test
    void whenGetMessagesWithVersion1AndFormatXml_thenReturnsListOfV2DTO() {
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/messages/list")
                .queryParam("version", Version.V2.name())
                .queryParam("format", "xml")
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<MessageV2ListWrapper> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<MessageV2DTO> messages = response.getBody().getMessages();
        assertEquals(2, messages.size());
        assertEquals("Message 1", messages.get(0).getTitle());
        assertEquals("Bob", messages.get(1).getSender());
    }

    @Test
    void whenUnsupportedVersion_thenReturnsBadRequest() {
        String invalidVersion = "V3";
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/messages/list")
                .queryParam("version", invalidVersion)
                .queryParam("format", "json")
                .toUriString();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assert response.getBody().contains("Invalid value for parameter 'version': " + invalidVersion);
    }
}
