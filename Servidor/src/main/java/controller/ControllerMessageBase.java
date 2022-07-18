package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import messages.MessageBase;

/**
 * @author Barth
 * @param <TypeMessage>
 */
abstract public class ControllerMessageBase<TypeMessage extends MessageBase> {
    
    private OutputStream    out;
    private InputStream     in;
    private Socket          connection;
    private TypeMessage message;
    
    public abstract void execute();
    
    public void setMessageBase(TypeMessage message) {
        this.message = message;
    }
    
    public TypeMessage getMessageBase() {
        return this.message;
    }
    
    public InputStream getInput() {
        return in;
    }

    public OutputStream getOutput() {
        return out;
    }
    

    public Socket getConnection() {
        return connection;
    }

    public void setConnection(Socket connection) throws IOException {
        this.connection = connection;
        this.in = connection.getInputStream();
        this.out = connection.getOutputStream();
    }

    
    public void write(String retorno) throws IOException {
        ControllerApp.getInstance().getInstanceView().addLog(retorno);
        this.out.write(retorno.getBytes());
    }
    
}