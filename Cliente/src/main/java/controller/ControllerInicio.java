package controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import message.MessageNovoLogin;
import utils.ConfigUtils;
import utils.Connection;
import view.ViewInicio;

/**
 * @author Barth
 */
public class ControllerInicio extends ControllerPadrao<ViewInicio> {

    public ControllerInicio() {
        
    }

    @Override
    public void abreTela() {
        this.setIPPortFromConfig();
        super.abreTela();
    }
    
    public ControllerInicio(String nomeUsuario) {
        this.getView().getTxtNomeUsuario().setText(nomeUsuario);
    }
    
    @Override
    protected ViewInicio getInstanceView() {
        return new ViewInicio();
    }

    @Override
    protected void addActionListeners(ViewInicio view) {
        this.addActionListenerContinuar(view);
         this.addActionListenerAplicar(view);
    }

    private void addActionListenerContinuar(ViewInicio view) {
        view.getBtnContinuar().addActionListener((e) -> {
            String nomeUser = this.getView().getTxtNomeUsuario().getText();
            
            if (nomeUser.isEmpty()) {
                JOptionPane.showMessageDialog(this.getView(), 
                        "Informe um nome de usuário", "Informação", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                if (this.isUsuarioCadastrado(nomeUser)) {
                    new ControllerLogin(nomeUser).abreTela();
                }
                else {
                    new ControllerUsuarioCadastro(nomeUser).abreTela();
                }
                this.getView().dispose();
            }
        });
    }

    private boolean isUsuarioCadastrado(String username) {
        boolean retorno = false;
        
        try {
            try (Socket socket = (new Connection()).getInstanceSocket()) {
                MessageNovoLogin messageLoginInicial = new MessageNovoLogin().setUsername(username);
                socket.getOutputStream().write(messageLoginInicial.getMessageBytes());
                
                InputStream inputStream = socket.getInputStream();
                byte[] dadosBrutos      = new byte[1024];
                String response         = new String(dadosBrutos, 0, inputStream.read(dadosBrutos));
                retorno = response.equals("1");
            }
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Houve um erro ao tentar conectar com o servidor", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return retorno;
    }
    
    private void setIPPortFromConfig() {
        try {
            String config = ConfigUtils.getConfig();
            
            if (config != null && !config.isEmpty()) {
                String[] configs = config.split(":");
                this.getView().getTxtIP().setText(configs[0]);
                this.getView().getTxtPort().setText(configs[1]);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getView(), "Não foi possível ler o arquivo de configurações", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addActionListenerAplicar(ViewInicio view) {
        this.getView().getBtnContinuar().addActionListener((ActionEvent actionEvent) -> {
            try {
                String ip = this.getView().getTxtIP().getText();
                String port = this.getView().getTxtPort().getText();
                
                ConfigUtils.setConfig(ip.concat(":").concat(port));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this.getView(), "Não foi possível ler o arquivo de configurações", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
}