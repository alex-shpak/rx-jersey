package net.winterly.rxjersey.server;

import io.reactivex.Completable;
import org.jvnet.hk2.annotations.Contract;

/**
 * {@link RxRequestInterceptor} returning {@link io.reactivex.Completable}
 */
@Contract
public interface CompletableRequestInterceptor extends RxRequestInterceptor<Completable> {

}
