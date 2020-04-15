import io.reactivex.Completable;
import net.winterly.rxjersey.client.WebResourceFactory;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static org.junit.Assert.*;

public class ProxiedResourceTest extends RxJerseyTest {

    private TestResource testResource1;
    private TestResource testResource2;

    @Before
    public void setup() {
        testResource1 = WebResourceFactory.newResource(TestResource.class, target(), null);
        testResource2 =  WebResourceFactory.newResource(TestResource.class, target(), null);
    }

    @Test
    public void proxySupportsEquals() {
        // Confirm that identity-based equals supported without an UnsupportedOperationException
        assertEquals(testResource1, testResource1);
        assertNotEquals(testResource1, testResource2);
        assertNotEquals("not a proxied resource", testResource1);
    }

    @Test
    public void proxySupportsHashcode() {
        // Confirm that hashCode can be invoked on the proxy object without an UnsupportedOperationException
        //noinspection ResultOfMethodCallIgnored
        testResource1.hashCode();
    }

    @Path("/endpoint")
    public interface TestResource {

        @GET
        @Path("empty")
        Completable empty();
    }
}
