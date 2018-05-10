package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxRequestInterceptor;
import org.jvnet.hk2.annotations.Contract;
import rx.Completable;

/**
 * {@link RxRequestInterceptor} returning {@link rx.Completable}
 */
@Contract
public interface CompletableRequestInterceptor extends RxRequestInterceptor<Completable> {

}
