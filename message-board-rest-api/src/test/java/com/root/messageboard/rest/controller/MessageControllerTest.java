package com.root.messageboard.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.root.messageboard.core.dto.MessageRequestDTO;
import com.root.messageboard.core.dto.MessageV1DTO;
import com.root.messageboard.core.dto.MessageV2DTO;
import com.root.messageboard.rest.data.Version;
import com.root.messageboard.rest.service.MessageService;
import com.root.messageboard.rest.service.MessageVersionDispatcher;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageVersionDispatcher messageVersionDispatcher;

    @MockBean
    private MessageService messageService;

    @Test
    void whenPostValidMessage_thenReturns201() throws Exception {
        MessageRequestDTO request = new MessageRequestDTO("Title", "Content", "Bob", "https://example.com");
        mockMvc.perform(post("/messages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(messageService).createMessage(request);
    }

    @Test
    void whenPostInvalidMessage_thenReturns400AndNoServiceCall() throws Exception {
        var invalid = new MessageRequestDTO("", "Content", "Bob", "not-a-url");
        mockMvc.perform(post("/messages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is mandatory"))
                .andExpect(jsonPath("$.url").value("Invalid URL format"));
        verify(messageService, Mockito.never()).createMessage(any());
    }

    @Test
    void whenGetMessagesWithVersion1_thenReturnsListOfV1DTO() throws Exception {
        List<MessageV1DTO> messagesV1 = List.of(
                new MessageV1DTO("Title1", "Content1", "Alice"),
                new MessageV1DTO("Title2", "Content2", "Bob"));
        mockVersion(Version.V1, messagesV1);
        mockMvc.perform(get("/messages/list")
                .param("version", Version.V1.name())
                .param("format", "json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].sender").value("Bob"));
    }

    @Test
    void whenGetMessagesWithVersion2_thenReturnsListOfV2DTO() throws Exception {
        List<MessageV2DTO> messagesV2 = List.of(
                new MessageV2DTO("Title1", "Content1", "Alice", "https://example.com"),
                new MessageV2DTO("Title2", "Content2", "Bob", "https://bob.com"));
        mockVersion(Version.V2, messagesV2);
        mockMvc.perform(get("/messages/list")
                .param("version", Version.V2.name())
                .param("format", "json") // Explicitly passing format param as 'json'
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].url").value("https://example.com"))
                .andExpect(jsonPath("$[1].sender").value("Bob"));
    }

    @SuppressWarnings("unchecked")
    private void mockVersion(Version version, List<?> messages) {
        when(messageVersionDispatcher.getMessagesForVersion(version))
                .thenReturn((List<Object>) messages);
    }

    @Test
    void whenPostInvalidMessage_thenReturns400WithErrors() throws Exception {
        MessageRequestDTO invalidMessage = new MessageRequestDTO("", "Valid Content", "Bob", "https://example.com");
        mockMvc.perform(post("/messages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMessage)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is mandatory"));
    }

    @Test
    void whenPostInvalidMessageWithLongTitle_thenReturns400WithErrors() throws Exception {
        MessageRequestDTO invalidMessage = new MessageRequestDTO(
                "A".repeat(256),
                "Valid Content",
                "Bob",
                "https://example.com");
        mockMvc.perform(post("/messages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMessage)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be more than 255 characters"));
    }

    @Test
    void whenPostInvalidMessageWithInvalidUrl_thenReturns400WithErrors() throws Exception {
        MessageRequestDTO invalidMessage = new MessageRequestDTO("Title", "Valid Content", "Bob", "invalid-url");
        mockMvc.perform(post("/messages/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMessage)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.url").value("Invalid URL format"));
    }

    @Test
    void whenUnsupportedVersion_thenReturnsBadRequest() throws Exception {
        String unsupportedVersion = "v3";
        mockMvc.perform(get("/messages/list")
                .param("version", "v3")
                .param("format", "json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid value for parameter 'version': " + unsupportedVersion));
    }
}
