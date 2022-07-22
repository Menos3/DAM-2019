package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.exceptions.ClauDuplicadaException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DAOHelper<T> {
    private EntityManagerFactory emf;
    final Class<T> parameterClass;

    public DAOHelper(EntityManagerFactory emf, Class<T> parameterClass) {
        this.emf = emf;
        this.parameterClass = parameterClass;
    }

    /**
     * @param t /Parametre que s'ha d'insertar a la base de dades
     * @throws Exception
     *
     * Metode per fer INSERTS a una base de dades
     * Es rep el parametre a insertar, es crea una connexio a la base de dades, es fa el INSERT i despres, es tenca la connexio per alliberar recursos
     * Es controla la excepcio de ConstraintViolationException perque hi ha un camp a la base de dades que es UNIQUE
     *
     */
    public void insert(T t) throws Exception {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new ClauDuplicadaException(Constants.MSG_ADV_CAMP_DUPLICAT_BUIT);
            } else
                throw new Exception("");
        } finally {
            em.close();
        }
    }

    /**
     * @param t /Parametre al que se li ha de fer un UPDATE a la base de dades
     *
     * Metode per fer UPDATES a la base de dades
     * Es rep el parametre a actualitzar, es crea una connexio a la base de dades, es fa el UPDATE i despres, es tenca la connexio per alliberar recursos
     *
     */
    public void update(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {

            em.merge(t);
            em.getTransaction().commit();

        } catch (Exception e) {

            em.getTransaction().rollback();

        } finally {

            em.close();
        }
    }

    /**
     * @param id /Parametre que s'agafa com a referencia per fer el DELETE
     *
     * Metode per fer DELETES en una base de dades
     * Es rep la id del registre a eliminar, es crea una connexio a la base de dades, es fa el DELETE i despres, es tenca la connexio per alliberar recursos
     *
     */
    public void delete(Integer id) {

        EntityManager em = emf.createEntityManager();
        T t = em.find(parameterClass, id);

        try {

            if (t != null) {

                em.getTransaction().begin();
                em.remove(t);
                em.getTransaction().commit();
            }

        } catch (Exception e) {

            em.getTransaction().rollback();

        } finally {

            em.close();
        }
    }

    public T read(Integer id) {

        EntityManager em = emf.createEntityManager();

        try {

            return em.find(parameterClass, id);

        } finally {

            em.close();
        }
    }

    /**
     * @return l'ultim registre d'una taula o d'una llista
     *
     * Metode per buscar i mostrar l'ultim registre d'una taula o d'una llista
     * Es crea una connexio a la base de dades i un criteri de cerca, s'aconsegueix la informacio que es busca i es tenca al connexio per alliberar recursos
     *
     */
    public T getLastItem() {

        CriteriaBuilder cb = this.emf.getCriteriaBuilder();
        EntityManager em = this.emf.createEntityManager();
        List<T> llista;

        try {

            CriteriaQuery<T> cbQuery = cb.createQuery(parameterClass);
            Root<T> c = cbQuery.from(parameterClass);
            cbQuery.select(c);

            Query query = em.createQuery(cbQuery);
            llista = query.getResultList();

        } finally {

            em.close();
        }

        return (!llista.isEmpty()) ? llista.get(llista.size() - 1) : null;
    }

    /**
     * @return la llista amb tots els registres d'una taula
     *
     * Metode per aconseguir tots els registres d'una taula i guardar-los en una llista
     * Es crea la connexio a la base de dades i el criteri de cerca, s'obte el resultat i es guarda en una llista
     *
     */
    public List<T> getAll() {

        CriteriaBuilder cb = this.emf.getCriteriaBuilder();
        EntityManager manager = this.emf.createEntityManager();

        CriteriaQuery<T> cbQuery = cb.createQuery(parameterClass);
        Root<T> c = cbQuery.from(parameterClass);
        cbQuery.select(c);

        Query query = manager.createQuery(cbQuery);

        return query.getResultList();
    }

    /**
     * @return la llista amb tots els registres d'una taula per a un formulari de cerca o de baixa
     *
     * Metode per aconseguir tots els registres d'una taula i guardar-los en una ObservableList
     * Es crea la connexio a la base de dades i el criteri de cerca, s'obte el resultat, es guarda en una llista i es converteix a una ObservableList
     *
     */
    public ObservableList<T> getAllToObservableList() {

        CriteriaBuilder cb = this.emf.getCriteriaBuilder();
        EntityManager manager = this.emf.createEntityManager();

        CriteriaQuery<T> cbQuery = cb.createQuery(parameterClass);
        Root<T> c = cbQuery.from(parameterClass);
        cbQuery.select(c);

        Query query = manager.createQuery(cbQuery);

        return FXCollections.observableList(query.getResultList());
    }

    /**
     * Metode per imprimir per pantalla el resultat d'una llista de registres d'una taula
     *
     */
    public void printAll() {

        List<T> list = getAll();
        list.forEach(System.out::println);
    }

}
