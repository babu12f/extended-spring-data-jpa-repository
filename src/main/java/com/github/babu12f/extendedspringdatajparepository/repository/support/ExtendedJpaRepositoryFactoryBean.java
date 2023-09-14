package com.github.babu12f.extendedspringdatajparepository.repository.support;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.Nullable;

import java.io.Serializable;

public class ExtendedJpaRepositoryFactoryBean<R extends Repository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    private EntityPathResolver entityPathResolver;
    private EscapeCharacter escapeCharacter;
    private JpaQueryMethodFactory queryMethodFactory;

    public ExtendedJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public void setEntityPathResolver(ObjectProvider<EntityPathResolver> resolver) {
        this.entityPathResolver = resolver.getIfAvailable(() -> SimpleEntityPathResolver.INSTANCE);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public void setQueryMethodFactory(@Nullable JpaQueryMethodFactory factory) {
        if (factory != null) {
            this.queryMethodFactory = factory;
        }
    }

    @Override
    public void setEscapeCharacter(char escapeCharacter) {
        this.escapeCharacter = EscapeCharacter.of(escapeCharacter);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        BaseRepositoryFactory baseRepositoryFactory = new BaseRepositoryFactory(entityManager);
        baseRepositoryFactory.setEntityPathResolver(this.entityPathResolver);
        baseRepositoryFactory.setEscapeCharacter(this.escapeCharacter);
        if (this.queryMethodFactory != null) {
            baseRepositoryFactory.setQueryMethodFactory(this.queryMethodFactory);
        }

        return baseRepositoryFactory;
    }

    private static class BaseRepositoryFactory extends JpaRepositoryFactory {

        public BaseRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new SimpleExtendedJpaRepository<>(information.getDomainType(), entityManager);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return SimpleExtendedJpaRepository.class;
        }
    }
}
