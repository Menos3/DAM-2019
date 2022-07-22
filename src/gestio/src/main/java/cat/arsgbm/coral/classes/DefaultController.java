package cat.arsgbm.coral.classes;

import javax.persistence.EntityManagerFactory;

public abstract class DefaultController {
//AQUÍ CREEM UNA CONEXIÓ AMB LA BBDD
    private EntityManagerFactory emf;

    public  EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

}
