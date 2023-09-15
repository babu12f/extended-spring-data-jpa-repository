package com.github.babu12f.extendedspringdatajparepository.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> test();

    List<T> readAllDomainData();

    List<T> readAllDomainData(@Nullable Specification<T> spec);

    long countDomainData();

    long countDomainData(@Nullable Specification<T> spec);
}
