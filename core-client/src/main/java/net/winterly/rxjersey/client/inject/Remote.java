package net.winterly.rxjersey.client.inject;

import javax.inject.Qualifier;
import javax.ws.rs.core.UriInfo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injection qualifier for Proxy Client or {@link javax.ws.rs.client.WebTarget}. <br>
 * If annotation value contains host then value will be used as target URI for {@link javax.ws.rs.client.WebTarget}
 * otherwise value will be merged with {@link UriInfo#getBaseUri()}. <br>
 * Annotation value has higher priority in merge <br><br>
 * Example:
 * <pre>
 * value="http://example.com",  baseUrl="http://baseurl.com/resource"   will produce "http://example.com/"
 * value="/resource",           baseUrl="http://baseurl.com/some"       will produce "http://baseurl.com/resource"
 * </pre>
 * Usage:
 * <pre>
 * &#064;Remote("http://example.com/")
 * ResourceInterface resource;
 * </pre>
 * <pre>
 * &#064;Remote
 * ResourceInterface resource;
 * </pre>
 *
 * @see RemoteResolver
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Remote {
    String value() default "/";
}
