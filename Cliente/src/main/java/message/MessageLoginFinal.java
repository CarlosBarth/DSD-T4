package message;

import model.Usuario;

/**
 * @author Barth
 */
public class MessageLoginFinal extends MessageSendBase {

    private Usuario usuario;

    public Usuario getUsuario() {
        if (this.usuario == null) {
            this.usuario = new Usuario();
        }
        return this.usuario;
    }

    public MessageLoginFinal setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }
    
    @Override
    protected String getId() {
        return "acesso";
    }

    @Override
    protected String[] getParams() {
        return new String[]{
            this.getUsuario().getUsername(),
            this.getUsuario().getSenha()
        };
    }

}