import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.junit.Test;

import io.reactivex.Single;

public class SubResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContentFromSubResourceWithoutPath() {
        SingleResource resource = target(SingleResource.class);
        String subResourceId = resource.getSubResource("someId").getId().blockingGet();

        assertEquals("someId", subResourceId);
    }

    @Test
    public void shouldReturnContentFromSubResourcePath() {
        SingleResource resource = target(SingleResource.class);
        String attribute = resource.getSubResource("someId").getAttribute().blockingGet();

        assertEquals("attribute", attribute);
    }

    @Path("/endpoint")
    public interface SingleResource {

        @Path("subresource/{id}")
        SingleSubResource getSubResource(@PathParam("id") String id);
    }

    public interface SingleSubResource {

        @GET
        Single<String> getId();

        @GET
        @Path("attribute")
        Single<String> getAttribute();

    }
}
