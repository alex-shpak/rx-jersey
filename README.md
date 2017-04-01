## Reactive Jersey feature
RxJava support for Jersey resources and proxy client with non-blocking experience.
Library uses Jersey 2 async support with `@Suspended` and `AsyncResponse` under-the-hood.

Comes with dropwizard bundle.

Please report bugs and share ideas.


## Roadmap
- [x] Tests coverage
- [ ] Switchable client connectors (Grizzly/Netty)
- [x] rx.Single support (Only server)
- [x] Async request interceptors
- [ ] RxJava 2.0



## Maven Artifact
[https://jitpack.io/#alex-shpak/rx-jersey/0.7.0](https://jitpack.io/#alex-shpak/rx-jersey/0.7.0)

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
    public Single<HelloEntity> getAsync() {
        return Single.just(new HelloEntity());
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
        return remote.call().map(it -> it.doStuff());
    }

}
```

## Important notes
### RxJava 1
 - It's recommended to use `rx.Single` as return type (Representing single response entity).
 - Multiple elements emitted in `Observable` will be treated as error.
 - Empty `Observable` or `null` value in `Observable` or `Single` will be treated as `204: No content`.

### RxJava 2 (Upcoming)
 - It's recommended to use `io.reactivex.Maybe` which could be 0 or 1 item or an error.
 - Multiple elements emitted in `Observable` or `Flowable` will be treated as error.
 - Empty `Observable`/`Maybe` will be treated as `204: No content`.
 - `Completable` will be executed and `204: No content` will be returned.
 

## Licence
[MIT](LICENCE.txt)