public final class Tuple {
    private String foo;
    private String bar;

    Tuple(int i) {
        this.foo = "foo" + i;
        this.bar = "bar" + i;
    }

    @Override
    public String toString() {
        return "{" +
            "foo:'" + foo + '\'' +
            ",bar:'" + bar + '\'' +
            '}';
    }

    public String getBar() {
        return bar;
    }

    public String getFoo() {
        return foo;
    }
}
