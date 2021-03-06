package controller.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.FactoryControllerReceiveMessage;
import utils.FactoryReceiveMessage;

/**
 * @author Barth
 */
public class ControllerListenerMessages extends Thread {

    private int port;
    private ServerSocket serverSocket;

    public ControllerListenerMessages(int port) throws IOException {
        this.setPort(port);
    }

    public void setPort(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                try (Socket socket = this.serverSocket.accept()) {
                    byte[] dadosBrutos = new byte[1024];
                    int dadosLidos = socket.getInputStream().read(dadosBrutos);
                    
                    if(dadosLidos >= 0)  {
                        String response = new String(dadosBrutos, 0, dadosLidos);
                        if (!response.isEmpty()) {
                            FactoryControllerReceiveMessage.getControllerClientMessage(
                                    FactoryReceiveMessage.getClientMessage(response),
                                    socket
                            ).execute();
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    JOptionPane.showMessageDialog(null, 
                            "Não foi possível processar a mensagem: ".concat(ex.getMessage()), 
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                Logger.getLogger(ControllerListenerMessages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * RFN4 Sockets ociosos não podem existir
     */
    public void interruptSocket() {
        currentThread().interrupt();
    }
    
}