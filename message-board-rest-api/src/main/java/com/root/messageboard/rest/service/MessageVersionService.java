package com.root.messageboard.rest.service;

import java.util.List;

import com.root.messageboard.rest.data.Version;

public interface MessageVersionService<T> {

    boolean supports(Version version);

    List<T> listMessages();
}
