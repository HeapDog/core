package io.heapdog.core.shared;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiResponse<T> {
    private Instant timestamp;
    private T data;
    private String path;
}
