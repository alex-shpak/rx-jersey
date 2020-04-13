package net.winterly.rxjersey.client.rxjava2;

import io.reactivex.*;
import net.winterly.rxjersey.client.RxGenericBodyReader;

import javax.annotation.Priority;

/**
 * MessageBodyReader accepting rx types and routing to read entity of generic type instead
 *
 * @see Flowable
 * @see Observable
 * @see Single
 * @see Completable
 * @see Maybe
 */
@Priority(1) //Priority should be higher than JSON providers
public class RxBodyReader extends RxGenericBodyReader {

    public RxBodyReader() {
        super(Flowable.class, Observable.class, Single.class, Completable.class, Maybe.class);
    }
}
