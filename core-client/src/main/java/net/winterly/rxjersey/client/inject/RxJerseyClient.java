package net.winterly.rxjersey.client.inject;

import org.glassfish.hk2.api.AnnotationLiteral;

import javax.inject.Qualifier;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface RxJerseyClient {

    class RxJerseyClientImpl extends AnnotationLiteral<RxJerseyClient> implements RxJerseyClient {}
}
