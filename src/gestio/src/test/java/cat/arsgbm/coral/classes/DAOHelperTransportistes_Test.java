package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.model.Transportistes;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOHelperTransportistes_Test {
    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper helper;
    static Transportistes transportista = new Transportistes();

    @BeforeClass
    public static void setUpClass() {

        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory("test");

        LocalDate dataTmp = LocalDate.of(2019,1,27);
        Date data = Utilitats.localDateToDate(dataTmp);

        transportista.setRaosocial("paco");
        transportista.setCif_nif("12345678A");
        transportista.setMail_transp("email22@example.com");
        transportista.setTelefon("666777888");
        transportista.setCert_hisenda(data);
        transportista.setCert_prl(data);
        transportista.setAsseguranca_merc(data);
        transportista.setAsseguranca_rc(data);
        transportista.setBaixatemporal(data);
        transportista.setActivaBaixaTemporal(false);

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
        helper = new DAOHelper(emf, Transportistes.class);
    }


    @After
    public void tearDown() {

        System.out.println("tearDown");
        if (manager.isOpen())
            manager.close();

        helper = null;
        manager = null;
    }

    @Test
    public void testA_insert() {
        try {
            helper.insert(transportista);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Transportistes result = (Transportistes) helper.getLastItem();

        assertEquals(transportista.getId(), result.getId());
        assertEquals(transportista.getRaosocial(), result.getRaosocial());
        assertEquals(transportista.getCif_nif(),result.getCif_nif());
        assertEquals(transportista.getMail_transp(),result.getMail_transp());
        assertEquals(transportista.getTelefon(),result.getTelefon());
        assertEquals(transportista.getCert_hisenda(),result.getCert_hisenda());
        assertEquals(transportista.getCert_prl(), result.getCert_prl());
        assertEquals(transportista.getAsseguranca_merc(), result.getAsseguranca_merc());
        assertEquals(transportista.getAsseguranca_rc(),result.getAsseguranca_rc());
        assertEquals(transportista.getBaixatemporal(),result.getBaixatemporal());
        assertEquals(transportista.isActivaBaixaTemporal(),result.isActivaBaixaTemporal());
    }

    @Test
    public void testB_update(){
        LocalDate dataTmp = LocalDate.of(2019,3,27);
        Date data = Utilitats.localDateToDate(dataTmp);


        transportista = (Transportistes) helper.getLastItem();
        transportista.setRaosocial("roger");
        transportista.setCif_nif("98765432A");
        transportista.setMail_transp("email2@example.com");
        transportista.setTelefon("666777999");
        transportista.setCert_hisenda(data);
        transportista.setCert_prl(data);
        transportista.setAsseguranca_merc(data);
        transportista.setAsseguranca_rc(data);
        transportista.setBaixatemporal(data);
        transportista.setActivaBaixaTemporal(false);
        helper.update(transportista);
        assertEquals(transportista.getRaosocial(), "roger");
        assertEquals(transportista.getCif_nif(),"98765432A");
        assertEquals(transportista.getMail_transp(),"email2@example.com");
        assertEquals(transportista.getTelefon(),"666777999");
        assertEquals(transportista.getCert_hisenda(),data);
        assertEquals(transportista.getCert_prl(),data);
        assertEquals(transportista.getAsseguranca_merc(),data);
        assertEquals(transportista.getAsseguranca_rc(),data);
        assertEquals(transportista.getBaixatemporal(),data);
        assertEquals(transportista.isActivaBaixaTemporal(),false);
    }

    @Test
    public void testC_delete(){
        Transportistes t = (Transportistes) helper.getLastItem();
        assertEquals(transportista.getId(), t.getId());
        helper.delete(transportista.getId());
    }

    @Test
    public void testD_getAll(){
        List <Transportistes> listac = helper.getAll();
        listac.forEach(System.out::println);

    }
}
