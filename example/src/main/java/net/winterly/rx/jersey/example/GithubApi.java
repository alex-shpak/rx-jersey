package net.winterly.rx.jersey.example;

import rx.Observable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public interface GithubApi {

    @GET
    @Path("/repos/{user}/{repo}")
    Observable<GithubRepository> getRepository(@PathParam("user") String username, @PathParam("repo") String repo);
}
