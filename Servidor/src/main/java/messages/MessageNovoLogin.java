package messages;

/**
 * @author Barth
 */
public class MessageNovoLogin extends MessageBase {
    
    public String getUsername() {
        return this.getInfo(1);
    }
    
}