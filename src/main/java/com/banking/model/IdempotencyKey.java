package com.banking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "idempotency_keys")
public class IdempotencyKey {

    @Id
    private String key; // UUID or client-generated token

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
