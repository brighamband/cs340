public class ReverseDecorator extends Decorator {

    public ReverseDecorator(IStringSource stringSource) {
        super(stringSource);
    }

    @Override
    public String next() {
        StringBuilder revStr = new StringBuilder();

        revStr.append(super.next());

        revStr.reverse();
        return revStr.toString();
    }
}
