package message;

/**
 * @author Barth
 */
public class MessageReceiveBase {
    
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