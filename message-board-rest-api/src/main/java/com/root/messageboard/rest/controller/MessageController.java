package com.root.messageboard.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.root.messageboard.core.dto.MessageRequestDTO;
import com.root.messageboard.core.dto.MessageV2DTO;
import com.root.messageboard.core.wrapper.MessageV2ListWrapper;
import com.root.messageboard.rest.data.Version;
import com.root.messageboard.rest.service.MessageService;
import com.root.messageboard.rest.service.MessageVersionDispatcher;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/messages")
@Tag(name = "Messages", description = "Operations related to messages")
public class MessageController {

    private final MessageService service;

    private final MessageVersionDispatcher dispatcher;

    @Autowired
    public MessageController(final MessageService service, MessageVersionDispatcher dispatcher) {
        this.service = service;
        this.dispatcher = dispatcher;
    }

    @Operation(summary = "Create a new message")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Message created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Valid @RequestBody MessageRequestDTO request) {
        service.createMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get messages by version", description = "Returns message list in specified version (V1 or V2) and format (JSON or XML)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of messages"),
        @ApiResponse(responseCode = "400", description = "Invalid version or format")
    })
    @GetMapping(value = "/list", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> list(
            @RequestParam(name = "version") Version version,
            @RequestParam(name = "format", required = false, defaultValue = "json") String format) {
        List<?> messages = dispatcher.getMessagesForVersion(version);
        if (format != null && "xml".equalsIgnoreCase(format)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(new MessageV2ListWrapper((List<MessageV2DTO>) messages));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(messages);
    }
}
