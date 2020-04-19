package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.IterableProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides {@link InvocationHandler} for resources returning {@code io.reactivex.*} instances
 * and converts them to {@link Maybe}
 */
class StreamableInvocationHandler<I, O> extends RxInvocationHandler<Flowable<I>, Completable, Flowable<I>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamableInvocationHandler.class);

    @Inject
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    private Supplier<StreamWriter<I, O>> outputSupplier;

    StreamableInvocationHandler<I, O> setStreamWriterSupplier(Supplier<StreamWriter<I, O>> outputSupplier) {
        this.outputSupplier = outputSupplier;
        return this;
    }

    @Override
    protected Flowable<I> convert(Flowable<I> result) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) {
        LOGGER.debug("Setting up async request");
      StreamWriter<I, O> output = outputSupplier.get();
      org.glassfish.jersey.server.AsyncContext asyncContext = suspend();
        final ContainerRequestContext requestContext = requestContextProvider.get();
        Flowable.fromIterable(requestInterceptors)
            .flatMapCompletable(interceptor -> interceptor.intercept(requestContext))
            .subscribeOn(Schedulers.computation())
            .subscribe(() -> {
                LOGGER.debug("Processed interceptors");
                Flowable<I> flowable = (Flowable<I>) method.invoke(proxy, args);
                flowable.subscribe(
                    output::write,
                    e -> {
                        LOGGER.error("Error during stream", e);
                        output.error(e);
                        output.close();
                    },
                    output::close);
                output.resumeFor(asyncContext);
                LOGGER.debug("Handed off for chunked processing");
            }, e -> {
                LOGGER.error("Error streaming data", e);
                asyncContext.resume(e);
            });
        LOGGER.debug("Work submitted");
        return null; //async methods return nulls
    }

}
