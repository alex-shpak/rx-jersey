package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxRequestInterceptor;
import org.jvnet.hk2.annotations.Contract;
import rx.Observable;

/**
 * {@link RxRequestInterceptor} returning {@link rx.Observable}
 *
 * @param <T> reactive return type
 */
@Contract
public interface ObservableRequestInterceptor<T> extends RxRequestInterceptor<Observable<T>> {

}
