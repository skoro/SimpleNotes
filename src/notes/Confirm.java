package notes;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * Confirm dialog.
 *
 * @author skoro
 */
public class Confirm implements CommandListener {
  
    private Alert dialog;
    private boolean confirmed = false;
    private ConfirmedListener listener;
    
    interface ConfirmedListener {
        public void confirmedAction(Confirm c);
    }
    
    public Confirm(String title, String text) {
        dialog = new Alert(title, text, null, AlertType.CONFIRMATION);
        dialog.addCommand(new Command("Yes", Command.OK, 0));
        dialog.addCommand(new Command("No", Command.CANCEL, 0));
        dialog.setTimeout(Alert.FOREVER);
        dialog.setCommandListener(this);
    }
    
    public Alert getAlert() {
        return dialog;
    }
    
    public void commandAction(Command c, Displayable d) {
        if (d == dialog) {
            confirmed = c.getCommandType() == Command.OK;
            if (listener != null) {
                listener.confirmedAction(this);
            }
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public void setConfirmedListener(ConfirmedListener listener) {
        this.listener = listener;
    }
}
