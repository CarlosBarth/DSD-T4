package messages;

/**
 * @author Barth
 */
public class MessageBase {
    
    private String[] infos;

    public String getInfo(int info) {
        return infos[info];
    }
    
    public String[] getInfos() {
        return infos;
    }

    public void setInfos(String[] infos) {
        this.infos = infos;
    }
    
}