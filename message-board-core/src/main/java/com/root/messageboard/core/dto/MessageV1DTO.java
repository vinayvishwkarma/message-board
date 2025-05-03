package com.root.messageboard.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Data Transfer Object for ListMessages v1 response.
 * <p>
 * Only exposes {@code title}, {@code content}, and {@code sender} fields.
 * Designed for clients that do not require the {@code url}.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageV1DTO {

    private String title;

    private String content;

    private String sender;

    public MessageV1DTO() {}

    public MessageV1DTO(final String title, final String content, final String sender) {
        this.title = title;
        this.content = content;
        this.sender = sender;
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
}
