package net.winterly.rx.jersey.server.spi;

import org.jvnet.hk2.annotations.Contract;
import rx.Observable;

import javax.ws.rs.container.ContainerRequestContext;

@Contract
public interface RxRequestInterceptor<T> {

    Observable<T> apply(ContainerRequestContext requestContext);

}
