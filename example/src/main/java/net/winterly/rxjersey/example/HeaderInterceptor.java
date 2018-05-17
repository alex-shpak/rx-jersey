package net.winterly.rxjersey.example;


import net.winterly.rxjersey.server.rxjava.CompletableRequestInterceptor;
import rx.Completable;

import javax.ws.rs.container.ContainerRequestContext;

public class HeaderInterceptor implements CompletableRequestInterceptor {

    public Completable intercept(ContainerRequestContext requestContext) {
        return Completable.complete();
    }
}
