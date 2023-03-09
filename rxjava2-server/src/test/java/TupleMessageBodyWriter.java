import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

public class TupleMessageBodyWriter implements MessageBodyWriter<Tuple> {

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type.equals(Tuple.class) && mediaType.equals(MediaType.TEXT_HTML_TYPE);
  }

  @Override
  public void writeTo(Tuple tuple, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException {
    entityStream.write(tuple.toString().getBytes(StandardCharsets.UTF_8));
  }
}
