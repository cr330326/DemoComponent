package com.cryallen.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chenran3 on 2018/2/2.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface EFModuleAnnotation {
    String moduleName();

    String delegateName();
}
