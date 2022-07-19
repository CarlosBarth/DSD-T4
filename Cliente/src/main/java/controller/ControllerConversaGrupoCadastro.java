package controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import message.MessageGetUsuarios;
import message.MessageNewConversaGrupo;
import model.Chat;
import model.Usuario;
import utils.Connection;
import view.TableModelUsuarioConversaGrupo;
import view.ViewConversaGrupoCadastro;

/**
 * @author Barth
 */
public class ControllerConversaGrupoCadastro extends ControllerPadrao<ViewConversaGrupoCadastro> {

    @Override
    protected ViewConversaGrupoCadastro getInstanceView() {
        return new ViewConversaGrupoCadastro(this.getUsuarios());
    }

    @Override
    protected void addActionListeners(ViewConversaGrupoCadastro view) {
        this.addSelectionListenerTableUsuario(view);
        this.addActionListenerAdicionar(view);
        this.addActionListenerRemover(view);
        this.addActionListenerIniciarConversa(view);
        this.addActionListenersWindow(view);
    }

    private void addSelectionListenerTableUsuario(ViewConversaGrupoCadastro view) {
        view.getTableUsuarios().getSelectionModel().addListSelectionListener((e) -> {
            this.getView().getBtnRemover().setEnabled(this.getView().getTableUsuarios().getSelectedRowCount() == 1);
        });
    }
    
    private void addActionListenerAdicionar(ViewConversaGrupoCadastro view) {
        view.getBtnAdicionar().addActionListener((e) -> {
            TableModelUsuarioConversaGrupo tableModelUsuarioConversaGrupo = this.getView().getTableModel();
            JComboBox<Usuario> listUsuarios = this.getView().getListUsuarios();
            Usuario usuarioSelecionado = listUsuarios.getItemAt(listUsuarios.getSelectedIndex());
            
            if (!tableModelUsuarioConversaGrupo.getUsuarios().contains(usuarioSelecionado)) {
                tableModelUsuarioConversaGrupo.addUsuario(usuarioSelecionado);
            }
        });
    }

    private void addActionListenerRemover(ViewConversaGrupoCadastro view) {
        view.getBtnRemover().addActionListener((e) -> {
            JTable tableUsuarios = this.getView().getTableUsuarios();
            
            if (tableUsuarios.getSelectedRowCount() == 1) {
                TableModelUsuarioConversaGrupo tableModelUsuarioConversaGrupo = this.getView().getTableModel();
                Usuario usuarioSelecionado = tableModelUsuarioConversaGrupo.getUsuarios().get(tableUsuarios.getSelectedRow());
                
                tableModelUsuarioConversaGrupo.deleteUsuario(usuarioSelecionado);
            }
        });
    }

    private void addActionListenerIniciarConversa(ViewConversaGrupoCadastro view) {
        view.getBtnIniciarConversa().addActionListener((e) -> {
            String titulo = this.getView().getTxtTitulo().getText();
            
            if (titulo.isEmpty()) {
                JOptionPane.showMessageDialog(this.getView(), "Informe um título", "Informação", JOptionPane.ERROR_MESSAGE);
            }
            else {
                ArrayList<Usuario> usuarios = this.getView().getTableModel().getUsuarios();

                if (usuarios.size() < 1) {
                    JOptionPane.showMessageDialog(this.getView(), "Selecione um ou mais usuários para começar a conversa", "Informação", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Chat conversa = this.newConversaGrupo(titulo, usuarios);

                    if (conversa != null) {
                        ControllerMain.getInstance().getView().getTableModel().addConversa(conversa);
                        ControllerMensagem.getInstance().setConversa(conversa).abreTela();
                        this.getView().dispose();
                    }
                }
            }
        });
    }
    
    private void addActionListenersWindow(ViewConversaGrupoCadastro view) {
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

    private Chat newConversaGrupo(String titulo, ArrayList<Usuario> usuarios) {
        Chat conversa = null;
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageNewConversaGrupo messageNewConversaGrupo = new MessageNewConversaGrupo()
                        .setUsername(ControllerMain.getInstance().getUsuarioLogado().getUsername())
                        .setTitulo(titulo)
                        .setUsuarios(usuarios
                                .stream()
                                .map(u -> u.getUsername())
                                .collect(Collectors.toCollection(ArrayList::new)));
                
                socket.getOutputStream().write(messageNewConversaGrupo.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                
                if (response.isEmpty()) {
                    JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao buscar o usuário", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    conversa = new Chat()
                            .setId(response)
                            .setTitulo(titulo);
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return conversa;
    }
    
}