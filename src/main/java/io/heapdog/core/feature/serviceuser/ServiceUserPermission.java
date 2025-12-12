package io.heapdog.core.feature.serviceuser;

public enum ServiceUserPermission {

    READ_HEAPDOG_USER("read:heapdog_user"),
    WRITE_HEAPDOG_USER("write:heapdog_user"),
    READ_NOTIFICATION("read:notification");
    READ_NOTIFICATION("read:notification"),
    READ_SSE_TOKEN("read:sse_token");

    private final String permission;

    ServiceUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
