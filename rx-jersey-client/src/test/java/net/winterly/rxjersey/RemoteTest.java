package net.winterly.rxjersey;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.junit.Test;
import rx.Observable;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

public class RemoteTest extends RxJerseyTest {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    protected Application configure() {
        return config();
    }

    @Test
    public void shouldReturnContent() {
        //GithubApi githubApi = WebResourceFactoryPatched.newResource(GithubApi.class, remote(URI.create("https://api.github.com/")));
        //GithubRepository repository = githubApi.getRepository("alex-shpak", "rx-jersey").toBlocking().first();

        //RxClient<RxInvoker<Observable>> rxClient = serviceLocator.getService(RxClient.class);
        RxWebTarget<RxObservableInvoker> rxWebTarget = RxObservable.from(client()).target("https://api.github.com/");
        Observable<GithubRepository> obs = rxWebTarget.path("/repos/alex-shpak/rx-jersey").request(MediaType.APPLICATION_JSON).rx().method("GET", GithubRepository.class);

        GithubRepository object = obs.toBlocking().first();
    }

    @Path("/")
    public interface GithubApi {

        @GET
        @Path("/repos/{user}/{repo}")
        Observable<GithubRepository> getRepository(@PathParam("user") String username, @PathParam("repo") String repo);
    }

    public static class GithubRepository {

        public long id;
        public String name;
        public String full_name;
        public String description;
        public String html_url;

        @JsonProperty("private")
        public boolean isPrivate;

    }
}
