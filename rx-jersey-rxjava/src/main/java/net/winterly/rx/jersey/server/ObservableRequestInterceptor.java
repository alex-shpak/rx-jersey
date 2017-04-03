package net.winterly.rx.jersey.server;

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
