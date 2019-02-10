import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.reactivex.Maybe;

public class MissingBodyWriterTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(XmlResource.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldReturnInternalServerError() {
        target("xml").path("writeXml")
                .request()
                .get(Message.class);
    }

    @Path("/xml")
    @Produces(MediaType.APPLICATION_XML)
    public static class XmlResource {

        @GET
        @Path("writeXml")
        public Maybe<Message> writeXml() {
            return Maybe.just(new Message("hello"));
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
