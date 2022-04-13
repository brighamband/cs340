import java.util.Scanner;

public class Replace implements ICommand {

    private IDocument document;
    private int replaceIdx;
    private int replaceLength;
    private String replaceString;

    public Replace(IDocument document) {
        this.document = document;
        this.replaceIdx = -1;
        this.replaceLength = -1;
    }

    @Override
    public void redo() {
        while (replaceIdx == -1 && replaceLength == -1) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Start index: ");
            String replaceIdxRawInput = scanner.next();
            replaceIdx = Utils.validateNumberInput(replaceIdxRawInput);
            if (replaceIdx != -1) {
                System.out.print("Number of characters to replace: ");
                String replaceLengthRawInput = scanner.next();
                replaceLength = Utils.validateNumberInput(replaceLengthRawInput);
                if (Replace.this.replaceLength != -1) {
                    System.out.print("Replacement string: ");
                    replaceString = scanner.next();
                }
            }
        }

        document.delete(replaceIdx, replaceLength);
        document.insert(replaceIdx, replaceString);
    }

    @Override
    public void undo() {
        document.delete(replaceIdx, replaceLength);
        document.insert(replaceIdx, replaceString);
    }
}
