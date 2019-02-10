package net.winterly.rxjersey.server.rxjava2;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.message.internal.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.reactivex.Maybe;

public class RxBodyWriterTest {

    private ServiceLocator serviceLocator;

    @Before
    public void setUp() {
        serviceLocator = ServiceLocatorUtilities.bind(
                new MessageBodyFactory.Binder(), new MessagingBinders.MessageBodyProviders(null, null),
                new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(RxBodyWriter.class).to(MessageBodyWriter.class).in(Singleton.class);
                    }
                });
    }

    @Test
    public void shouldWriteMessageBody() throws NoSuchMethodException, IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        testWriteTo("text", MediaType.TEXT_PLAIN_TYPE, "test", outputStream);

        final String response = new String(outputStream.toByteArray());

        Assert.assertEquals("test", response);
    }

    @Test(expected = MessageBodyProviderNotFoundException.class)
    public void shouldThrowMessageBodyProviderNotFoundException() throws NoSuchMethodException, IOException {
        testWriteTo("xml", MediaType.APPLICATION_XML_TYPE, new Message("test"), new NullOutputStream());
    }

    private OutputStream testWriteTo(String methodName, MediaType mediaType, Object entity, OutputStream outputStream) throws NoSuchMethodException, IOException {

        final MessageBodyFactory messageBodyFactory = serviceLocator.getService(MessageBodyFactory.class);

        final Method textMethod = TestResource.class.getMethod(methodName);
        final Type genericReturnType = textMethod.getGenericReturnType();
        final Annotation[] annotations = textMethod.getDeclaredAnnotations();

        final MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>(
                Collections.singletonMap(HttpHeaders.CONTENT_TYPE, Collections.singletonList(mediaType))
        );

        return messageBodyFactory.writeTo(entity, entity.getClass(), genericReturnType, annotations, mediaType, headers,
                new MapPropertiesDelegate(), outputStream, Collections.emptyList());
    }

    @Path("/resource")
    public interface TestResource {

        @GET
        @Path("xml")
        @Produces(MediaType.APPLICATION_XML)
        Maybe<Message> xml();

        @GET
        @Path("text")
        @Produces(MediaType.TEXT_PLAIN)
        Maybe<String> text();

    }

    public static class Message {

        @NotNull
        public String message;

        @JsonCreator
        public Message(@JsonProperty("message") String message) {
            this.message = message;
        }
    }

}