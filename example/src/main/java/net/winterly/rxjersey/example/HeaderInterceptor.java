package net.winterly.rxjersey.example;


import net.winterly.rxjersey.server.rxjava.ObservableRequestInterceptor;
import rx.Observable;

import javax.ws.rs.container.ContainerRequestContext;

public class HeaderInterceptor implements ObservableRequestInterceptor<Void> {

    public Observable<Void> intercept(ContainerRequestContext requestContext) {
        return Observable.empty();
    }
}
