package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxGenericBodyWriter;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * {@link MessageBodyWriter} accepting {@link rx.Observable} or {@link rx.Single}
 *
 * @see Observable
 * @see Single
 */
@Singleton
@Priority(1) //Priority should be higher than JSON providers
public class RxBodyWriter extends RxGenericBodyWriter {
    public RxBodyWriter() {
        super(Observable.class, Single.class, Completable.class);
    }
}
