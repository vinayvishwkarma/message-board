package com.root.messageboard.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for ListMessages v2 response.
 * <p>
 * Exposes all four fields: {@code title}, {@code content}, {@code sender}, and {@code url}.
 * Supports both JSON and XML formats depending on client request.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageV2DTO {

    private String title;

    private String content;

    private String sender;

    private String url;

    public MessageV2DTO() {}

    public MessageV2DTO(String title, String content, String sender, String url) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
