package com.root.messageboard.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.root.messageboard.core.dto.MessageRequestDTO;
import com.root.messageboard.core.dto.MessageV1DTO;
import com.root.messageboard.core.dto.MessageV2DTO;
import com.root.messageboard.rest.data.Message;
import com.root.messageboard.rest.repository.MessageRepository;

class MessageServiceTest {

    private MessageService service;

    @Mock
    private MessageRepository messageRepository;

    private List<Message> messages;

    @BeforeEach
    void setUp() {
        service = new MessageServiceImpl(messageRepository);
        setupMock();
    }

    private void setupMock() {
        Mockito.when(messageRepository.findAll()).thenAnswer(new Answer<List<Message>>() {

            @Override
            public List<Message> answer(InvocationOnMock invocationOnMock) {
                return null;
            }
        });
    }

    @Test
    void whenNoMessages_thenListIsEmpty() {
        messages = new ArrayList<>();
        List<MessageV1DTO> all = service.listMessagesV1();
        assertThat(all).isEmpty();
    }

    @Test
    void whenOneMessageCreated_thenListContainsIt() {
        MessageRequestDTO msg = new MessageRequestDTO(
                "Title",
                "Content",
                "Bob",
                "https://example.com");
        service.createMessage(msg);
        List<MessageV1DTO> all = service.listMessagesV1();
        assertThat(all)
                .hasSize(1)
                .first()
                .extracting(
                        MessageV1DTO::getTitle,
                        MessageV1DTO::getContent,
                        MessageV1DTO::getSender)
                .isEqualTo(
                        Arrays.asList(msg.getTitle(), msg.getContent(), msg.getSender()));
    }

    @Test
    void whenMultipleMessages_thenListReturnsAllInOrder() {
        MessageRequestDTO a = new MessageRequestDTO("A", "1", "X", "https://a");
        MessageRequestDTO b = new MessageRequestDTO("B", "2", "Y", "https://b");
        service.createMessage(a);
        service.createMessage(b);
        List<MessageV1DTO> all = service.listMessagesV1();
        assertThat(all)
                .extracting(
                        MessageV1DTO::getTitle,
                        MessageV1DTO::getContent,
                        MessageV1DTO::getSender)
                .containsExactly(
                        tuple(a.getTitle(), a.getContent(), a.getSender()),
                        tuple(b.getTitle(), b.getContent(), b.getSender()));
    }

    @Test
    void whenListMessagesV2_thenReturnsCorrectDTOs() {
        MessageRequestDTO msg = new MessageRequestDTO(
                "Title",
                "Content",
                "Bob",
                "https://example.com");
        service.createMessage(msg);
        List<MessageV2DTO> all = service.listMessagesV2();
        assertThat(all)
                .hasSize(1)
                .first()
                .extracting(
                        MessageV2DTO::getTitle,
                        MessageV2DTO::getContent,
                        MessageV2DTO::getSender,
                        MessageV2DTO::getUrl)
                .isEqualTo(
                        Arrays.asList(msg.getTitle(), msg.getContent(), msg.getSender(), msg.getUrl()));
    }
}
