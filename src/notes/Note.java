package notes;

import java.io.UnsupportedEncodingException;

/**
 * Note item.
 *
 * @author skoro
 */
public class Note extends Model {
    
    public static final int TITLE_MAX = 25;
  
    protected String title;
    protected String text;
    
    public Note(String text) throws EmptyStringException {
        setText(text);
        this.title = createTitleFromText(text);
    }
    
    public Note(String text, String title) throws EmptyStringException {
        setText(text);
        setTitle(title);
    }
    
    /**
     * Create note title from a text.
     * 
     * @param text
     * @return
     * @throws EmptyStringException 
     */
    public String createTitleFromText(String text) throws EmptyStringException {
        text = text.trim();
        if (isEmpty(text)) {
            throw new EmptyStringException();
        }
        int pos = text.indexOf('\n');
        if (pos != -1) {
            return createTitleFromText(text.substring(0, pos));
        }
        if (text.length() < TITLE_MAX) {
            return text;
        }
        return text.substring(0, TITLE_MAX - 1) + "...";
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) throws EmptyStringException {
        if (isEmpty(title)) {
            this.title = createTitleFromText(text);
        } else {
            this.title = title;
        }
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) throws EmptyStringException {
        if (isEmpty(text)) {
            throw new EmptyStringException();
        }
        this.text = text;
    }
    
    protected boolean isEmpty(String s) {
        return s.trim().length() == 0;
    }
    
    public byte[] toBytes() {
        byte[] data;
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            data = text.getBytes();
        }
        return data;
    }
    
    public static Model createFromBytes(byte[] buf) throws EmptyStringException {
        String text;
        try {
            text = new String(buf, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            text = new String(buf);
        }
        return new Note(text);
    }
}
