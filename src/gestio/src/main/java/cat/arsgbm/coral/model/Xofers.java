package cat.arsgbm.coral.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_xofers")

public class Xofers implements Serializable {

    private static final long serialVersionUID = 3119088680575312729L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    @JoinColumn(name = "id_transportista")
    private Transportistes transportista;

    @Column(name = "nom_xofer")
    private String nomXofer;

    @Column(name = "cognoms_xofer")
    private String cognomXofer;

    @Column(name = "num_intern")
    private String numIntern;

    @Column(name = "correu_xofer")
    private String correuXofer;

    @Column(name = "empremta")
    private boolean empremta;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "carnet_conduir")
    private Date carnetConduir;

    @Column(name = "path_carnetconduir")
    private String pathCarnetConduir;

    @Column(name = "carnet_adr")
    private Date carnetAdr;

    @Column(name = "path_carnetadr")
    private String pathCarnetAdr;

    @Column(name = "carnet_carreter")
    private Date carnetCarreter;

    @Column(name = "path_carnetcarreter")
    private String pathCarnetCarreter;

    @Column(name = "baixa_temporal")
    private Date baixaTemporal;

    @Column(name = "activa_baixa_temporal")
    private boolean activaBaixaTemporal;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Transportistes getTransportista() { return transportista; }

    public void setTransportista(Transportistes transportista) { this.transportista = transportista; }

    public String getNomXofer() { return nomXofer; }

    public void setNomXofer(String nomXofer) { this.nomXofer = nomXofer; }

    public String getCognomXofer() { return cognomXofer; }

    public void setCognomXofer(String cognomXofer) { this.cognomXofer = cognomXofer; }

    public String getNumIntern() { return numIntern; }

    public void setNumIntern(String numIntern) { this.numIntern = numIntern; }

    public String getCorreuXofer() { return correuXofer; }

    public void setCorreuXofer(String correuXofer) { this.correuXofer = correuXofer; }

    public boolean isEmpremta() { return empremta; }

    public void setEmpremta(boolean empremta) { this.empremta = empremta; }

    public String getTelefon() { return telefon; }

    public void setTelefon(String telefon) { this.telefon = telefon; }

    public Date getCarnetConduir() { return carnetConduir; }

    public void setCarnetConduir(Date carnetConduir) { this.carnetConduir = carnetConduir; }

    public String getPathCarnetConduir() { return pathCarnetConduir; }

    public void setPathCarnetConduir(String pathCarnetConduir) { this.pathCarnetConduir = pathCarnetConduir; }

    public Date getCarnetAdr() { return carnetAdr; }

    public void setCarnetAdr(Date carnetAdr) { this.carnetAdr = carnetAdr; }

    public String getPathCarnetAdr() { return pathCarnetAdr; }

    public void setPathCarnetAdr(String pathCarnetAdr) { this.pathCarnetAdr = pathCarnetAdr; }

    public Date getCarnetCarreter() { return carnetCarreter; }

    public void setCarnetCarreter(Date carnetCarreter) { this.carnetCarreter = carnetCarreter; }

    public String getPathCarnetCarreter() { return pathCarnetCarreter; }

    public void setPathCarnetCarreter(String pathCarnetCarreter) { this.pathCarnetCarreter = pathCarnetCarreter; }

    public Date getBaixaTemporal() { return baixaTemporal; }

    public void setBaixaTemporal(Date baixaTemporal) { this.baixaTemporal = baixaTemporal; }

    public boolean isActivaBaixaTemporal() { return activaBaixaTemporal; }

    public void setActivaBaixaTemporal(boolean activaBaixaTemporal) { this.activaBaixaTemporal = activaBaixaTemporal; }

    @Override
    public String toString() {
        return transportista.toString();
    }
}



