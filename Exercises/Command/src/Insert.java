import java.util.Scanner;

public class Insert implements ICommand {

    private IDocument document;
    private int insertIdx;
    private String sequenceToInsert;

    public Insert(IDocument document) {
        this.document = document;
        insertIdx = -1;
        sequenceToInsert = null;
    }

    @Override
    public void redo() {
        while (insertIdx != -1 && sequenceToInsert == null) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Start index: ");
            String insertIdxRawInput = scanner.next();
            insertIdx = Utils.validateNumberInput(insertIdxRawInput);

            if (insertIdx != -1) {
                System.out.print("Sequence to insert: ");
                sequenceToInsert = scanner.next();
            }
        }
        document.insert(insertIdx, sequenceToInsert);
    }

    @Override
    public void undo() {
        document.delete(insertIdx, sequenceToInsert.length());
    }
}
