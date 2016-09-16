package net.winterly.rx.jersey;

import org.glassfish.jersey.message.MessageBodyWorkers;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class RxBodyReader implements MessageBodyReader<Object> {

    @Inject
    private Provider<MessageBodyWorkers> workers;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Observable.class.isAssignableFrom(raw(genericType));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        final Class actualTypeArgument = actual(genericType);
        final MessageBodyReader reader = workers.get().getMessageBodyReader(actualTypeArgument, genericType, annotations, mediaType);

        return reader.readFrom(actualTypeArgument, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

    private static Class raw(Type genericType) {
        final ParameterizedType parameterizedType = (ParameterizedType) genericType;
        return (Class) parameterizedType.getRawType();
    }

    private static Class actual(Type genericType) {
        final ParameterizedType actualGenericType = (ParameterizedType) genericType;
        return (Class) actualGenericType.getActualTypeArguments()[0];
    }

}
