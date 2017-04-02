package net.winterly.rx.jersey.server;

import io.reactivex.Completable;
import net.winterly.rxjersey.server.RxRequestInterceptor;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface CompletableRequestInterceptor extends RxRequestInterceptor<Completable> {

}
