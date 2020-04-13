package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxRequestInterceptor;
import rx.Completable;

/**
 * {@link RxRequestInterceptor} returning {@link rx.Completable}
 */
public interface CompletableRequestInterceptor extends RxRequestInterceptor<Completable> {

}
