package message;

/**
 * @author Barth
 */
public class MessageGetUsuarios extends MessageSendBase {

    @Override
    protected String getId() {
        return "getUsuarios";
    }

    @Override
    protected String[] getParams() {
        return null;
    }
    
}