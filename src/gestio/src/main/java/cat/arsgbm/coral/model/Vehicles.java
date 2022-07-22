package cat.arsgbm.coral.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_vehicles")
public class Vehicles implements Serializable {

    private static final long serialVersionUID = 3119088680575312729L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "id_transportista")
    private Transportistes transportista;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "id_tipusvehicle")
    private TipusVehicle tipusVehicle;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "itv")
    private Date itv;

    @Column(name = "asseguranca_vehicle")
    private Date assegurancaVehicle;

    @Column(name = "path_assegurancavehicle")
    private String pathAssegurancaVehicle;

    @Column(name = "targeta_transport")
    private Date targetaTransport;

    @Column(name = "path_targetatransport")
    private String pathTargetaTransport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "id_tipustargeta")
    private TipusTargeta tipusTargeta;

    @Column(name = "baixa_temporal")
    private Date baixaTemporal;

    @Column(name = "activa_baixa_temporal")
    private boolean activaBaixaTemporal;

    @Column(name = "llogat")
    private boolean llogat;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Transportistes getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportistes transportista) {
        this.transportista = transportista;
    }

    public TipusVehicle getTipusVehicle() {
        return tipusVehicle;
    }

    public void setTipusVehicle(TipusVehicle tipusVehicle) {
        this.tipusVehicle = tipusVehicle;
    }

    public TipusTargeta getTipusTargeta() {
        return tipusTargeta;
    }

    public void setTipusTargeta(TipusTargeta tipusTargeta) {
        this.tipusTargeta = tipusTargeta;
    }

    public String getMatricula() { return matricula; }

    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Date getItv() { return itv; }

    public void setItv(Date itv) { this.itv = itv; }

    public Date getAssegurancaVehicle() { return assegurancaVehicle; }

    public void setAssegurancaVehicle(Date assegurancaVehicle) { this.assegurancaVehicle = assegurancaVehicle; }

    public String getPathAssegurancaVehicle() { return pathAssegurancaVehicle; }

    public void setPathAssegurancaVehicle(String pathAssegurancaVehicle) { this.pathAssegurancaVehicle = pathAssegurancaVehicle; }

    public Date getTargetaTransport() { return targetaTransport; }

    public void setTargetaTransport(Date targetaTransport) { this.targetaTransport = targetaTransport; }

    public String getPathTargetaTransport() { return pathTargetaTransport; }

    public void setPathTargetaTransport(String pathTargetaTransport) { this.pathTargetaTransport = pathTargetaTransport; }

    public Date getBaixaTemporal() { return baixaTemporal; }

    public void setBaixaTemporal(Date baixaTemporal) { this.baixaTemporal = baixaTemporal; }

    public boolean isActivaBaixaTemporal() { return activaBaixaTemporal; }

    public void setActivaBaixaTemporal(boolean activaBaixaTemporal) { this.activaBaixaTemporal = activaBaixaTemporal; }

    public boolean isLlogat() { return llogat; }

    public void setLlogat(boolean llogat) { this.llogat = llogat; }


}
