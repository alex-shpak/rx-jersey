package net.winterly.rx.jersey.server;

import org.glassfish.jersey.message.MessageBodyWorkers;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static net.winterly.rx.jersey.server.RxJerseyServerFeature.isRx;

/**
 * MessageBodyWriter accepting {@link rx.Observable} or {@link rx.Single} and routing to write entity of generic type instead
 */
@Singleton
public class RxBodyWriter implements MessageBodyWriter<Object> {

    @Inject
    private Provider<MessageBodyWorkers> workers;

    private static Class raw(Type genericType) {
        final ParameterizedType parameterizedType = (ParameterizedType) genericType;
        return (Class) parameterizedType.getRawType();
    }

    private static Type actual(Type genericType) {
        final ParameterizedType actualGenericType = (ParameterizedType) genericType;
        return actualGenericType.getActualTypeArguments()[0];
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        Class rawType = raw(genericType);
        return isRx(rawType);
    }

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0; //skip
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeTo(Object entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        final Type actualTypeArgument = actual(genericType);
        final MessageBodyWriter writer = workers.get().getMessageBodyWriter(entity.getClass(), actualTypeArgument, annotations, mediaType);

        writer.writeTo(entity, entity.getClass(), actualTypeArgument, annotations, mediaType, httpHeaders, entityStream);
    }

}
