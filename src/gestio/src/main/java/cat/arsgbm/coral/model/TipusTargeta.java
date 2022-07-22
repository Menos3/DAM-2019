package cat.arsgbm.coral.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_tipustargeta")
public class TipusTargeta implements Serializable {

    private static final long serialVersionUID = 3119088680575312729L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nom_targeta")
    private String nomTargeta;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getNomTargeta() { return nomTargeta; }

    public void setNomTargeta(String nomTargeta) { this.nomTargeta = nomTargeta; }

    @Override
    public String toString() {
        return
                nomTargeta + '\'';
    }
}
