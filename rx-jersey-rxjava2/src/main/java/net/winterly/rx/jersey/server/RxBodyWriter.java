package net.winterly.rx.jersey.server;

import io.reactivex.*;
import net.winterly.rxjersey.server.RxGenericBodyWriter;

import javax.annotation.Priority;
import javax.inject.Singleton;

/**
 * MessageBodyWriter accepting io.reactivex.* types and routing to write entity of generic type instead
 */
@Singleton
@Priority(1)
public class RxBodyWriter extends RxGenericBodyWriter {
    public RxBodyWriter() {
        super(
                Flowable.class,
                Observable.class,
                Single.class,
                Completable.class,
                Maybe.class
        );
    }
}
