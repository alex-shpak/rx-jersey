import io.reactivex.Flowable;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class FlowableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        Resource resource = target(Resource.class);
        String message = resource.echo("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        Resource resource = target(Resource.class);
        String message = resource.empty().blockingFirst();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        Resource resource = target(Resource.class);
        String message = resource.error().blockingFirst();

        assertEquals("", message);
    }

    @Test(expected = NotSupportedException.class)
    public void shouldThrowSensibleErrorForNonRxType() {
        Resource resource = target(Resource.class);
        String message = resource.string();

        assertEquals("", message);
    }

    @Test(expected = NotSupportedException.class)
    public void shouldThrowSensibleErrorForNonRxTypeWithParam() {
        Resource resource = target(Resource.class);
        Entity entity = resource.json("message");

        assertEquals("", entity.message);
    }

    @Path("/endpoint")
    public interface Resource {

        @GET
        @Path("echo")
        Flowable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Flowable<String> empty();

        @GET
        @Path("error")
        Flowable<String> error();

        @GET
        @Path("string")
        String string();

        @GET
        @Path("json")
        Entity json(@QueryParam("message") String message);
    }
}
