## RxJersey - Reactive Jersey Feature

[![Build Status](https://travis-ci.org/alex-shpak/rx-jersey.svg?branch=master)](https://travis-ci.org/alex-shpak/rx-jersey)

RxJersey is RxJava extension for [Jersey](https://jersey.java.net/) framework providing non-blocking Jax-RS server and client.
RxJersey target is to handle large amount requests in small static set of threads, which is highly suitable for microservice applications.

Library uses Jersey 2 async support with `@Suspended` and `AsyncResponse` under the hood.

For documentation visit [wiki](https://github.com/alex-shpak/rx-jersey/wiki)

## Features
- [x] RxJava Support
- [x] RxJava 2 Support
- [x] RxJava Proxy Client
- [x] Async Request Interceptors
- [x] Dropwizard bundle


## Maven Artifacts
### Maven Central
```
compile "net.winterly.rxjersey:dropwizard:$rxJerseyVersion"
compile "net.winterly.rxjersey:rx-jersey-rxjava:$rxJerseyVersion"
compile "net.winterly.rxjersey:rx-jersey-rxjava2:$rxJerseyVersion"
compile "net.winterly.rxjersey:rx-jersey-client:$rxJerseyVersion"
```

### JitPack
Most recent snapshot is available via [JitPack](https://jitpack.io/#alex-shpak/rx-jersey/)
```
compile "com.github.alex-shpak.rx-jersey:dropwizard:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rx-jersey-rxjava:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rx-jersey-rxjava2:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rx-jersey-client:$rxJerseyVersion"
```


## Example
```java
@Path("/example/")
public class GithubResource {

    @Remote("https://api.github.com/")
    private GithubApi githubApi;

    @GET
    @Path("github")
    public Single<GithubRepository> getRepository() {
        return githubApi.getRepository("alex-shpak", "rx-jersey").toSingle();
    }

}

@Path("/")
public interface GithubApi {

    @GET
    @Path("/repos/{user}/{repo}")
    Observable<GithubRepository> getRepository(@PathParam("user") String username, @PathParam("repo") String repo);
}

```

## Licence
[MIT](LICENCE.txt)