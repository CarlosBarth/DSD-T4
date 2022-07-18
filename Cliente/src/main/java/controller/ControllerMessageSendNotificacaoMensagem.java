package controller;

import message.MessageSendNotificacaoMensagem;
import model.Conversa;

/**
 * @author Barth
 */
public class ControllerMessageSendNotificacaoMensagem extends ControllerMessageReceiveBase<MessageSendNotificacaoMensagem> {

    @Override
    public void execute() {
        ControllerMensagem controllerConversa = ControllerMensagem.getInstance();
        if (controllerConversa.getView().isVisible()
         && controllerConversa.getConversa().getId().equals(this.getMessageBase().getConversa())) {
            controllerConversa.appendMensagem(this.getMessageBase().getMensagem());
        }
        else {
            ControllerMain controllerIndex = ControllerMain.getInstance();
            controllerIndex.getView()
                    .getTableModel()
                    .addNotificacaoMensagemNova(new Conversa()
                            .setId(this.getMessageBase().getConversa()));
        }
    }

}