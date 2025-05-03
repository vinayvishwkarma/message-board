package com.root.messageboard.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@RestController
public class PingController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/ping")
    @Transactional
    @Operation(summary = "Health check", description = "Checks if the service and DB are reachable")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is up and DB is connected"),
        @ApiResponse(responseCode = "500", description = "DB is unreachable")
    })
    public ResponseEntity<String> ping() {
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return ResponseEntity.ok("Up and running");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Database connection failed: " + ex.getMessage());
        }
    }
}
