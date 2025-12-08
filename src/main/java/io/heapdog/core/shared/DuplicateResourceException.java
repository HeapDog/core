package io.heapdog.core.shared;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException{
    private final String resourceName;

    public  DuplicateResourceException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
    }

}
