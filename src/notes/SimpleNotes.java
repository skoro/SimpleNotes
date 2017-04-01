package notes;

import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;

/**
 * SimpleNotes midlet application.
 *
 * @author skoro
 */
public class SimpleNotes extends MIDlet implements CommandListener {

    private boolean midletPaused = false;
    
    private Vector list;
    
    /**
     * Form elements.
     */
    private List mainForm;
    private Command exitCommand;
    private Command newNoteCommand;
    private Command clearCommand;
    private EditForm editForm;
    
    public SimpleNotes() {
        super();
        list = new Vector();
    }
    
    /**
     * Called when MIDlet is started. Checks whether the MIDlet have been
     * already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            // TODO: resume midlet.
        } else {
            switchDisplayable(null, getMainForm());
        }
        midletPaused = false;
    }
    
    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }
    
    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called to signal the MIDlet to terminate.
     *
     * @param unconditional if true, then the MIDlet has to be unconditionally
     * terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    /**
     * Returns a display instance.
     *
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }
    
    /**
     * Switches a current displayable in a display. The <code>display</code>
     * instance is taken from <code>getDisplay</code> method. This method is
     * used by all actions in the design for switching displayable.
     *
     * @param alert the Alert which is temporarily set to the display; if
     * <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = getDisplay();                                               
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }                                             
    }

    public List getMainForm() {
        if (mainForm == null) {
            mainForm = new List("Simple notes", List.IMPLICIT);
            mainForm.addCommand(getExitCommand());
            mainForm.addCommand(getNewNoteCommand());
            mainForm.addCommand(getClearCommand());
            mainForm.setCommandListener(this);
        }
        return mainForm;
    }
    
    public Command getExitCommand() {
        if (exitCommand == null) {
            exitCommand = new Command("Exit", Command.EXIT, 0);
        }
        return exitCommand;
    }
    
    public Command getNewNoteCommand() {
        if (newNoteCommand == null) {
            newNoteCommand = new Command("New note", Command.SCREEN, 0);
        }
        return newNoteCommand;
    }
    
    public Command getClearCommand() {
        if (clearCommand == null) {
            clearCommand = new Command("Clear", Command.SCREEN, 0);
        }
        return clearCommand;
    }
    
    /**
     * Called by a system to indicated that a command has been invoked on a
     * particular displayable.
     *
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == mainForm) {
            if (command == exitCommand) {
                exitMIDlet();
            }
            else if (command == newNoteCommand) {
                editForm = EditForm.createNew("Add note", this);
                switchDisplayable(null, editForm);
            }
            else if (command == clearCommand) {
                Alert alert = null;
                if (mainForm.size() == 0) {
                    alert = new Alert("No notes", "There are no notes.", null, AlertType.WARNING);
                } else {
                    Confirm confirm = new Confirm("Clear all notes", "Are you sure to clear ALL notes ?");
                    confirm.setConfirmedListener(new Confirm.ConfirmedListener() {
                        public void confirmedAction(Confirm c) {
                            if (c.isConfirmed()) {
                                clearNotes();
                            }
                            // Return to main form after any action.
                            switchDisplayable(null, mainForm);
                        }
                    });
                    alert = confirm.getAlert();
                }
                switchDisplayable(alert, mainForm);
            }
            else if (command == List.SELECT_COMMAND) {
                editNote(mainForm.getSelectedIndex());
            }
        }
        else if (displayable == editForm) {
            Alert alert = null; // Confirm dialog.
            if (command == editForm.getOkCommand()) {
                setNote(editForm.getString(), mainForm.getSelectedIndex());
            }
            else if (command == editForm.getAddCommand()) {
                addNote(editForm.getString());
            }
            else if (command == editForm.getDeleteCommand()) {
                Confirm confirm = new Confirm("Delete note", "Are you sure to delete note ?");
                confirm.setConfirmedListener(new Confirm.ConfirmedListener() {
                    public void confirmedAction(Confirm c) {
                        if (c.isConfirmed()) {
                            deleteNote(mainForm.getSelectedIndex());
                        }
                        // Return to main form after any action.
                        switchDisplayable(null, mainForm);
                    }
                });
                alert = confirm.getAlert();
            }
            switchDisplayable(alert, mainForm);
            editForm = null;
        }
    }
    
    public Note addNote(String text) {
        Note note = null;
        try {
            note = new Note(text);
            list.addElement(note);
            mainForm.append(note.getTitle(), null);
        } catch (EmptyStringException e) {
            // TODO: alert ?
            e.printStackTrace();
        }
        return note;
    }
    
    public void editNote(int index) {
        if (index == -1) {
            return;
        }
        try {
            Note note = (Note) list.elementAt(index);
            editForm = EditForm.createFromNote(note, this);
            switchDisplayable(null, editForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setNote(String text, int index) {
        if (index == -1) {
            return;
        }
        try {
            Note note = (Note) list.elementAt(index);
            note.setText(text);
            note.setTitle("");
            mainForm.set(index, note.getTitle(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteNote(int index) {
        if (index == -1) {
            return;
        }
        try {
            list.removeElementAt(index);
            mainForm.delete(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void clearNotes() {
        try {
            list.removeAllElements();
            mainForm.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
