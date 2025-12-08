package io.heapdog.core.shared;

import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class PaginationMapper {

    private PaginationMapper() {}

    public static <E, D> PaginatedResponse<D> fromPage(Page<E> page,
                                                       Function<? super E, ? extends D> mapper,
                                                       UriComponentsBuilder uriBuilder,
                                                       String path) {
        List<D> contents = page.getContent().stream().map(mapper).collect(Collectors.toList());

//        PageMeta meta = new PageMeta();
//        meta.setPage(page.getNumber());
//        meta.setSize(page.getSize());
//        meta.setTotalPages(page.getTotalPages());
//        meta.setTotalElements(page.getTotalElements());
//        meta.setNumberOfElements(page.getNumberOfElements());
//        meta.setFirst(page.isFirst());
//        meta.setLast(page.isLast());
        PageMeta meta = PageMeta.builder()
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();

//        PageLinks links = new PageLinks();
//        links.setSelf(buildPageUri(uriBuilder, path, page.getNumber(), page.getSize()));
//        links.setFirst(buildPageUri(uriBuilder, path, 0, page.getSize()));
//        links.setLast(buildPageUri(uriBuilder, path, page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1, page.getSize()));
//        links.setPrev(page.hasPrevious() ? buildPageUri(uriBuilder, path, page.getNumber() - 1, page.getSize()) : null);
//        links.setNext(page.hasNext() ? buildPageUri(uriBuilder, path, page.getNumber() + 1, page.getSize()) : null);

        return PaginatedResponse.<D>builder()
                .contents(contents)
                .meta(meta)
                .build();
    }

    private static String buildPageUri(UriComponentsBuilder uriBuilder, String path, int page, int size) {
        return uriBuilder.path(path)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUriString();
    }
}

