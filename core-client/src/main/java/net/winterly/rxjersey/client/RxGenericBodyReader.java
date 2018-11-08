package net.winterly.rxjersey.client;

import org.glassfish.jersey.message.MessageBodyWorkers;

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
import java.util.Arrays;
import java.util.List;

/**
 * MessageBodyReader accepting rx.Observable and routing to read entity of generic type instead
 */
public abstract class RxGenericBodyReader implements MessageBodyReader<Object> {

    private final List<Class> allowedTypes;

    @Inject
    private Provider<MessageBodyWorkers> workers;

    /**
     * @param allowedTypes list of types to be processed by this reader.
     */
    protected RxGenericBodyReader(Class<?>... allowedTypes) {
        this.allowedTypes = Arrays.asList(allowedTypes);
    }

    private static Type actual(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            final ParameterizedType actualGenericType = (ParameterizedType) genericType;
            return actualGenericType.getActualTypeArguments()[0];
        }

        return String.class;
    }

    private static Class entityType(Type actualTypeArgument) {
        if (actualTypeArgument instanceof Class) {
            return (Class) actualTypeArgument;
        }

        if (actualTypeArgument instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) actualTypeArgument;
            return (Class) parameterized.getRawType();
        }

        throw new IllegalArgumentException();
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return allowedTypes.contains(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        final Type actualTypeArgument = actual(genericType);
        final Class entityType = entityType(actualTypeArgument);
        final MessageBodyReader reader = workers.get().getMessageBodyReader(entityType, actualTypeArgument, annotations, mediaType);

        return reader.readFrom(entityType, actualTypeArgument, annotations, mediaType, httpHeaders, entityStream);
    }
}
