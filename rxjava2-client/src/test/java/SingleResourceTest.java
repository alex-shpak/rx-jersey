import io.reactivex.Single;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class SingleResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        Resource resource = target(Resource.class);
        String message = resource.echo("hello").blockingGet();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        Resource resource = target(Resource.class);
        String message = resource.empty().blockingGet();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        Resource resource = target(Resource.class);
        String message = resource.error().blockingGet();

        assertEquals("", message);
    }

    @Test
    public void shouldReturnContentForNonRxTypes() {
        Resource resource = target(Resource.class);
        String message = resource.string();

        assertEquals("string", message);
    }

    @Test
    public void shouldReturnContentForNonRxTypesWithParam() {
        Resource resource = target(Resource.class);
        Entity entity = resource.json("message");

        assertEquals("message", entity.message);
    }

    @Path("/endpoint")
    public interface Resource {

        @GET
        @Path("echo")
        Single<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Single<String> empty();

        @GET
        @Path("error")
        Single<String> error();

        @GET
        @Path("string")
        String string();

        @GET
        @Path("json")
        Entity json(@QueryParam("message") String message);
    }
}
