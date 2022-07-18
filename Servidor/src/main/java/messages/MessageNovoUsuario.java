package messages;

/**
 * @author Barth
 */
public class MessageNovoUsuario extends MessageBase {
    
    public String getUsername() {
        return this.getInfo(1);
    }
    
    public String getMomeCompleto() {
        return this.getInfo(2);
    }
    
    public String getPassword() {
        return this.getInfo(3);
    }
    
    public String getPort() {
        return this.getInfo(4);
    }
    
}