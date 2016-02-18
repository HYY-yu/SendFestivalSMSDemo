package com.app.feng.treelistview.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by feng on 2015/11/11.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

@Documented
public @interface NodeName {

}
