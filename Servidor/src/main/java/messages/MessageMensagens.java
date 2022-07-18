package messages;

/**
 * @author Barth
 */
public class MessageMensagens extends MessageBase {
    
    public String getConversa() {
        return this.getInfo(1);
    }
    
    public String getUsuario() {
        return this.getInfo(2);
    }
    
}