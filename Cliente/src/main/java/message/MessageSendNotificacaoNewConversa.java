package message;

import model.Conversa;

/**
 * @author Barth
 */
public class MessageSendNotificacaoNewConversa extends MessageReceiveBase {

    public Conversa getConversa() {
        return new Conversa()
                .setId(this.getInfo(1))
                .setTitulo(this.getInfo(2));
    }
       
}