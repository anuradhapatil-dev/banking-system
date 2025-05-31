package com.banking.generator;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {
    public String generate() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
