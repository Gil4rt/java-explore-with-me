package ru.practicum.mainservice.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPageable implements Pageable {
    private final int offset;
    private final int limit;
    private final Sort sort;

    protected OffsetPageable(int offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public static Pageable newInstance(int myOffset, int myLimit, Sort mySort) {
        if (myOffset < 0 || myLimit < 1) {
            throw new RuntimeException("Invalid pagination parameters: offset must be non-negative, limit must be positive.");
        }
        return new OffsetPageable(myOffset, myLimit, mySort);
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return newInstance((int) (getOffset() + getPageSize()), getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? newInstance((int) (getOffset() - getPageSize()), getPageSize(), getSort()) : first();
    }

    @Override
    public Pageable first() {
        return newInstance(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return newInstance(getPageSize() * pageNumber, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return getOffset() >= getPageSize();
    }
}

