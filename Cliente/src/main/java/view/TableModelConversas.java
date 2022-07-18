package view;

import java.util.ArrayList;
import java.util.stream.IntStream;
import javax.swing.table.AbstractTableModel;
import model.Conversa;

/**
 * @author Barth
 */
public class TableModelConversas extends AbstractTableModel {

    private final ArrayList<Conversa> conversas;

    public TableModelConversas() {
        this.conversas = new ArrayList<>();
    }
    
    public TableModelConversas(ArrayList<Conversa> conversas) {
        this.conversas = conversas;
    }

    public ArrayList<Conversa> getConversas() {
        return conversas;
    }

    public void addConversa(Conversa conversa) {
        this.getConversas().add(conversa);
        int i = this.getConversas().indexOf(conversa);
        this.fireTableRowsInserted(i, i);
    }
    
    public void addNotificacaoMensagemNova(Conversa conversa) {
        int index = this.getIndexOf(conversa);

        if (index >= 0) {
            this.getConversas().get(index).setMensagensNovas(this.getConversas().get(index).getMensagensNovas() + 1);
            this.fireTableRowsUpdated(index, index);
        }
    }
    
    public void resetNotificacoesConversa(Conversa conversa) {
        int index = this.getIndexOf(conversa);

        if (index >= 0) {
            this.getConversas().get(index).setMensagensNovas(0);
            this.fireTableRowsUpdated(index, index);
        }
    }
    
    private int getIndexOf(Conversa conversa) {
        return IntStream.range(0, this.getRowCount())
                .filter(i -> this.getConversas().get(i).getId().equals(conversa.getId()))
                .findFirst()
                .getAsInt();
    }
    
    public void clear() {
        int i = this.getConversas().size();
        if (i > 0) {
            this.getConversas().clear();
            this.fireTableRowsDeleted(0, i - 1);
        }
    }
    
    @Override
    public int getRowCount() {
        return this.getConversas().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Conversa conversa = this.getConversas().get(rowIndex);
        switch (columnIndex) {
            case 0: return conversa.getTitulo();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Conversa";
            default: return null;
        }
    }
    
}