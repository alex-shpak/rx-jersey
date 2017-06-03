package net.winterly.rxjersey.server;

import io.reactivex.*;

import javax.annotation.Priority;
import javax.inject.Singleton;

/**
 * MessageBodyWriter accepting {@code io.reactivex.*} types
 *
 * @see Flowable
 * @see Observable
 * @see Single
 * @see Completable
 * @see Maybe
 */
@Singleton
@Priority(1)
public class RxBodyWriter extends RxGenericBodyWriter {
    public RxBodyWriter() {
        super(Flowable.class, Observable.class, Single.class, Completable.class, Maybe.class);
    }
}
