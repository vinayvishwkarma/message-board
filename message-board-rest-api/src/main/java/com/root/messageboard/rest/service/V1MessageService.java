package com.root.messageboard.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.root.messageboard.core.dto.MessageV1DTO;
import com.root.messageboard.rest.data.Version;

@Component
public class V1MessageService implements MessageVersionService<MessageV1DTO> {

    private final MessageService messageService;

    @Autowired
    public V1MessageService(final MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public boolean supports(final Version version) {
        return version == Version.V1;
    }

    @Override
    public List<MessageV1DTO> listMessages() {
        return messageService.listMessagesV1();
    }
}
