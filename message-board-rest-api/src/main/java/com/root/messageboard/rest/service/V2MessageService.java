package com.root.messageboard.rest.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.root.messageboard.core.dto.MessageV2DTO;
import com.root.messageboard.rest.data.Version;

@Component
public class V2MessageService implements MessageVersionService<MessageV2DTO> {

    private final MessageService messageService;

    public V2MessageService(final MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public boolean supports(final Version version) {
        return version == Version.V2;
    }

    @Override
    public List<MessageV2DTO> listMessages() {
        return messageService.listMessagesV2();
    }
}
