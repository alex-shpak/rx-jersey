package net.winterly.rxjersey.client;

import org.glassfish.jersey.client.rx.RxInvocationBuilder;
import org.glassfish.jersey.client.rx.RxInvoker;
import rx.Observable;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;

public class ObservableClientMethodInvoker implements ClientMethodInvoker<Observable, RxInvocationBuilder<RxInvoker<Observable>>> {

    @Override
    public <T> Observable method(RxInvocationBuilder<RxInvoker<Observable>> builder, String name, GenericType<T> responseType) {
        return builder.rx().method(name, responseType);
    }

    @Override
    public <T> Observable method(RxInvocationBuilder<RxInvoker<Observable>> builder, String name, Entity<?> entity, GenericType<T> responseType) {
        return builder.rx().method(name, entity, responseType);
    }
}
