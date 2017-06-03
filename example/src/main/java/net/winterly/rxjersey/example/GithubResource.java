package net.winterly.rxjersey.example;

import rx.Single;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/example/")
@Produces(MediaType.APPLICATION_JSON)
public class GithubResource {

    private GithubApi githubApi;

    @GET
    @Path("github")
    public Single<GithubRepository> getRepository() {
        return githubApi.getRepository("alex-shpak", "rx-jersey").toSingle();
    }

    @GET
    @Path("echo/{message}")
    public Single<String> echo(@PathParam("message") String message) {
        return Single.just(message);
    }

}
