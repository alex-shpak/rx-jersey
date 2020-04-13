import org.junit.Test;
import rx.Single;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class EntityResourceTest extends RxJerseyTest {

    @Test
    public void shouldParseEntity() {
        SingleResource resource = target(SingleResource.class);
        Entity entity = resource.json("hello").toBlocking().value();

        assertEquals(entity.message, "hello");
    }

    @Path("/endpoint")
    public interface SingleResource {

        @GET
        @Path("json")
        Single<Entity> json(@QueryParam("message") String message);
    }
}
