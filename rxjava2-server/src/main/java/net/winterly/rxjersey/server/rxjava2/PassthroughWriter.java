package net.winterly.rxjersey.server.rxjava2;

import java.io.IOException;

final class PassthroughWriter extends StreamWriter<Object, Object> {

    PassthroughWriter() {
        super(Object.class, Object.class, "");
    }

    @Override
    protected void writeObject(Object input, boolean first) throws IOException {
        writeChunk(input);
    }
}
