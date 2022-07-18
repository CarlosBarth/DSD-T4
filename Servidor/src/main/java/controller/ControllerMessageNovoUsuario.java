package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MessageNovoUsuario;
import model.Dao;
import model.Usuario;

/**
 * @author Barth
 */
public class ControllerMessageNovoUsuario extends ControllerMessageBase<MessageNovoUsuario> {

    @Override
    public void execute() {
        try {
            Usuario usuario = new Usuario(
                this.getMessageBase().getUsername(), 
                this.getMessageBase().getPassword(), 
                this.getMessageBase().getMomeCompleto(), 
                this.getConnection().getInetAddress().getHostAddress(), 
                Integer.valueOf(this.getMessageBase().getPort()));

            Dao.getInstance().getUsuarios().put(usuario.getUsername(), usuario);
        
            this.write("1");
        } catch (IOException ex) {
            Logger.getLogger(ControllerMessageNovoUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}