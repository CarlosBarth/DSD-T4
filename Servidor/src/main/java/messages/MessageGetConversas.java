package messages;

/**
 * @author Barth
 */
public class MessageGetConversas extends MessageBase {
    
    public String getUsername() {
        return this.getInfo(1);
    }
    
}