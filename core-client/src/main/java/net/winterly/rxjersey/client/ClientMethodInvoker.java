package net.winterly.rxjersey.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

public interface ClientMethodInvoker<R> {

    <T> R method(Invocation.Builder builder, String name, GenericType<T> responseType);

    <T> R method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType);
}
