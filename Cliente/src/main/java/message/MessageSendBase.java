package message;

/**
 * @author Barth
 */
abstract public class MessageSendBase {

    abstract protected String getId();
    
    abstract protected String[] getParams();
    
    public byte[] getMessageBytes() {
        String joins;
        
        try {
            joins = String.join(";", this.getParams());
        }
        catch (NullPointerException exception) {
            joins = "";
        }
        
        if (!joins.isEmpty()) {
            joins = ";".concat(joins);
        }
        
        return this.getId().concat(joins).getBytes();
    }
    
}