import java.util.Scanner;

public class Delete implements ICommand {

    private IDocument document;
    private int deleteIdx;
    private int deleteLength;
    private String deletedString;

    public Delete(IDocument document) {
        this.document = document;
        deleteIdx = -1;
        deleteLength = -1;
        deletedString = null;
    }

    @Override
    public void redo() {
        while (deleteIdx == -1 && deleteLength == -1) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Start index: ");
            String deleteIdxRawInput = scanner.next();
            deleteIdx = Utils.validateNumberInput(deleteIdxRawInput);

            if (deleteIdx != -1) {
                System.out.print("Number of characters to delete: ");
                String deletionDistanceInput = scanner.next();
                deleteLength = Utils.validateNumberInput(deletionDistanceInput);
            }
        }

        deletedString = document.toString().substring(deleteIdx, deleteIdx + deleteLength);
        if (document.delete(deleteIdx, deleteLength) == null) {
            System.out.println("Deletion unsuccessful");
        }
    }

    @Override
    public void undo() {
        document.insert(deleteIdx, deletedString);
    }
}
