package com.project.taskmanager.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaginationUtilsTest {

    @Test
    public void shouldThrowExceptionWhenPageIsLessThanZero() {
        //arrange
        Integer page = -1;
        Integer numTasks = 10;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id","title","status");

        //act
        //assert
        assertThrows(IllegalArgumentException.class, ()-> PaginationUtils.validateAndCreatePageable(page,numTasks,sort,allowedSortFields));
    }

    @Test
    public void shouldThrowExceptionWhenPageIsMoreThanMaximum(){
        //arrange
        Integer page = 101;
        Integer numTasks = 10;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id","title","status");

        //act
        //assert
        assertThrows(IllegalArgumentException.class, ()-> PaginationUtils.validateAndCreatePageable(page,numTasks,sort,allowedSortFields));
    }

    @Test
    public void shouldAcceptLowerBoundPage(){
        //arrange
        Integer page = 0;
        Integer numTasks = 10;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id","title","status");

        //act
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page,numTasks,sort,allowedSortFields);

        //assert
        assertEquals(0,pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertTrue(pageable.getSort().isSorted());
        assertTrue(pageable.getSort().getOrderFor("id").isAscending());
    }

    @Test
    public void shouldAcceptUpperBoundPage(){
        //arrange
        Integer page = 100;
        Integer numTasks = 10;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id","title","status");

        //act
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page,numTasks,sort,allowedSortFields);

        //assert
        assertEquals(100,pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertTrue(pageable.getSort().isSorted());
        assertTrue(pageable.getSort().getOrderFor("id").isAscending());
    }

    @Test
    public void shouldThrowExceptionWhenPageSizeIsLessThanZero() {
        //arrange
        Integer page = 0;
        Integer numTasks = -1;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id", "title", "status");

        //act
        //assert
        assertThrows(IllegalArgumentException.class, () -> PaginationUtils.validateAndCreatePageable(page, numTasks, sort, allowedSortFields));
    }

    @Test
    public void shouldThrowExceptionWhenPageSizeIsMoreThanMaximum() {
        //arrange
        Integer page = 0;
        Integer numTasks = 101;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id", "title", "status");

        //act
        //assert
        assertThrows(IllegalArgumentException.class, () -> PaginationUtils.validateAndCreatePageable(page, numTasks, sort, allowedSortFields));
    }

    @Test
    public void shouldAcceptLowerBoundPageSize() {
        //arrange
        Integer page = 0;
        Integer numTasks = 1;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id", "title", "status");

        //act
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, allowedSortFields);

        //assert
        assertEquals(0, pageable.getPageNumber());
        assertEquals(1, pageable.getPageSize());
        assertTrue(pageable.getSort().isSorted());
        assertTrue(pageable.getSort().getOrderFor("id").isAscending());
    }

    @Test
    public void shouldAcceptUpperBoundPageSize() {
        //arrange
        Integer page = 0;
        Integer numTasks = 100;
        String sort = "id,asc";
        List<String> allowedSortFields = List.of("id", "title", "status");

        //act
        Pageable pageable = PaginationUtils.validateAndCreatePageable(page, numTasks, sort, allowedSortFields);

        //assert
        assertEquals(0, pageable.getPageNumber());
        assertEquals(100, pageable.getPageSize());
        assertTrue(pageable.getSort().isSorted());
        assertTrue(pageable.getSort().getOrderFor("id").isAscending());
    }


}
