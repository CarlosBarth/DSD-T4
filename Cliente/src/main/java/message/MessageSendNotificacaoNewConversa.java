package message;

import model.Chat;

/**
 * @author Barth
 */
public class MessageSendNotificacaoNewConversa extends MessageReceiveBase {

    public Chat getConversa() {
        return new Chat()
                .setId(this.getInfo(1))
                .setTitulo(this.getInfo(2));
    }
       
}