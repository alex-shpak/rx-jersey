package net.winterly.rxjersey.server;

import org.jvnet.hk2.annotations.Contract;

import javax.ws.rs.container.ContainerRequestContext;

@Contract
public interface RxRequestInterceptor<T> {

    /**
     * Should be implemented as non-blocking
     * @param requestContext request context
     * @return future or observable
     */
    T intercept(ContainerRequestContext requestContext);

}
