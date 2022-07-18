package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MessageAcesso;
import model.Dao;
import model.Usuario;

/**
 * @author Barth
 */
public class ControllerMessageAcesso extends ControllerMessageBase<MessageAcesso> {

    
    @Override
    public void execute() {
        try {
            Usuario usuario = Dao.getInstance().getUsuarios().get(this.getMessageBase().getUsername());
            if(this.getMessageBase().getPassword().equals(usuario.getSenha())) {
                this.write("1;" + usuario.getNome() + ";" + usuario.getPorta());
            } else {
                this.write("0");
            }
        } catch (IOException ex) {
            Logger.getLogger(ControllerMessageAcesso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}