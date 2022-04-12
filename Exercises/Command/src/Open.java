import java.util.Scanner;

public class Open implements ICommand {

    private IDocument document;
    private String openFileName;

    public Open(IDocument document) {
        this.document = document;
        this.openFileName = null;
    }

    @Override
    public void redo() {
        while (openFileName == null) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Name of file to open: ");
            openFileName = scanner.next();
        }
        document.open(openFileName);
    }

    @Override
    public void undo() {
        document.clear();
    }
}
