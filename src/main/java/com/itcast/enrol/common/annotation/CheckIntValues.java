package com.itcast.enrol.common.annotation;

/**
 * Created by liuzhongshuai on 2017/10/29.
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = CheckEnumValidator.class)
@Documented
public @interface CheckIntValues {

    /**
     * 验证未通过的提示语
     *
     * @return
     */
    String message() default "验证未通过!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 字段是否可为空
     *
     * @return
     */
    boolean ableNull() default false;

    /**
     * 字段值的枚举
     *
     * @return
     */
    int[] values();
}
