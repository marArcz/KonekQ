package com.example.konekq;

import androidx.annotation.Nullable;

public interface RequestCompleteListener<T> {
    void onComplete(@Nullable T data);
}
