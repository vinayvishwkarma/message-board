package com.root.messageboard.core.wrapper;

import java.util.List;

import com.root.messageboard.core.dto.MessageV2DTO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "messages")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageV2ListWrapper {

    @XmlElement(name = "message")
    private List<MessageV2DTO> messages;

    public MessageV2ListWrapper() {
        // Default constructor for JAXB
    }

    public MessageV2ListWrapper(final List<MessageV2DTO> messages) {
        this.messages = messages;
    }

    public List<MessageV2DTO> getMessages() {
        return messages;
    }

    public void setMessages(final List<MessageV2DTO> messages) {
        this.messages = messages;
    }
}
