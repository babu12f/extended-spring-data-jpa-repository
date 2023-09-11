package com.github.babu12f.extendedspringdatajparepository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ExtendedJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> test();
}
