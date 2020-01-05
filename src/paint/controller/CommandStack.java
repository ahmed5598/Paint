package paint.controller;

import java.util.Stack;

public class CommandStack {
    private static final CommandStack firstInstance = new CommandStack();

    private Stack<Command> recentCommands;
    private Stack<Command> undoneCommands;
    
    public void addCommand(Command command)
    {
        undoneCommands.clear();
        recentCommands.push(command);
    }
    
    public void undoLastCommand()
    {
        if (recentCommands.isEmpty())
        {
            return;
        }
        Command command = recentCommands.pop();
        command.undo();
        
        undoneCommands.push(command);
    }
    
    public void redo()
    {
        Command command = undoneCommands.pop();
        command.execute();
        recentCommands.push(command);
    }
    
    public boolean recentCommandsIsEmpty()
    {
        return recentCommands.isEmpty();
    }
    
    public boolean undoneCommandsIsEmpty()
    {
        return undoneCommands.isEmpty();
    }
    
    private CommandStack() {
        recentCommands = new Stack<>();
        undoneCommands = new Stack<>();
    }
    
    public static CommandStack getInstance()
    {
        return firstInstance;
    }

    public Stack<Command> getRecentCommands() {
        return recentCommands;
    }

    public void setRecentCommands(Stack<Command> recentCommands) {
        this.recentCommands = recentCommands;
    }

    public Stack<Command> getUndoneCommands() {
        return undoneCommands;
    }

    public void setUndoneCommands(Stack<Command> undoneCommands) {
        this.undoneCommands = undoneCommands;
    }
}
