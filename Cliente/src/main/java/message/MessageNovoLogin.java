package message;

/**
 * @author Barth
 */
public class MessageNovoLogin extends MessageSendBase {

    private String username;

    public String getUsername() {
        return username;
    }

    public MessageNovoLogin setUsername(String username) {
        this.username = username;
        return this;
    }
    
    @Override
    protected String getId() {
        return "novoLogin";
    }

    @Override
    protected String[] getParams() {
        return new String[]{
            this.getUsername()
        };
    }

}