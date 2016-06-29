### RxJava Jersey resources support
Invocation handler and body writer for resources returning `rx.Observable`

### Usage
#### Gradle
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
```groovy
dependencies {
    compile 'com.github.alex-shpak.rx-jersey:jaxrs:0.1.0'
}
```

#### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.github.alex-shpak.rx-jersey</groupId>
        <artifactId>jaxrs</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```
#### Code
Add `RxJerseyFeature` to your `resourceConfig`
```java
reourceConfig.register(RxJerseyFeature.class);
```
Or with dropwizard
```java
environment.jersey().register(RxJerseyFeature.class);
```

Update your resource, see example
```java
@Path("/")
public class HelloResource {

    @GET
    public Observable<HelloEntity> getAsync(@Suspended AsyncResponse asyncResponse) {
        return Observable.just(new HelloEntity());
    }


    public static class HelloEntity {
        public String hello = "world";
    }
}
```

### Licence
[MIT](LICENCE)