package model;

import java.util.List;

/**
 * @author Barth
 */
public class ConversaGrupo extends Conversa {
    
    private String titulo;
    
    private List<Usuario> usuarios;

    public ConversaGrupo(String id, String nomeConversa, List<Usuario> usuarios, int qtdMensagensNaoLidas) {
        super(id, qtdMensagensNaoLidas);
        this.titulo = nomeConversa;
        this.usuarios = usuarios;
    }
    
    public String getTitulo() {
        return this.titulo;
    }

    @Override
    public List<Usuario> getUsuariosNotificar() {
        return usuarios;
    }
    
}