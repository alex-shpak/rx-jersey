package net.winterly.rx.jersey.example;

import io.reactivex.Completable;
import net.winterly.rx.jersey.server.CompletableRequestInterceptor;

import javax.ws.rs.container.ContainerRequestContext;

public class ReactiveInterceptor implements CompletableRequestInterceptor {

    public Completable intercept(ContainerRequestContext requestContext) {
        return Completable.complete();
    }
}
