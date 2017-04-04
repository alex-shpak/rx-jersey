package net.winterly.rx.jersey.example;


import net.winterly.rx.jersey.server.ObservableRequestInterceptor;
import rx.Observable;

import javax.ws.rs.container.ContainerRequestContext;

public class HeaderInterceptor implements ObservableRequestInterceptor<Void> {

    public Observable<Void> intercept(ContainerRequestContext requestContext) {
        return Observable.empty();
    }
}
