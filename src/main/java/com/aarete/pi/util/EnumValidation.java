package com.aarete.pi.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface EnumValidation {
    Class<? extends Enum<?>> enumClass();

    String message() default "must be any of enum {enumClass}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}