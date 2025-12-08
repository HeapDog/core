package io.heapdog.core.shared;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;


public class PageWrappingAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Page.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (!(body instanceof Page<?>)) {
            return body; // not a page -> leave untouched
        }

        Page<?> page = (Page<?>) body;

        var ub = ServletUriComponentsBuilder.fromUriString(request.getURI().toString());
        String path = request.getURI().getPath();

        return PaginationMapper.fromPage(page, Function.identity(), ub, path);
    }
}
