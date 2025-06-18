package com.banking.service;

public interface IdempotencyService {
    boolean isDuplicate(String key);
    void save(String key);
}
