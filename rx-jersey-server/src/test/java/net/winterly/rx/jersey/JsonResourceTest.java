package net.winterly.rx.jersey;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;
import rx.Observable;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class JsonResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(JsonResource.class);
    }

    @Test
    public void shouldWriteJsonEntities() {
        final Message message = target("json").path("writeJson")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Message.class);

        assertEquals("hello", message.message);
    }

    @Test
    public void shouldReadJsonEntities() {
        final Message message = target("json").path("readJson")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new Message("hello")))
                .readEntity(Message.class);

        assertEquals("hello", message.message);
    }


    @Path("/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static class JsonResource {

        @GET
        @Path("writeJson")
        public Observable<Message> writeJson() {
            return Observable.just(new Message("hello"));
        }

        @POST
        @Path("readJson")
        public Observable<Message> readJson(@NotNull Message message) {
            return Observable.just(message);
        }

    }

    public static class Message {

        public String message;

        @JsonCreator
        public Message(@JsonProperty("message") String message) {
            this.message = message;
        }
    }
}
