package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.*;
import net.winterly.rxjersey.server.RxGenericBodyWriter;

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
@Priority(1) //Priority should be higher than JSON providers
public class RxBodyWriter extends RxGenericBodyWriter {
    public RxBodyWriter() {
        super(Flowable.class, Observable.class, Single.class, Completable.class, Maybe.class);
    }
}
