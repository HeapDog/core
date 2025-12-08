package io.heapdog.core.shared;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = DataMaskingSerializer.class)
public @interface MaskData {
    // Number of characters to keep visible on the left side
    int keepLeft() default 0;

    // Number of characters to keep visible on the right side
    int keepRight() default 0;

    // The character used to mask the data
    String maskChar() default "*";
}
