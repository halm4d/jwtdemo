package com.davidhalma.jwtdemo.onboarding.converter;

public interface BaseConverter<T, K> {
    T to(K k);
    K from(T t);
}
