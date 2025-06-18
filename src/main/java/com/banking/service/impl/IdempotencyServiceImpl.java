package com.banking.service.impl;

import com.banking.model.IdempotencyKey;
import com.banking.repository.IdempotencyKeyRepository;
import com.banking.service.IdempotencyService;
import org.springframework.stereotype.Service;

@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    public IdempotencyServiceImpl(IdempotencyKeyRepository repository) {
        this.idempotencyKeyRepository = repository;
    }

    @Override
    public boolean isDuplicate(String key) {
        return idempotencyKeyRepository.existsById(key);
    }

    @Override
    public void save(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Idempotency key cannot be null or blank");
        }

        IdempotencyKey idempotencyKey = new IdempotencyKey();
        idempotencyKey.setKey(key);
        idempotencyKeyRepository.save(idempotencyKey);
    }
}
