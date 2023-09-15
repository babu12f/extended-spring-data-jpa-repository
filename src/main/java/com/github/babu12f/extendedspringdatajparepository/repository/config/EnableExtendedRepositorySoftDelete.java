package com.github.babu12f.extendedspringdatajparepository.repository.config;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableExtendedRepositorySoftDelete {
    String fieldName();
}
