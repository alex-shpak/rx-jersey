package net.winterly.rxjersey.example;

import io.reactivex.Single;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/example/")
@Produces(MediaType.APPLICATION_JSON)
public interface GithubResource {

    @GET
    @Path("github")
    Single<GithubRepository> getRepository();

    @GET
    @Path("echo/{message}")
    Single<String> echo(@PathParam("message") String message);

}