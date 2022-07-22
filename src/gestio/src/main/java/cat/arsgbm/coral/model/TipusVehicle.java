package cat.arsgbm.coral.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_tipusvehicle")
public class TipusVehicle implements Serializable {

    private static final long serialVersionUID = 3119088680575312729L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nom_vehicle")
    private String nomVehicle;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNomVehicle() { return nomVehicle; }

    public void setNomVehicle(String nomVehicle) { this.nomVehicle = nomVehicle; }

    @Override
    public String toString() {
        return  nomVehicle + '\'' ;

    }
}
