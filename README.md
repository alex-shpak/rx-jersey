## RxJava Jersey resources support with dropwizard bundle
RxJava support for Jersey resources and proxy client. Comes with dropwizard bundle.
Library uses Jersey 2 async support with `@Suspended` and `AsyncResponse` under-the-hood.

Please report bugs and share ideas.



## Maven Artifact
[https://jitpack.io/#alex-shpak/rx-jersey/0.5.1](https://jitpack.io/#alex-shpak/rx-jersey/0.5.1)



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
        return remote.map( it -> new SomeEntity() );
    }

}
```



## Licence
[MIT](LICENCE.txt)