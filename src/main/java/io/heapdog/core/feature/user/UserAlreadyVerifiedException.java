package io.heapdog.core.feature.user;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String message) {
        super(message);
    }
}
