package model;

/**
 * @author Barth
 */
public class Usuario {
    
    private String username;
    private String senha;
    private String nome;
    private String ip;
    private int porta;

    public Usuario(String username, String senha, String nome, String ip, int porta) {
        this.username = username;
        this.senha = senha;
        this.nome = nome;
        this.ip = ip;
        this.porta = porta;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    @Override
    public String toString() {
        return this.username + ";" + this.nome;
    }
    
}
