package net.winterly.rxjersey.client;

import rx.Observable;
import rx.Single;

/**
 * MessageBodyReader accepting {@link rx.Observable} and {@link rx.Single} and routing to read entity of generic type instead
 */
public class RxBodyReader extends RxGenericBodyReader {

    public RxBodyReader() {
        super(Observable.class, Single.class);
    }
}
