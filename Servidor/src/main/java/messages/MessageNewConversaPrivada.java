package messages;

/**
 * @author Barth
 */
public class MessageNewConversaPrivada extends MessageBase {
    
    public String getUsername1() {
        return this.getInfo(1);
    }
    
    public String getUsername2() {
        return this.getInfo(2);
    }
    
}