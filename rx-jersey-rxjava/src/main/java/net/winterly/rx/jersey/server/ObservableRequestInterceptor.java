package net.winterly.rx.jersey.server;

import net.winterly.rxjersey.server.RxRequestInterceptor;
import org.jvnet.hk2.annotations.Contract;
import rx.Observable;

@Contract
public interface ObservableRequestInterceptor<T> extends RxRequestInterceptor<Observable<T>> {

}
