package com.havefunwith.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
