package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.Completable;
import net.winterly.rxjersey.server.RxRequestInterceptor;

/**
 * {@link RxRequestInterceptor} returning {@link io.reactivex.Completable}
 */
public interface CompletableRequestInterceptor extends RxRequestInterceptor<Completable> {

}
