package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.IterableProvider;

/**
 * Provides {@link InvocationHandler} for resources returning {@code io.reactivex.*} instances
 * and converts them to {@link Maybe}
 */
class StreamableInvocationHandler<I, O> extends RxInvocationHandler<Flowable<I>, Completable, Flowable<I>> {

    @Inject
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    private StreamWriter<I, O> output;
    private Consumer<String> infoLogger = System.out::println;
    private Consumer<Throwable> errorLogger = Throwable::printStackTrace;

    StreamableInvocationHandler<I, O> setOutput(StreamWriter<I, O> output) {
        this.output = output;
        return this;
    }

    StreamableInvocationHandler<I, O> setErrorLogger(Consumer<Throwable> errorLogger) {
        this.errorLogger = errorLogger;
        return this;
    }

    StreamableInvocationHandler<I, O> setInfoLogger(Consumer<String> infoLogger) {
        this.infoLogger = infoLogger;
        return this;
    }

    @Override
    protected Flowable<I> convert(Flowable<I> result) {
      throw new UnsupportedOperationException();
    }

  @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) {
        infoLogger.accept("Setting up async request");
        org.glassfish.jersey.server.AsyncContext asyncContext = suspend();
        final ContainerRequestContext requestContext = requestContextProvider.get();
        Flowable.fromIterable(requestInterceptors)
            .flatMapCompletable(interceptor -> interceptor.intercept(requestContext))
            .subscribeOn(Schedulers.computation())
            .subscribe(() -> {
                infoLogger.accept("Processed interceptors");
                Flowable<I> flowable = (Flowable<I>) method.invoke(proxy, args);
                flowable.subscribe(
                    output::write,
                    e -> {
                        errorLogger.accept(e);
                        output.error(e);
                        output.close();
                    },
                    output::close);
                output.resumeFor(asyncContext);
                infoLogger.accept("Handed off for chunked processing");
            }, e -> {
                errorLogger.accept(new RuntimeException("Error streaming data", e));
                asyncContext.resume(e);
            });
        infoLogger.accept("Work submitted");
        return null; //async methods return nulls
    }

}
