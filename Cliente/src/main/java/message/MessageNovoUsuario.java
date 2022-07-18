package message;

import model.Usuario;

/**
 * @author Barth
 */
public class MessageNovoUsuario extends MessageSendBase {

    private Usuario usuario;

    public Usuario getUsuario() {
        if (this.usuario == null) {
            this.usuario = new Usuario();
        }
        return this.usuario;
    }

    public MessageNovoUsuario setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }
    
    @Override
    protected String getId() {
        return "novoUsuario";
    }

    @Override
    protected String[] getParams() {
        return new String[]{
            this.getUsuario().getUsername(),
            this.getUsuario().getNome(),
            this.getUsuario().getSenha(),
            String.valueOf(this.getUsuario().getPorta())
        };
    }

}