package com.github.babu12f.extendedspringdatajparepository.repository.support;

import com.github.babu12f.extendedspringdatajparepository.repository.ExtendedJpaRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;

public class SimpleExtendedJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExtendedJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;

    public SimpleExtendedJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;
    }

    @Override
    @Deprecated
    public List<T> test() {
        return findAll();
    }

    @Override
    public long countDomainData() {
        return this.countDomainData(null);
    }

    @Override
    public long countDomainData(@Nullable Specification<T> spec) {
        return count(spec);
    }
}
