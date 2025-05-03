package com.root.messageboard.core.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * General payload for message-related operations.
 * <p>
 * Contains the core inputs shared between different endpoints.
 * Can be used as a base for create, update, or other operations.
 * </p>
 */
public class MessageRequestDTO {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title cannot be more than 255 characters")
    String title;

    @NotBlank(message = "Content is mandatory")
    @Size(max = 1000, message = "Content cannot be more than 1000 characters")
    String content;

    @NotBlank(message = "Sender is mandatory")
    @Size(max = 255, message = "Sender cannot be more than 255 characters")
    String sender;

    @NotBlank(message = "URL is mandatory")
    @URL(message = "Invalid URL format")
    String url;

    public MessageRequestDTO() {}

    public MessageRequestDTO(final String title, final String content, final String sender, final String url) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
