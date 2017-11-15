package net.winterly.rxjersey.client.rxjava;

import net.winterly.rxjersey.client.RxGenericBodyReader;
import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * MessageBodyReader accepting {@link rx.Observable} and {@link rx.Single} and routing to read entity of generic type instead
 */
public class RxBodyReader extends RxGenericBodyReader {

    public RxBodyReader() {
        super(Observable.class, Single.class, Completable.class);
    }
}
