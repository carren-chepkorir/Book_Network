package com.example.BookNetwork.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericResponse<T> {
    private String message;
    private ResponseStatusEnum status;
    private T _embedded;
}
