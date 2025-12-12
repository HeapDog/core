package io.heapdog.core.feature.serviceuser;

public enum ServiceUserPermission {

    READ_HEAPDOG_USER("read:heapdog_user"),
    WRITE_HEAPDOG_USER("write:heapdog_user"),
    READ_NOTIFICATION("read:notification"),
    READ_SSE_TOKEN("read:sse_token");

    private final String label;
    private final String description = switch (this) {
        case READ_HEAPDOG_USER -> "Permission to read Heapdog user data.";
        case WRITE_HEAPDOG_USER -> "Permission to modify Heapdog user data.";
        case READ_NOTIFICATION -> "Permission to read notifications.";
        case READ_SSE_TOKEN -> "Permission to read SSE tokens.";
    };

    ServiceUserPermission(String description) {
        this.label = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
