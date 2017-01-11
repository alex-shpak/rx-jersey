package net.winterly.rx.jersey.client;

import org.glassfish.jersey.client.rx.RxInvocationBuilder;
import org.glassfish.jersey.client.rx.RxInvoker;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import java.util.Observable;

public interface WebResourceInvoker<T> {

    <R> Object method(T builder, String name, Entity<?> entity, GenericType<R> responseType);
    <R> Object method(T builder, String name, GenericType<R> responseType);

    class Sync implements WebResourceInvoker<Invocation.Builder> {

        @Override
        public <R> Object method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<R> responseType) {
            return builder.method(name, entity, responseType);
        }

        @Override
        public <R> Object method(Invocation.Builder builder, String name, GenericType<R> responseType) {
            return builder.method(name, responseType);
        }
    }

    class Rx implements WebResourceInvoker<RxInvocationBuilder<RxInvoker<Observable>>> {

        @Override
        public <R> Object method(RxInvocationBuilder<RxInvoker<Observable>> builder, String name, Entity<?> entity, GenericType<R> responseType) {
            return builder.rx().method(name, entity, responseType);
        }

        @Override
        public <R> Object method(RxInvocationBuilder<RxInvoker<Observable>> builder, String name, GenericType<R> responseType) {
            return builder.rx().method(name, responseType);
        }
    }
}
