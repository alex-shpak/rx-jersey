## RxJersey - Reactive Jersey Feature

[![Build Status](https://travis-ci.org/alex-shpak/rx-jersey.svg?branch=master)](https://travis-ci.org/alex-shpak/rx-jersey)
![Maven Central](https://img.shields.io/maven-central/v/net.winterly.rxjersey/core-server.svg)
[![JitPack](https://jitpack.io/v/alex-shpak/rx-jersey.svg)](https://jitpack.io/#alex-shpak/rx-jersey)

RxJersey is RxJava extension for [Jersey](https://jersey.java.net/) framework providing non-blocking Jax-RS server and client.
RxJersey target is to handle large amount requests in small static set of threads, which is highly suitable for microservice applications.

Library uses Jersey 2 async support with `@Suspended` and `AsyncResponse` under the hood.

Note that Jersey 2.26+ support is not released yet, you can obtain it from [JitPack](https://jitpack.io/#alex-shpak/rx-jersey/feature~jersey-2.27-SNAPSHOT)

### [Documentation](https://alex-shpak.github.io/rx-jersey)

## Features
- [x] RxJava Support
- [x] RxJava 2 Support
- [x] RxJava Proxy Client
- [x] Async Request Interceptors
- [x] Dropwizard bundle

## Roadmap
- [ ] Futures support
- [ ] Vert.x integration
- [ ] Improved proxy client


## Maven Artifacts
### Maven Central
```
compile "net.winterly.rxjersey:dropwizard:$rxJerseyVersion"
compile "net.winterly.rxjersey:rxjava-client:$rxJerseyVersion"
compile "net.winterly.rxjersey:rxjava-server:$rxJerseyVersion"
compile "net.winterly.rxjersey:rxjava2-client:$rxJerseyVersion"
compile "net.winterly.rxjersey:rxjava2-server:$rxJerseyVersion"
```

### JitPack
Most recent snapshot is available via [JitPack](https://jitpack.io/#alex-shpak/rx-jersey/)
```
compile "com.github.alex-shpak.rx-jersey:dropwizard:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rxjava-client:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rxjava-server:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rxjava2-client:$rxJerseyVersion"
compile "com.github.alex-shpak.rx-jersey:rxjava2-server:$rxJerseyVersion"
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
[MIT](LICENSE)
