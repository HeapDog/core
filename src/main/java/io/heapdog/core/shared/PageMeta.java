package io.heapdog.core.shared;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageMeta {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private int numberOfElements;
    private boolean first;
    private boolean last;
}
