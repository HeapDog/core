package io.heapdog.core.shared;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> contents;
    private PageMeta meta;
}
