package io.heapdog.core.shared;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConstraintToFieldMapper {
    private static final Map<String, String> MAP = Map.of(
            "uc_organization_slug", "slug",
            "uc_heapdog_user_username", "username",
            "uc_heapdog_user_email", "email"

    );
    public String map(String constraintName) {
        return MAP.getOrDefault(constraintName, "unknown_field");
    }
}
