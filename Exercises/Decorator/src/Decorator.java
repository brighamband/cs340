public abstract class Decorator implements IStringSource {
    IStringSource stringSource;

    public Decorator(IStringSource stringSource) {
        this.stringSource = stringSource;
    }

    @Override
    public String next() {
        return stringSource.next();
    }
}
