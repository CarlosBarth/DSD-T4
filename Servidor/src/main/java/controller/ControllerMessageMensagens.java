package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MessageMensagens;
import model.Conversa;
import model.Dao;
import model.Mensagem;

/**
 * @author Barth
 */
public class ControllerMessageMensagens extends ControllerMessageBase<MessageMensagens> {

    @Override
    public void execute() {
        try {
            Dao dao = Dao.getInstance();
            Conversa conversa = dao.getConversas().get(this.getMessageBase().getConversa());
            String retorno = "";
            if(conversa != null) {
                conversa.getNotificacoes().remove(this.getMessageBase().getUsuario());
                for(Mensagem mensagem : conversa.getMensagens()) {
                    retorno += (mensagem.toString() + "\r\n");
                }
                if(!retorno.isEmpty()) {
                    this.write(retorno);
                } else {
                    this.write("0");
                }
            } else {
                this.write("0");
            }
        } catch (IOException ex) {
            Logger.getLogger(ControllerMessageMensagens.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}