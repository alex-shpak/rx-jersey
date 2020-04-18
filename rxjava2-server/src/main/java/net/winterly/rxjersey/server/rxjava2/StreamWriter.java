package net.winterly.rxjersey.server.rxjava2;

import java.io.IOException;
import org.glassfish.jersey.server.AsyncContext;
import org.glassfish.jersey.server.ChunkedOutput;

/**
 * Responsible for serializing a {@link io.reactivex.Flowable} in a streaming fashion.
 * Here's a naive JSON example (in practice, it is recommended that you use the streaming
 * features of a library such as Gson or Jackson for this!):
 *
 * <pre>
 static final class CustomWriter extends StreamWriter&lt;Tuple, String&gt; {
     CustomWriter() {
        super(Tuple.class, String.class, "");
     }

    {@literal @}Override
     protected String transform(Tuple input) {
        return String.format("{foo:'%s',bar:'%s'}", input.getFoo(), input.getBar());
     }

    {@literal @}Override
     protected void writeChunk(String output, boolean first) throws IOException {
         if (first) {
            super.writeChunk("[" + output, true);
         } else {
            super.writeChunk("," + output, false);
         }
     }

    {@literal @}Override
     public void writeClose(boolean empty) throws IOException {
         if (empty) {
            super.writeChunk("[]", true);
         } else {
            super.writeChunk("]", false);
         }
     }
 }
 * </pre>
 *
 * @param <I> The type used in the {@link io.reactivex.Flowable}.
 * @param <O> The type to pass to {@link ChunkedOutput}. This will then be processed by
 *           the matching {@link javax.ws.rs.ext.MessageBodyWriter}. Unfortunately, this has
 *           to be shared by every chunk written (you can't mix and match chunk types).
 */
public abstract class StreamWriter<I, O> implements AutoCloseable {

    private final Class<I> streamType;
    private final Class<O> chunkType;
    private final ChunkedOutput<O> chunkedOutput;

    private volatile boolean first = true;

    /**
     * @param streamType The type used in the {@link io.reactivex.Flowable}.
     * @param chunkType The type to pass to {@link ChunkedOutput}. This will then be processed by
     *                  the matching {@link javax.ws.rs.ext.MessageBodyWriter}. Unfortunately, this has
     *                  to be shared by every chunk written (you can't mix and match chunk types).
     * @param delimiter A delimiter string to output between chunks. This will always be inserted
     *                  after every chunk so is not suitable for strict comma separation, for example.
     */
    protected StreamWriter(Class<I> streamType,
                           Class<O> chunkType,
                           String delimiter) {
        this.streamType = streamType;
        this.chunkType = chunkType;
        this.chunkedOutput = new ChunkedOutput<>(chunkType, delimiter);
    }

    /**
     * Converts from the stream type to the chunk type.
     *
     * @param input The stream object.
     * @return The chunk object.
     */
    protected abstract O transform(I input);

    /**
     * Invoked to write the chunk out to the client. Can be overriden to insert additional
     * processing before and after. By default the chunk is just passed directly.
     *
     * @param output The chunk object.
     * @param first True if this is the first chunk.
     * @throws IOException On exceptions writing out the chunk.
     */
    protected void writeChunk(O output, boolean first) throws IOException {
        chunkedOutput.write(output);
    }

    /**
     * Invoked if an error occurs processing the stream. Does nothing by default.
     *
     * @param cause The cause of the error.
     */
    void error(Throwable cause) {
        // No-op
    }

    /**
     * Invoked before the stream is closed. Can be used to write additional footer
     * data. Does nothing by default.
     *
     * @param empty True if no chunks were written.
     * @throws IOException On exceptions writing out the chunk.
     */
    protected void beforeClose(boolean empty) throws IOException {
        // No-op
    }

    final void write(I input) throws IOException {
        O output = transform(input);
        boolean firstNow = first;
        if (first) {
            synchronized (this) {
                firstNow = first;
                if (firstNow) {
                    first = false;
                }
            }
        }
        writeChunk(output, firstNow);
    }

    void resumeFor(AsyncContext asyncContext) {
        asyncContext.resume(chunkedOutput);
    }

    @Override
    public final void close() throws IOException {
        try {
            beforeClose(first);
        } finally {
            chunkedOutput.close();
        }
    }
}
