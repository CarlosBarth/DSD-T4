package messages;

/**
 * @author Barth
 */
public class MessageAcesso extends MessageBase {
    
    public String getUsername() {
        return this.getInfo(1);
    }
    
    public String getPassword() {
        return this.getInfo(2);
    }
    
}