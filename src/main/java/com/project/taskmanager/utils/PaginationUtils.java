package com.project.taskmanager.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public final class PaginationUtils {

    public static Pageable validateAndCreatePageable(Integer page, Integer numTasks, String sort, List<String> allowedSortFields){
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative.");
        }
        if (page > 100){
            throw new IllegalArgumentException("Page number cannot be greater than 100.");
        }
        if (numTasks <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero.");
        }
        if (numTasks > 100) {
            throw new IllegalArgumentException("Page size cannot be greater than 100.");
        }
        if (sort == null || sort.isEmpty()) {
            throw new IllegalArgumentException("Sort must be specified.");
        }

        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];


        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort.Direction direction = Sort.Direction.ASC;  // default
        if (sortParts.length > 1) {
            String sortOrder = sortParts[1];
            if (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
                throw new IllegalArgumentException("Invalid sort order: must be 'asc' or 'desc'.");
            }else if(sortOrder.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }
        }
        return PageRequest.of(page,numTasks,Sort.by(direction,sortBy));
    }
}
