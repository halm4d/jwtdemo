package com.davidhalma.jwtdemo.user.converter;

public interface BaseConverter<T, K> {
    T to(K k);
    K from(T t);
}
