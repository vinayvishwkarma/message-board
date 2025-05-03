package com.root.messageboard.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.root.messageboard.rest.data.Version;

@Component
public class MessageVersionDispatcher {

    private final List<MessageVersionService<?>> versionServices;

    @Autowired
    public MessageVersionDispatcher(final List<MessageVersionService<?>> versionServices) {
        this.versionServices = versionServices;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getMessagesForVersion(final Version version) {
        return versionServices.stream()
                .filter(service -> service.supports(version))
                .findFirst()
                .map(service -> (List<T>) service.listMessages())
                .orElseThrow(() -> new IllegalArgumentException("Unsupported version: " + version));
    }
}
