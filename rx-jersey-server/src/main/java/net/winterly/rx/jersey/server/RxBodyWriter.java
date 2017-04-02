package net.winterly.rx.jersey.server;

import net.winterly.rxjersey.server.RxGenericBodyWriter;
import rx.Observable;
import rx.Single;

import javax.inject.Singleton;

/**
 * MessageBodyWriter accepting {@link rx.Observable} or {@link rx.Single} and routing to write entity of generic type instead
 */
@Singleton
public class RxBodyWriter extends RxGenericBodyWriter {
    public RxBodyWriter() {
        super(Observable.class, Single.class);
    }
}
