import java.util.Stack;

public class UndoRedoManager implements ICommand {
    private Stack<ICommand> undoStack;
    private Stack<ICommand> redoStack;

    // When you undo a command, pop off top of undo stack, call undo, then push it on the redo stack
    @Override
    public void undo() {
        ICommand cmdToUndo = undoStack.pop();
        cmdToUndo.undo();
        redoStack.push(cmdToUndo);
    }

    // When you redo a command, pop off redo stack, call do, push to undo stack
    @Override
    public void redo() {
        ICommand cmdToRedo = redoStack.pop();
        cmdToRedo.redo();
        undoStack.push(cmdToRedo);
    }

    // When you execute a command, add to undo stack (C1.do()) and clear the redo stack
    public void execute(ICommand c) {
        undoStack.add(c);
        c.redo();
        redoStack.clear();
    }
}
