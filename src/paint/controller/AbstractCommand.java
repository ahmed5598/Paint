/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint.controller;

public abstract class AbstractCommand implements Command{
    Controller controller = Controller.getInstance();

    @Override
    public void undo()
    {
        controller.getView().getRedoButton().setEnabled(true);
    }

    @Override
    public  void execute()
    {
        controller.getView().getUndoButton().setEnabled(true);
    }
    
    
}
