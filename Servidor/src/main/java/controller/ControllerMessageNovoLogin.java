package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MessageNovoLogin;
import model.Dao;
import model.Usuario;

/**
 * @author Barth
 */
public class ControllerMessageNovoLogin extends ControllerMessageBase<MessageNovoLogin> {
    
    @Override
    public void execute() {
        try {
            String username = this.getMessageBase().getUsername();
            Usuario usuario = Dao.getInstance().getUsuarios().get(username);
            if (usuario != null) {
                this.write("1");
            } else {
                this.write("0");
            }
        } catch (IOException ex) {
            Logger.getLogger(ControllerMessageNovoLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
