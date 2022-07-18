package controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import message.MessageLoginFinal;
import model.Usuario;
import utils.Connection;
import utils.MD5;
import view.ViewLogin;

/**
 * @author Barth
 */
public class ControllerLogin extends ControllerPadrao<ViewLogin> {

    private final Usuario usuario;

    public ControllerLogin(String username) {
        this.usuario = new Usuario()
                .setUsername(username);
    }
    
    @Override
    protected ViewLogin getInstanceView() {
        return new ViewLogin();
    }

    @Override
    protected void addActionListeners(ViewLogin view) {
        this.addActionListenerVoltar(view);
        this.addActionListenerEntrar(view);
    }

    private void addActionListenerVoltar(ViewLogin view) {
        view.getBtnVoltar().addActionListener((e) -> {
            new ControllerInicio(this.usuario.getUsername()).abreTela();
            this.getView().dispose();
        });
    }

    private void addActionListenerEntrar(ViewLogin view) {
        view.getBtnEntrar().addActionListener((e) -> {
            String senha = MD5.md5(this.getView().getTxtSenha().getPassword());
            
            this.usuario.setSenha(senha);
            
            if (this.doLogin(this.usuario)) {
                ControllerMain.getInstance().setUsuarioLogado(this.usuario).abreTela();
                this.getView().dispose();
            }
            else {
                JOptionPane.showMessageDialog(this.getView(), "Senha incorreta", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    private boolean doLogin(Usuario usuario) {
        boolean retorno = false;
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageLoginFinal messageLoginFinal = new MessageLoginFinal().setUsuario(usuario);
                socket.getOutputStream().write(messageLoginFinal.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos = new byte[1024];
                String response = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                String[] responsePieces = response.split(";");
                retorno = responsePieces[0].equals("1");
                
                if (retorno) {
                    usuario.setNome(responsePieces[1])
                            .setPorta(Integer.valueOf(responsePieces[2]));
                }
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return retorno;
    }
    
}