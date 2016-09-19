package net.winterly.rx.jersey.inject;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Remote {

    String value() default DEFAULT;

    String DEFAULT = "/";

}
