import java.util.Scanner;

public class Save implements ICommand {

    private IDocument document;
    private String saveFileName;

    public Save(IDocument document) {
        this.document = document;
        this.saveFileName = null;
    }

    @Override
    public void redo() {
        while (saveFileName == null) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Name of file: ");
            saveFileName = scanner.next();
        }
        document.save(saveFileName);
    }

    @Override
    public void undo() {
        document.clear();
    }
}
