package io.heapdog.core.shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class DataMaskingSerializer extends StdSerializer<Object> implements ContextualSerializer {

    private int keepLeft;
    private int keepRight;
    private String maskChar;

    // Default constructor needed for Jackson
    public DataMaskingSerializer() {
        super(Object.class);
    }

    // Constructor to create a new instance with specific configuration
    public DataMaskingSerializer(int keepLeft, int keepRight, String maskChar) {
        super(Object.class);
        this.keepLeft = keepLeft;
        this.keepRight = keepRight;
        this.maskChar = maskChar;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        // This method configures the serializer by reading the annotation values
        if (property != null) {
            MaskData maskData = property.getAnnotation(MaskData.class);
            if (maskData != null) {
                return new DataMaskingSerializer(
                        // Reference the new parameter names: keepLeft() and keepRight()
                        maskData.keepLeft(),
                        maskData.keepRight(),
                        maskData.maskChar()
                );
            }
        }
        return this;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // Convert Int, Long, and String to String for unified masking
        String originalValue = value.toString();
        String maskedValue = maskString(originalValue);

        gen.writeString(maskedValue);
    }

    private String maskString(String input) {
        if (input == null || input.isEmpty()) return input;

        int length = input.length();

        // Safety check: if characters to keep exceed length, mask everything
        if (keepLeft + keepRight >= length) {
            return maskChar.repeat(length);
        }

        StringBuilder sb = new StringBuilder();

        // 1. Append left visible characters
        sb.append(input, 0, keepLeft);

        // 2. Append mask characters
        int maskLength = length - (keepLeft + keepRight);
        sb.append(maskChar.repeat(maskLength));

        // 3. Append right visible characters
        sb.append(input.substring(length - keepRight));

        return sb.toString();
    }
}
