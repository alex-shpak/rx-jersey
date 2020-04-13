package net.winterly.rxjersey.server;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Contract for dispatch request interceptor <br>
 * Interceptors implementing {@link RxRequestInterceptor} should be programmatically registered in {@link org.glassfish.jersey.internal.inject.InjectionManager}
 *
 * @param <T> return type of interceptor (Supposed to be Observable or Future)
 */
public interface RxRequestInterceptor<T> {

    /**
     * This method will be called for each request and should be implemented as non-blocking
     *
     * @param requestContext request context
     * @return Future or Observable
     */
    T intercept(ContainerRequestContext requestContext);

}
