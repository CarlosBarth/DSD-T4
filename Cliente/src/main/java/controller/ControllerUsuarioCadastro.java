package controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import message.MessageNovoUsuario;
import model.Usuario;
import utils.Connection;
import utils.MD5;
import view.ViewUsuarioCadastro;

/**
 * @author Barth
 */
public class ControllerUsuarioCadastro extends ControllerPadrao<ViewUsuarioCadastro> {

    private final Usuario usuario;

    public ControllerUsuarioCadastro(String username) {
        this.usuario = new Usuario()
                .setUsername(username);
    }
    
    @Override
    protected ViewUsuarioCadastro getInstanceView() {
        return new ViewUsuarioCadastro();
    }

    @Override
    protected void addActionListeners(ViewUsuarioCadastro view) {
        this.addActionListenerVoltar(view);
        this.addActionListenerCadastrar(view);
    }    

    private void addActionListenerVoltar(ViewUsuarioCadastro view) {
        view.getBtnVoltar().addActionListener((e) -> {
            new ControllerInicio(this.usuario.getUsername()).abreTela();
            this.getView().dispose();
        });
    }

    private void addActionListenerCadastrar(ViewUsuarioCadastro view) {
        view.getBtnCadastrar().addActionListener((e) -> {
            String nome = this.getView().getTxtNomeCompleto().getText();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this.getView(), "Informe seu nome completo", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                String port = this.getView().getTxtPort().getText();

                if (port.isEmpty()) {
                    JOptionPane.showMessageDialog(this.getView(), "Informe a porta disponível para conexão na sua rede", "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    String senha = MD5.md5(this.getView().getTxtSenha().getPassword());

                    if (!senha.equals(MD5.md5(this.getView().getTxtConfirmacaoSenha().getPassword()))) {
                        JOptionPane.showMessageDialog(this.getView(), "As senhas não conferem", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        this.usuario.setNome(nome).setSenha(senha).setPorta(Integer.valueOf(port));
                        
                        if (doCadastroUsuario(this.usuario)) {
                            ControllerMain.getInstance().setUsuarioLogado(this.usuario).abreTela();
                            this.getView().dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(this.getView(), "Houve um erro não esperado", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }
    
    private boolean doCadastroUsuario(Usuario usuario) {
        boolean retorno = false;
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageNovoUsuario messageNewUsuario = new MessageNovoUsuario().setUsuario(usuario);
                socket.getOutputStream().write(messageNewUsuario.getMessageBytes());
                
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