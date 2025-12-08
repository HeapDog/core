package io.heapdog.core.shared;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;


@RestControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        if (returnType.getContainingClass().isAnnotationPresent(SkipApiWrap.class) || returnType.hasMethodAnnotation(SkipApiWrap.class)) {
            return false;
        }

        if (ApiResponse.class.isAssignableFrom(returnType.getParameterType())) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof ApiResponse) {
            return body;
        }

        if (body instanceof Page<?>) {
            Page<?> page = (Page<?>) body;

            var ub = ServletUriComponentsBuilder.fromUriString(request.getURI().toString());
            String path = request.getURI().getPath();
            body = PaginationMapper.fromPage(page, Function.identity(), ub, path);
        }

        return ApiResponse.builder()
                .timestamp(java.time.Instant.now())
                .data(body)
                .path(request.getURI().getPath())
                .build();

    }
}
