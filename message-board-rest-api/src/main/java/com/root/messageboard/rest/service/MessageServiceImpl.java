package com.root.messageboard.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.root.messageboard.core.dto.MessageRequestDTO;
import com.root.messageboard.core.dto.MessageV1DTO;
import com.root.messageboard.core.dto.MessageV2DTO;
import com.root.messageboard.rest.data.Message;
import com.root.messageboard.rest.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(final MessageRequestDTO messageRequestDTO) {
        Message message = new Message(
                messageRequestDTO.getTitle(),
                messageRequestDTO.getContent(),
                messageRequestDTO.getSender(),
                messageRequestDTO.getUrl());
        messageRepository.save(message);
    }

    @Override
    public List<MessageV1DTO> listMessagesV1() {
        return messageRepository.findAll().stream()
                .map(filteredMessage -> new MessageV1DTO(
                        filteredMessage.getTitle(),
                        filteredMessage.getContent(),
                        filteredMessage.getSender()))
                .toList();
    }

    @Override
    public List<MessageV2DTO> listMessagesV2() {
        List<MessageV2DTO> result = messageRepository.findAll().stream()
                .map(filteredMessage -> new MessageV2DTO(
                        filteredMessage.getTitle(),
                        filteredMessage.getContent(),
                        filteredMessage.getSender(),
                        filteredMessage.getUrl()))
                .toList();
        return result;
    }
}
