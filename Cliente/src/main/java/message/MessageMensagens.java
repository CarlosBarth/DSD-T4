package message;

/**
 * @author Barth
 */
public class MessageMensagens extends MessageSendBase {

    private String conversa;
    private String usuario;

    public String getConversa() {
        return conversa;
    }

    public MessageMensagens setConversa(String conversa) {
        this.conversa = conversa;
        return this;
    }

    public String getUsuario() {
        return usuario;
    }

    public MessageMensagens setUsuario(String usuario) {
        this.usuario = usuario;
        return this;
    }
    
    @Override
    protected String getId() {
        return "Mensagens";
    }

    @Override
    protected String[] getParams() {
        return new String[]{
            this.getConversa(),
            this.getUsuario()
        };
    }

}