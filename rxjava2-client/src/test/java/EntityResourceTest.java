import io.reactivex.Maybe;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class EntityResourceTest extends RxJerseyTest {

    @Test
    public void shouldParseEntity() {
        SingleResource resource = target(SingleResource.class);
        Entity entity = resource.json("hello").blockingGet();

        assertEquals(entity.message, "hello");
    }

    @Path("/endpoint")
    public interface SingleResource {

        @GET
        @Path("json")
        Maybe<Entity> json(@QueryParam("message") String message);
    }
}
