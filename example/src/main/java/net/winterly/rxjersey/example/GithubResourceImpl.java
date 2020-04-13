package net.winterly.rxjersey.example;

import io.reactivex.Single;
import net.winterly.rxjersey.client.inject.Remote;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/example/")
@Produces(MediaType.APPLICATION_JSON)
public class GithubResourceImpl implements GithubResource {

    @Remote("https://api.github.com")
    private GithubApi githubApi;

    @Override
    public Single<GithubRepository> getRepository() {
        return githubApi.getRepository("alex-shpak", "rx-jersey");
    }

    @Override
    public Single<String> echo(String message) {
        return Single.just(message);
    }

}
