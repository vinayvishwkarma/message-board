package com.root.messageboard.rest.service;

import java.util.List;

import com.root.messageboard.core.dto.MessageRequestDTO;
import com.root.messageboard.core.dto.MessageV1DTO;
import com.root.messageboard.core.dto.MessageV2DTO;

public interface MessageService {

    void createMessage(final MessageRequestDTO message);

    List<MessageV1DTO> listMessagesV1();

    List<MessageV2DTO> listMessagesV2();
}
