import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

import io.reactivex.Flowable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import net.winterly.rxjersey.server.rxjava2.Streamable;
import net.winterly.rxjersey.server.rxjava2.StreamWriter;
import org.junit.Test;

public class FlowableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(FlowableResource.class);
    }

    @Test
    public void shouldReturnContent() {
        final String message = target("flowable").path("echo")
                .queryParam("message", "hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnEmptyObservable() {
        final int status = target("flowable").path("empty")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("flowable").path("npe")
                .request()
                .get(String.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnMultipleEntities() {
        target("flowable").path("multiple")
                .request()
                .get(String.class);
    }

    @Test
    public void shouldReturnStreamedStrings() {
        String result = target("flowable").path("stringStream")
            .request()
            .get(String.class);

        assertEquals(
            IntStream.range(1, 50).mapToObj(i -> "Item " + i + "\n").collect(joining()),
            result);
    }

    @Test
    public void shouldReturnObjectStrings() {
        String result = target("flowable").path("objectStream")
            .request()
            .get(String.class);

        assertEquals(
            "[" + IntStream.range(1, 50).mapToObj(i -> String.format("{foo:'foo%d',bar:'bar%d'}", i, i)).collect(joining(",")) + "]",
            result);
    }


    @Path("/flowable")
    public static class FlowableResource {

        @GET
        @Path("echo")
        public Flowable<String> echo(@QueryParam("message") String message) {
            return Flowable.just(message);
        }

        @GET
        @Path("empty")
        public Flowable<String> empty() {
            return Flowable.empty();
        }

        @GET
        @Path("npe")
        public Flowable<String> npe() {
            return null;
        }

        @GET
        @Path("multiple")
        public Flowable<String> multiple() {
            return Flowable.just("hello", "rx");
        }

        @GET
        @Path("stringStream")
        @Streamable
        public Flowable<String> stringStream() {
            return Flowable.range(1, 49)
                .zipWith(Flowable.interval(5, TimeUnit.MILLISECONDS), (it, interval) -> it)
                .map(i -> "Item " + i + "\n");
        }

        @GET
        @Path("objectStream")
        @Streamable(writer = StringWriter.class)
        public Flowable<Tuple> objectStream() {
            return Flowable.range(1, 49)
                .zipWith(Flowable.interval(5, TimeUnit.MILLISECONDS), (it, interval) -> it)
                .map(Tuple::new);
        }

    }

    static final class StringWriter extends StreamWriter<Tuple, String> {

        StringWriter() {
            super(Tuple.class, String.class, "");
        }

        @Override
        protected String transform(Tuple input) {
            return String.format("{foo:'%s',bar:'%s'}", input.getFoo(), input.getBar());
        }

        @Override
        protected void writeChunk(String output, boolean first) throws IOException {
            if (first) {
                super.writeChunk("[" + output, true);
            } else {
                super.writeChunk("," + output, false);
            }
        }

        @Override
        public void beforeClose(boolean empty) throws IOException {
            if (empty) {
                super.writeChunk("[]", true);
            } else {
                super.writeChunk("]", false);
            }
        }
    }

}
