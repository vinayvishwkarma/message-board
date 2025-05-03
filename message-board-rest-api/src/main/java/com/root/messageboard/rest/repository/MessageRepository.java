package com.root.messageboard.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.root.messageboard.rest.data.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {}
