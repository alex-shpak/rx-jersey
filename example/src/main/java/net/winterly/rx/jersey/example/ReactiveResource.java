package net.winterly.rx.jersey.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reactivex.Maybe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/example/")
@Produces(MediaType.APPLICATION_JSON)
public class ReactiveResource {

    @GET
    public Maybe<Message> getMessage() {
        return Maybe.just(new Message());
    }

    public static class Message {

        @JsonProperty
        public String message = "Hello!";
    }
}
