package net.winterly.rxjersey.server.rxjava2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the method returns an RX {@link io.reactivex.Flowable} with multiple elements
 * which should be streamed back to the client as they are received. Without this annotation, any
 * more than a single element will be treated as an error condition.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Streamable {

  /**
   * By default, each object in the flowable will be serialised with whatever {@link
   * javax.ws.rs.ext.MessageBodyWriter} is selected by JAX-RS, then immediately flushed to the
   * client. However, this is usually only suitable for simple data formats such as CSV. For more
   * complex formats, you will need to define a {@link StreamWriter}.
   *
   * @return The stream writer.
   */
  Class<? extends StreamWriter<?, ?>> writer() default PassthroughWriter.class;

}
