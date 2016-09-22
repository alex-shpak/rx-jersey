## Reactive Jersey feature
RxJava support for Jersey resources and proxy client with non-blocking experience.
Library uses Jersey 2 async support with `@Suspended` and `AsyncResponse` under-the-hood.

Comes with dropwizard bundle.

Please report bugs and share ideas.


## Roadmap
- [ ] Tests coverage
- [ ] Switchable client connectors (Grizzly/Netty)



## Maven Artifact
[https://jitpack.io/#alex-shpak/rx-jersey/0.5.3](https://jitpack.io/#alex-shpak/rx-jersey/0.5.3)

```gradle
compile "com.github.alex-shpak.rx-jersey:dropwizard:$rxJerseyVersion"
```



## Dropwizard Bundle
Add dropwizard bundle to bootstrap
```java
bootstrap.addBundle(new RxResourceBundle())
```

This will register both client and server features



## Jersey Server
register `RxJerseyServerFeature` in `resourceConfig`
```java
resourceConfig.register(RxJerseyServerFeature.class);
```

Update your resource, see example:
```java
@Path("/")
public class HelloResource {

    @GET
    public Observable<HelloEntity> getAsync() {
        return Observable.just(new HelloEntity());
    }


    public static class HelloEntity {
        public String hello = "world";
    }
}
```



## Jersey client
register `RxJerseyClientFeature` in `resourceConfig`
```java
resourceConfig.register(RxJerseyClientFeature.class);
```

Update your resource, see example:
```java
@Path("/")
public class HelloResource {

    @Remote("http://example.com") //skip annotation value to get target from current context
    private OtherResource remote;

    @GET
    public Observable<HelloEntity> getAsync() {
        return remote.map( it -> it.doStuff() );
    }

}
```



## Licence
[MIT](LICENCE.txt)