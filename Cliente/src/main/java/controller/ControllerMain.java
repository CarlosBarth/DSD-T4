package controller;

import controller.thread.ControllerListenerMessages;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import message.MessageGetConversas;
import model.Conversa;
import model.Usuario;
import utils.Connection;
import view.ViewMain;

/**
 * @author Barth
 */
public class ControllerMain extends ControllerPadrao<ViewMain> {

    static private ControllerMain instance;
    
    private Usuario usuarioLogado;
    private ControllerListenerMessages controllerListenerMessages;
    
    private ControllerMain() {
        
    }
    
    @Override
    protected ViewMain getInstanceView() {
        return new ViewMain();
    }

    @Override
    public void abreTela() {
        try {
            if (this.controllerListenerMessages == null) {
                this.controllerListenerMessages = new ControllerListenerMessages(this.getUsuarioLogado().getPorta());
                this.controllerListenerMessages.start();
            }
        }
        catch (BindException ex) {
            JOptionPane.showMessageDialog(null, "A porta configurada já está em uso nnesta máquina", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível iniciar o notificador de mensagens novas: ".concat(ex.getMessage()), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        super.abreTela();
    }

    public static synchronized ControllerMain getInstance() {
        if (ControllerMain.instance == null) {
            ControllerMain.instance = new ControllerMain();
        }
        return instance;
    }

    @Override
    protected void addActionListeners(ViewMain view) {
        this.addActionListenerLogout(view);
        this.addActionListenerNewConversaPrivada(view);
        this.addActionListenerNewConversaGrupo(view);
        this.addActionListenerAbrirConversa(view);
    }
    
    private void addActionListenerLogout(ViewMain view) {
        view.getBtnLogout().addActionListener((e) -> {
            this.setUsuarioLogado(null);
            // RFN4 Sockets ociosos não podem existir
            this.controllerListenerMessages.interruptSocket();
            new ControllerInicio().abreTela();
            this.getView().dispose();
        });
    }
    
    
    private void addActionListenerNewConversaPrivada(ViewMain view) {
        view.getBtnNewConversaPrivada().addActionListener((e) -> {
            new ControllerConversaUsuarioCadastro().abreTela();
            this.getView().dispose();
        });
    }
    
    private void addActionListenerNewConversaGrupo(ViewMain view) {
        view.getBtnNewConversaGrupo().addActionListener((e) -> {
            new ControllerConversaGrupoCadastro().abreTela();
            this.getView().dispose();
        });
    }
    
    private void addActionListenerAbrirConversa(ViewMain view) {
        view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Conversa conversa = ControllerMain.getInstance().getView().getTableModel().getConversas().get(row);
                    abreConversa(conversa);
                }
            }
        });
    }
    
    private static void abreConversa(Conversa conversa) {
        ControllerMain.getInstance().getView().dispose();
        ControllerMain.getInstance().getView().getTableModel().resetNotificacoesConversa(conversa);
        ControllerMensagem.getInstance().setConversa(conversa).abreTela();
    }
    
    private ArrayList<Conversa> getConversas() {
        ArrayList<Conversa> conversas = new ArrayList<>();
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageGetConversas messageGetConversas = new MessageGetConversas().setUsername(this.getUsuarioLogado().getUsername());
                socket.getOutputStream().write(messageGetConversas.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                
                if (!response.equals("0")) {
                    String[] responseLines = response.split("\n");
                    
                    for (String line : responseLines) {
                        String[] linePieces = line.split(";");
                        
                        conversas.add(new Conversa()
                                .setId(linePieces[0])
                                .setTitulo(linePieces[1])
                                .setMensagensNovas(Integer.parseInt(linePieces[2])));
                    }
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return conversas;
    }
    
    public Usuario getUsuarioLogado() {
        if (this.usuarioLogado == null) {
            this.usuarioLogado = new Usuario();
        }
        return this.usuarioLogado;
    }

    public ControllerMain setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        if (usuarioLogado != null) {
            this.getView().getLabelUsuarioLogado().setText(usuarioLogado.getNome());
            this.getView().getTableModel().clear();
            this.getConversas().forEach(conversa -> this.getView().getTableModel().addConversa(conversa));
        }
        return this;
    }

}