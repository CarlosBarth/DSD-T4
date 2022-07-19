package controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JOptionPane;
import message.MessageGetMensagens;
import message.MessageSendMensagem;
import model.Chat;
import model.Mensagem;
import model.Usuario;
import utils.Connection;
import view.ViewMensagem;

/**
 * @author Barth
 */
public class ControllerMensagem extends ControllerPadrao<ViewMensagem>{

    static private ControllerMensagem instance;
    
    private Chat conversa;
    
    private ControllerMensagem() {
        
    }

    public static synchronized ControllerMensagem getInstance() {
        if (ControllerMensagem.instance == null) {
            ControllerMensagem.instance = new ControllerMensagem();
        }
        return ControllerMensagem.instance;
    }
    
    @Override
    protected ViewMensagem getInstanceView() {
        return new ViewMensagem();
    }

    @Override
    public void abreTela() {
        this.getView().getTxtHistorico().setText(this.getMensagens(this.getConversa()));
        this.getView().setTitle(conversa.getTitulo());
        super.abreTela();
    }

    @Override
    protected void addActionListeners(ViewMensagem view) {
        this.addActionListenerEnviar(view);
        this.addActionListenersWindow(view);
    }
    
    public Chat getConversa() {
        return conversa;
    }

    public ControllerMensagem setConversa(Chat conversa) {
        this.conversa = conversa;
        return this;
    }

    private void addActionListenerEnviar(ViewMensagem view) {
        view.getBtnEnviar().addActionListener((e) -> {
            String texto = this.getView().getTxtMensagem().getText();
            
            if (texto.isEmpty()) {
                JOptionPane.showMessageDialog(this.getView(), "Digite uma mensagem", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                Mensagem mensagem = new Mensagem()
                        .setUsuario(ControllerMain.getInstance().getUsuarioLogado())
                        .setDataHora(new Date())
                        .setMensagem(texto);
                
                if (this.sendMessage(mensagem, this.getConversa())) {
                    this.appendMensagem(mensagem);
                    this.getView().getTxtMensagem().setText("");
                }
                else {
                    JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar enviar sua mensagem", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    public void appendMensagem(Mensagem mensagem) {
        if (!this.getView().getTxtHistorico().getText().isEmpty()) {
            this.getView().getTxtHistorico().append("\n\n");
        }
        this.getView().getTxtHistorico().append(mensagem.toString());
    }

    private void addActionListenersWindow(ViewMensagem view) {
        view.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                ControllerMain.getInstance().abreTela();
                ControllerMensagem.getInstance().getView().dispose();
            }

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    private String getMensagens(Chat conversa) {
        ArrayList<String> mensagens = new ArrayList<>();
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageGetMensagens messageGetMensagens = new MessageGetMensagens()
                        .setConversa(conversa.getId())
                        .setUsuario(ControllerMain.getInstance().getUsuarioLogado().getUsername());
                socket.getOutputStream().write(messageGetMensagens.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                
                if (!response.equals("0")) {
                    String[] responseLines = response.split("\r\n");
                    
                    for (String line : responseLines) {
                        String[] linePieces = line.split(";");
                        
                        mensagens.add(new Mensagem()
                                .setDataHora(linePieces[1])
                                .setUsuario(new Usuario()
                                        .setNome(linePieces[0]))
                                .setMensagem(new ArrayList<>(Arrays.asList(linePieces))
                                        .subList(2, linePieces.length)
                                        .stream()
                                        .reduce((String t, String u) -> {
                                            return t == null ? u : (u == null ? t : t.concat(";").concat(u));
                                        })
                                        .get())
                                .toString());
                    }
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return String.join("\n\n", mensagens);
    }

    private boolean sendMessage(Mensagem mensagem, Chat conversa) {
        boolean retorno = false;
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageSendMensagem messageSendMensagem = new MessageSendMensagem()
                        .setConversa(conversa.getId())
                        .setMensagem(mensagem);
                socket.getOutputStream().write(messageSendMensagem.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                retorno = response.equals("1");
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return retorno;
    }
}