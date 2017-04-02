package notes;

/**
 * Base model class.
 *
 * @author skoro
 */
public abstract class Model {
    
    protected int id = 0;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isNew() {
        return id == 0;
    }
    
    /**
     * Serialize model to bytes presentation.
     *
     * @return byte[]
     */
    abstract public byte[] toBytes();
    
    /**
     * Create model from bytes data.
     *
     * @param buf
     * @return
     * @throws Exception 
     */
    public static Model createFromBytes(byte[] buf) throws Exception {
        return null;
    }
}
