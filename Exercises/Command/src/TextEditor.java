import java.util.Scanner;

class TextEditor {

    private IDocument _document;

    TextEditor(IDocument document) {
        _document = document;
    }

    void run() {
        while (true) {
            printOptions();

            Scanner scanner = new Scanner(System.in);
            String optionInput = scanner.next();
            int option = Utils.validateNumberInput(optionInput);
            UndoRedoManager manager = new UndoRedoManager();

            if (option != -1) {
                switch (option) {
                    case 1:
                        manager.execute(new Insert(_document));
                        break;
                    case 2:
                        manager.execute(new Delete(_document));
                        break;
                    case 3:
                        manager.execute(new Replace(_document));
                        break;
                    case 4:
                        _document.display();
                        break;
                    case 5:
                        manager.execute(new Save(_document));
                        break;
                    case 6:
                        manager.execute(new Open(_document));
                        break;
                    case 7:
                        _document.clear();
                        break;
                    case 8:
                        System.out.println("Undo");
                        break;
                    case 9:
                        System.out.println("Redo");
                        break;
                    case 10:
                        return;
                }
            }

            System.out.println();
        }
    }

    private void printOptions() {
        System.out.println("SELECT AN OPTION (1 - 10):");
        System.out.println("1. Insert a string at a specified index in the document");
        System.out.println("2. Delete a sequence of characters at a specified index");
        System.out.println("3. Replace a sequence of characters at a specified index with a new string");
        System.out.println("4. Display the current contents of the document");
        System.out.println("5. Save the document to a file");
        System.out.println("6. Open a document from a file");
        System.out.println("7. Start a new, empty document");
        System.out.println("8. Undo");
        System.out.println("9. Redo");
        System.out.println("10. Quit");

        System.out.println();
        System.out.print("Your selection: ");
    }
}
