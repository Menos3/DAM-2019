package cat.arsgbm.coral.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_transportistes")
public class Transportistes  implements Serializable {
    private static final long serialVersionUID = 3119088680575312729L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "rao_social", length = 30)
    private String raosocial;

    @Column(name = "cif_nif", length = 9)
    private String cif_nif;

    @Column(name = "mail_transp", length = 50)
    private String mail_transp;

    @Column(name = "telefon", length = 9)
    private String telefon;

    @Column(name = "cert_hisenda")
    private Date cert_hisenda;

    @Column(name = "path_certhisenda")
    private String pathCertHisenda;

    @Column(name = "cert_prl")
    private Date cert_prl;

    @Column(name = "path_certprl")
    private String pathCertPrl;

    @Column(name = "asseguranca_merc")
    private Date asseguranca_merc;

    @Column(name = "path_assegurancamerc")
    private String pathAssegurancaMerc;

    @Column(name = "asseguranca_rc")
    private Date asseguranca_rc;

    @Column(name = "path_assegurancarc")
    private String pathAssegurancaRc;

    @Column(name = "trade")
    private boolean trade;

    @Column(name = "baixa_temporal")
    private Date baixatemporal;

    @Column(name = "activa_baixa_temporal")
    private  boolean activaBaixaTemporal;

    @OneToMany(mappedBy = "transportista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Xofers> xoferslist = new ArrayList<>();


    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getRaosocial() { return raosocial; }

    public void setRaosocial(String raosocial) { this.raosocial = raosocial; }

    public String getCif_nif() { return cif_nif; }

    public void setCif_nif(String cif_nif) { this.cif_nif = cif_nif; }

    public String getMail_transp() { return mail_transp; }

    public void setMail_transp(String mail_transp) { this.mail_transp = mail_transp; }

    public String getTelefon() { return telefon; }

    public void setTelefon(String telefon) { this.telefon = telefon; }

    public Date getCert_hisenda() { return cert_hisenda; }

    public void setCert_hisenda(Date cert_hisenda) { this.cert_hisenda = cert_hisenda; }

    public String getPathCertHisenda() { return pathCertHisenda; }

    public void setPathCertHisenda(String pathCertHisenda) { this.pathCertHisenda = pathCertHisenda; }

    public Date getCert_prl() { return cert_prl; }

    public void setCert_prl(Date cert_prl) { this.cert_prl = cert_prl; }

    public String getPathCertPrl() { return pathCertPrl; }

    public void setPathCertPrl(String pathCertPrl) { this.pathCertPrl = pathCertPrl; }

    public Date getAsseguranca_merc() { return asseguranca_merc; }

    public void setAsseguranca_merc(Date asseguranca_merc) { this.asseguranca_merc = asseguranca_merc; }

    public String getPathAssegurancaMerc() { return pathAssegurancaMerc; }

    public void setPathAssegurancaMerc(String pathAssegurancaMerc) { this.pathAssegurancaMerc = pathAssegurancaMerc; }

    public Date getAsseguranca_rc() { return asseguranca_rc; }

    public void setAsseguranca_rc(Date asseguranca_rc) { this.asseguranca_rc = asseguranca_rc; }

    public String getPathAssegurancaRc() { return pathAssegurancaRc; }

    public void setPathAssegurancaRc(String pathAssegurancaRc) { this.pathAssegurancaRc = pathAssegurancaRc; }

    public boolean isTrade() { return trade; }

    public void setTrade(boolean trade) { this.trade = trade; }

    public Date getBaixatemporal() { return baixatemporal; }

    public void setBaixatemporal(Date baixatemporal) { this.baixatemporal = baixatemporal; }

    public boolean isActivaBaixaTemporal() { return activaBaixaTemporal; }

    public void setActivaBaixaTemporal(boolean activaBaixaTemporal) { this.activaBaixaTemporal = activaBaixaTemporal; }

    @Override
    public String toString() {
        return raosocial.toUpperCase();
    }
}
