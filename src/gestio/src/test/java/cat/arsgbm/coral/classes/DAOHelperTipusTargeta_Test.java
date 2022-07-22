package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.model.TipusTargeta;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOHelperTipusTargeta_Test {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper helper;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory(Constants.UP_TEST);
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("tearDownClass");
        if(emf.isOpen()) {
            emf.close();
        }

        emf = null;
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
        manager = emf.createEntityManager();
        helper = new DAOHelper(emf, TipusTargeta.class);
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
        if(manager.isOpen()) {
            manager.close();
        }

        helper = null;
        manager = null;
    }

    @Test
    public void testA_insert() {

        TipusTargeta tt = new TipusTargeta();
        tt.setNomTargeta("Local");

        try{
            helper.insert(tt);
        }catch (Exception e) {
            e.printStackTrace();
        }

        TipusTargeta result = (TipusTargeta) helper.getLastItem();
        assertEquals(tt.getId(), result.getId());
        assertEquals(tt.getNomTargeta(), result.getNomTargeta());
    }
}
