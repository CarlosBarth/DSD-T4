package controller;

import message.MessageSendNotificacaoNewConversa;

/**
 * @author Barth
 */
public class ControllerMessageSendNotificacaoNewConversa extends ControllerMessageReceiveBase<MessageSendNotificacaoNewConversa> {

    @Override
    public void execute() {
        ControllerMain controllerIndex = ControllerMain.getInstance();
        controllerIndex.getView().getTableModel().addConversa(this.getMessageBase().getConversa());
    }

}