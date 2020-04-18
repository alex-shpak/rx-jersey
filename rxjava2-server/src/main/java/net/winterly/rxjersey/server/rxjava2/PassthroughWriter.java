package net.winterly.rxjersey.server.rxjava2;

final class PassthroughWriter extends StreamWriter<Object, Object> {

    PassthroughWriter() {
        super(Object.class, Object.class, "");
    }

    @Override
    protected Object transform(Object input) {
        return input;
    }
}
