package notes;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

/**
 * Simple editor form.
 * 
 * @author skoro
 */
public class EditForm extends TextBox {
    
    public final static int MODE_NEW = 1;
    public final static int MODE_EDIT = 2;
    
    public final static int TEXT_LENGTH = 255;
    
    private int mode;
    
    private Command cancelCommand;
    private Command addCommand;
    private Command deleteCommand;
    private Command okCommand;

    public EditForm(String title, String text, int mode) {
        super(title, text, TEXT_LENGTH, TextField.ANY);
        this.mode = mode;
        switch (mode) {
            case MODE_NEW:
                addCommand(getAddCommand());
                break;
            case MODE_EDIT:
                addCommand(getOkCommand());
                addCommand(getDeleteCommand());
                break;
        }
        addCommand(getCancelCommand());
    }
    
    public static EditForm createNew(String title, CommandListener listener) {
        EditForm form = new EditForm(title, "", EditForm.MODE_NEW);
        form.setCommandListener(listener);
        return form;
    }
    
    public static EditForm createFromNote(Note note, CommandListener listener) {
        EditForm form = new EditForm(note.getTitle(), note.getText(), EditForm.MODE_EDIT);
        form.setCommandListener(listener);
        return form;
    }
    
    public Command getCancelCommand() {
        if (cancelCommand == null) {
            cancelCommand = new Command("Cancel", Command.BACK, 0);
        }
        return cancelCommand;
    }
    
    public Command getAddCommand() {
        if (mode == MODE_NEW && addCommand == null) {
            addCommand = new Command("Add", Command.SCREEN, 0);
        }
        return addCommand;
    }
    
    public Command getOkCommand() {
        if (mode == MODE_EDIT && okCommand == null) {
            okCommand = new Command("OK", Command.SCREEN, 0);
        }
        return okCommand;
    }
    
    public Command getDeleteCommand() {
        if (mode == MODE_EDIT && deleteCommand == null) {
            deleteCommand = new Command("Delete", Command.SCREEN, 0);
        }
        return deleteCommand;
    }
}
