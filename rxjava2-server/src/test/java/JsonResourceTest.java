import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.Maybe;
import org.junit.Test;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @Test
    public void shouldValidateJsonEntities() {
        final Response response = target("json").path("validateJson")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(new Message(null)));

        assertEquals(400, response.getStatus());
    }


    @Path("/json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static class JsonResource {

        @GET
        @Path("writeJson")
        public Maybe<Message> writeJson() {
            return Maybe.just(new Message("hello"));
        }

        @POST
        @Path("readJson")
        public Maybe<Message> readJson(@NotNull Message message) {
            return Maybe.just(message);
        }

        @POST
        @Path("validateJson")
        public Maybe<Message> validateJson(@Valid Message message) {
            return Maybe.just(message);
        }

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
