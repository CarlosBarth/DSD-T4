package controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import message.MessageGetUsuarios;
import message.MessageNewConversaPrivada;
import model.Chat;
import model.Usuario;
import utils.Connection;
import view.ViewConversaUsuarioCadastro;

/**
 * @author Barth
 */
public class ControllerConversaUsuarioCadastro extends ControllerPadrao<ViewConversaUsuarioCadastro> {

    @Override
    protected ViewConversaUsuarioCadastro getInstanceView() {
        return new ViewConversaUsuarioCadastro(this.getUsuarios());
    }

    @Override
    protected void addActionListeners(ViewConversaUsuarioCadastro view) {
        this.addActionListenerIniciarConversa(view);
        this.addActionListenersWindow(view);
    }
    
    private void addActionListenerIniciarConversa(ViewConversaUsuarioCadastro view) {
        view.getBtnIniciarConversa().addActionListener((e) -> {
            int selectedIndex = this.getView().getListUsuarios().getSelectedIndex();
            
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(this.getView(), "Selecione um usuário para começar a conversa", "Informação", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Usuario usuario = this.getView().getListUsuarios().getItemAt(selectedIndex);
                
                Chat conversa = this.newConversaPrivada(usuario);
                
                if (conversa != null) {
                    ControllerMain.getInstance().getView().getTableModel().addConversa(conversa);
                    ControllerMensagem.getInstance().setConversa(conversa).abreTela();
                    this.getView().dispose();
                }
            }
        });
    }
    
    private void addActionListenersWindow(ViewConversaUsuarioCadastro view) {
        view.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                ControllerMain.getInstance().abreTela();
                view.dispose();
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

    private Chat newConversaPrivada(Usuario usuario) {
        Chat conversa = null;
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageNewConversaPrivada messageNewConversaPrivada = new MessageNewConversaPrivada()
                        .setUsername1(ControllerMain.getInstance().getUsuarioLogado().getUsername())
                        .setUsername2(usuario.getUsername());
                socket.getOutputStream().write(messageNewConversaPrivada.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                
                if (response.isEmpty()) {
                    JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao buscar o usuário", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                else if (response.equals("0")) {
                    JOptionPane.showMessageDialog(this.getView(), "Vocês já tem uma conversa ativa", "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    conversa = new Chat()
                            .setId(response)
                            .setTitulo(usuario.getNome());
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return conversa;
    }
    
    private ArrayList<Usuario> getUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageGetUsuarios messageGetUsuarios = new MessageGetUsuarios();
                socket.getOutputStream().write(messageGetUsuarios.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                
                if (!response.equals("0")) {
                    String[] responseLines = response.split("\n");
                    
                    for (String line : responseLines) {
                        String[] linePieces = line.split(";");
                        
                        usuarios.add(new Usuario()
                                .setUsername(linePieces[0])
                                .setNome(linePieces[1]));
                    }
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return usuarios
                .stream()
                .filter(u -> !u.getUsername().equals(ControllerMain.getInstance().getUsuarioLogado().getUsername()))
                .collect(Collectors.toCollection(ArrayList::new));
    } 
    
}