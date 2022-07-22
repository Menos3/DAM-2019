package cat.arsgbm.coral.classes;

public class AppConfigSingleton {

    private static AppConfigSingleton instancia;
    private String mailRecep1;
    private String mailRecep2;
    private String mailResp;
    private String dirDoc;
    private String missatgeAvis;

    /**
     * @return la instancia creada
     *
     * Metode per crear instancies de la classe AppConfigSingleton
     * Si la instancia a crear es diferent de null, es crea una nova instancia de la classe AppConfigSingleton
     *
     */
    public static AppConfigSingleton getInstancia() {
        if(instancia == null)
            instancia = new AppConfigSingleton();
        return instancia;
    }

    private AppConfigSingleton() {

    }

    public String getMailRecep1() { return mailRecep1; }

    public void setMailRecep1(String mailRecep1) { this.mailRecep1 = mailRecep1; }

    public String getMailRecep2() { return mailRecep2; }

    public void setMailRecep2(String mailRecep2) { this.mailRecep2 = mailRecep2; }

    public String getMailResp() { return mailResp; }

    public void setMailResp(String mailResp) { this.mailResp = mailResp; }

    public String getDirDoc() { return dirDoc; }

    public void setDirDoc(String dirDoc) { this.dirDoc = dirDoc; }

    public String getMissatgeAvis() { return missatgeAvis; }

    public void setMissatgeAvis(String missatgeAvis) { this.missatgeAvis = missatgeAvis; }

    @Override
    public String toString() {
        return "AppConfigSingleton{" +
                "mailRecep1='" + mailRecep1 + '\'' +
                ", mailRecep2='" + mailRecep2 + '\'' +
                ", mailResp='" + mailResp + '\'' +
                ", dirDoc='" + dirDoc + '\'' +
                ", missatgeAvis='" + missatgeAvis + '\'' +
                '}';
    }
}
