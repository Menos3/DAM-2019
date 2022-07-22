package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.model.Config;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOHelperTest {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper helper;
    static final Config c = new Config();

    @BeforeClass
    public static void setUpClass() {

        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory(Constants.UP_TEST);
        //emf = Persistence.createEntityManagerFactory("local");
        c.setId(1);
    }

    @AfterClass
    public static void tearDownClass() {

        System.out.println("tearDownClass");
        if (emf.isOpen()) {
            emf.close();
        }

        emf = null;
    }

    @Before
    public void setUp() {

        System.out.println("setUp");
        manager = emf.createEntityManager();
        helper = new DAOHelper(emf, Config.class);
    }

    @After
    public void tearDown() {

        System.out.println("tearDown");
        if (manager.isOpen()) {

            manager.close();
        }

        helper = null;
        manager = null;
    }

    @Test
    public void testA_insert() {

        Config conf = new Config();
        conf.setDirDoc("/sergio/local/jajja");
        conf.setMailRecep1("email11@example.com");
        conf.setMailRecep2("email22@example.com");
        conf.setMailResp("email33@example.com");
        conf.setMissatgeAvis("Prova insert");

        try {
            helper.insert(conf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Config result = (Config) helper.getLastItem();
        assertEquals(conf.getId(), result.getId());
        assertEquals(conf.getDirDoc(), result.getDirDoc());
        assertEquals(conf.getMailRecep1(),result.getMailRecep1());
        assertEquals(conf.getMailRecep2(),result.getMailRecep2());
        assertEquals(conf.getMailResp(),result.getMailResp());
        assertEquals(conf.getMissatgeAvis(),result.getMissatgeAvis());

    }

    @Test
    public void testB_update() {

        c.setDirDoc("/usr/local/bin");
        c.setMailRecep1("mail1@example.com");
        c.setMailRecep2("mail2@example.com");
        c.setMailResp("mail3@example.com");
        c.setMissatgeAvis("Prova tests");
        helper.update(c);
        assertEquals(c.getDirDoc(),"/usr/local/bin");
        assertEquals(c.getMailRecep1(), "mail1@example.com");
        assertEquals(c.getMailRecep2(), "mail2@example.com");
        assertEquals(c.getMailResp(), "mail3@example.com");
        assertEquals(c.getMissatgeAvis(),"Prova tests");

    }

    @Test
    public void testC_getAll() {

        List listac = helper.getAll();
        listac.forEach(System.out::println);
    }
}