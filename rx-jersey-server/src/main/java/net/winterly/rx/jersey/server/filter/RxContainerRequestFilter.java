package net.winterly.rx.jersey.server.filter;

import org.jvnet.hk2.annotations.Contract;
import rx.Observable;

import javax.ws.rs.container.ContainerRequestContext;

@Contract
public interface RxContainerRequestFilter {

    Observable<Void> filter(ContainerRequestContext requestContext);

}
