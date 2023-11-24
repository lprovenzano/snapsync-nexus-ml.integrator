package com.snapsync.nexus.repository.dto;

public abstract class DomainDTO<T> {
    public T map() {
        checker();
        return mapper();
    }

    protected abstract void checker();

    protected abstract T mapper();

}
