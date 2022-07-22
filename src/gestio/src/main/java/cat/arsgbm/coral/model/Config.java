package cat.arsgbm.coral.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_config")
public class Config implements Serializable {

    private static final long serialVersionUID = 3119088680575312729L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mail_recep1", length = 50)
    private String mailRecep1;

    @Column(name = "mail_recep2", length = 50)
    private String mailRecep2;

    @Column(name = "mail_resp", length = 50)
    private String mailResp;

    @Column(name = "dir_doc")
    private String dirDoc;

    @Column(name = "missatge_avis")
    private String missatgeAvis;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailRecep1() {
        return mailRecep1;
    }

    public void setMailRecep1(String mailRecep1) {
        this.mailRecep1 = mailRecep1;
    }

    public String getMailRecep2() {
        return mailRecep2;
    }

    public void setMailRecep2(String mailRecep2) {
        this.mailRecep2 = mailRecep2;
    }

    public String getMailResp() {
        return mailResp;
    }

    public void setMailResp(String mailResp) {
        this.mailResp = mailResp;
    }

    public String getDirDoc() {
        return dirDoc;
    }

    public void setDirDoc(String dirDoc) {
        this.dirDoc = dirDoc;
    }

    public String getMissatgeAvis() {
        return missatgeAvis;
    }

    public void setMissatgeAvis(String missatgeAvis) {
        this.missatgeAvis = missatgeAvis;
    }
}