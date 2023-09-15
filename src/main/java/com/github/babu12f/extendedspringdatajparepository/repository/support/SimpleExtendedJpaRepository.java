package com.github.babu12f.extendedspringdatajparepository.repository.support;

import com.github.babu12f.extendedspringdatajparepository.repository.ExtendedJpaRepository;
import com.github.babu12f.extendedspringdatajparepository.repository.config.EnableExtendedRepositorySoftDelete;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SimpleExtendedJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExtendedJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager em;

    private final EnableExtendedRepositorySoftDelete annotationSoftDelete;
    private final Boolean isEnableSoftDelete;
    private final String softDeleteFieldName;

    public SimpleExtendedJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.em = entityManager;

        this.annotationSoftDelete = getDomainClass().getAnnotation(EnableExtendedRepositorySoftDelete.class);
        this.isEnableSoftDelete = this.annotationSoftDelete != null;
        this.softDeleteFieldName = this.isEnableSoftDelete ? this.annotationSoftDelete.fieldName() : null;
    }

    public SimpleExtendedJpaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Override
    @Deprecated
    public List<T> test() {
        return findAll();
    }

    @Override
    public List<T> readAllDomainData() {
        return this.readAllDomainData(null);
    }

    @Override
    public List<T> readAllDomainData(@Nullable Specification<T> originSpecification) {
        Specification<T> specification;
        specification = isEnableSoftDelete ? this.addSoftDeleteCondition(originSpecification) : null;

        return specification != null ? findAll(specification) : findAll();
    }

    @Override
    public long countDomainData() {
        return this.countDomainData(null);
    }

    @Override
    public long countDomainData(@Nullable Specification<T> spec) {
        return count(spec);
    }

    private Specification<T> addSoftDeleteCondition(Specification<T> originSpecification) {
        return Specification
                .where(originSpecification)
                .and((Specification<T>) (root, query, cb) -> cb.notEqual(root.get(softDeleteFieldName), true));
    }

    private Specification<T> addSoftDeleteCondition2(Specification<T> originSpecification) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (originSpecification != null) {
                predicates.add(originSpecification.toPredicate(root, query, cb));
            }
            predicates.add(cb.notEqual(root.get(softDeleteFieldName), true));

            return query.where(cb.and(predicates.toArray(new Predicate[0]))).getGroupRestriction();
        };
    }
}
