package com.ibosng.teilnehmerportal.dto;

import java.util.List;

public record AbwesenheitListResponse(
        boolean success,
        List<AbwesenheitEntryDto> data,
        Pagination pagination
) {
    public record Pagination(long totalCount, int pageSize, int page) {}
}
