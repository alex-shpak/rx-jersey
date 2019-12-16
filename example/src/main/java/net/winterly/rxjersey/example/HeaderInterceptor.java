package net.winterly.rxjersey.example;


import io.reactivex.Completable;
import net.winterly.rxjersey.server.rxjava2.CompletableRequestInterceptor;

import javax.ws.rs.container.ContainerRequestContext;

public class HeaderInterceptor implements CompletableRequestInterceptor {

    public Completable intercept(ContainerRequestContext requestContext) {
        return Completable.complete();
    }
}
