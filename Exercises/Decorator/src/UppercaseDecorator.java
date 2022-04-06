public class UppercaseDecorator extends Decorator {
    public UppercaseDecorator(IStringSource stringSource) {
        super(stringSource);
    }

    @Override
    public String next() {
        return super.next().toUpperCase();
    }
}
