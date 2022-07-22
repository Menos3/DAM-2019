package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.model.Transportistes;
import cat.arsgbm.coral.model.Xofers;
import org.junit.*;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOHelperXofers_Test {

    static EntityManagerFactory emf;
    static DAOHelper helperX;
    static Xofers xofer = new Xofers();
    //static Transportistes transportista= new Transportistes();

        @BeforeClass
        public static void setUpClass() {
            System.out.println("setUpClass");
            emf = Persistence.createEntityManagerFactory("test");
            //helperX = new DAOHelper(emf, Xofers.class);
            DAOHelper helperT = new DAOHelper(emf, Transportistes.class);

            //CARREGO NOU XOFER PER FER LES PROVES AMB HELPER I CLASSE XOFER;
            Transportistes transportista = (Transportistes) helperT.read(2);
            LocalDate dataTmp = LocalDate.of(2019, 1, 27);
            Date data = Utilitats.localDateToDate(dataTmp);
            xofer.setTransportista(transportista);
            xofer.setNomXofer("paco");
            xofer.setCognomXofer("22345688A");
            xofer.setCorreuXofer("email22@example.com");
            xofer.setTelefon("666777888");
            xofer.setCarnetAdr(data);
            xofer.setCarnetConduir(data);
            xofer.setCarnetCarreter(data);
            xofer.setEmpremta(false);
            xofer.setBaixaTemporal(data);
            xofer.setActivaBaixaTemporal(true);
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
            helperX = new DAOHelper(emf, Xofers.class);
        }


        @After
        public void tearDown() {  //després test
            System.out.println("tearDown");

            helperX = null;
        }

        @Test
        public void testA_insert() {
            try {
                helperX.insert(xofer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //COMPARPO DADES DE XOFER QUE HE INSERTAT AMB LES QUE TREC DE BBDD SI
            // SÓN IGUALS VOL DIR QUE S'HA INSERTAT CORRECTAMENT

            Xofers result = (Xofers) helperX.getLastItem();

            assertEquals(xofer.getId(), result.getId(),0.0);
            assertEquals(xofer.getTransportista().getId(),result.getTransportista().getId());
            assertEquals(xofer.getNomXofer(), result.getNomXofer());
            assertEquals(xofer.getCognomXofer(), result.getCognomXofer());
            assertEquals(xofer.getCorreuXofer(), result.getCorreuXofer());
            assertEquals(xofer.getTelefon(), result.getTelefon());
            assertEquals(xofer.getCarnetAdr(), result.getCarnetAdr());
            assertEquals(xofer.getCarnetConduir(), result.getCarnetConduir());
            assertEquals(xofer.getCarnetCarreter(), result.getCarnetCarreter());
            assertEquals(xofer.getBaixaTemporal(), result.getBaixaTemporal());
            assertEquals(xofer.isActivaBaixaTemporal(), result.isActivaBaixaTemporal());
        }

        @Test
        public void testB_update() {
            LocalDate dataTmp = LocalDate.of(2019, 3, 27);
            Date data = Utilitats.localDateToDate(dataTmp);
//CANVIO VALORS I COMPARO AMB NOUS VALORS PER VEURE SI S'HA ACTUALITZAT

            xofer = (Xofers) helperX.getLastItem();
            xofer.setNomXofer("roger");
            xofer.setCognomXofer("33345678A");
            xofer.setCorreuXofer("email22@ex.com");
            xofer.setTelefon("666777777");
            xofer.setCarnetAdr(data);
            xofer.setCarnetConduir(data);
            xofer.setCarnetCarreter(data);
            xofer.setEmpremta(false);
            xofer.setBaixaTemporal(data);
            xofer.setActivaBaixaTemporal(true);
            helperX.update(xofer);
            assertEquals(xofer.getNomXofer(), "roger");
            assertEquals(xofer.getCognomXofer(), "33345678A");
            assertEquals(xofer.getCorreuXofer(), "email22@ex.com");
            assertEquals(xofer.getTelefon(), "666777777");
            assertEquals(xofer.getCarnetAdr(), data);
            assertEquals(xofer.getCarnetConduir(), data);
            assertEquals(xofer.getCarnetCarreter(), data);
            assertEquals(xofer.isEmpremta(), false);
            assertEquals(xofer.getBaixaTemporal(), data);
            assertEquals(xofer.isActivaBaixaTemporal(), false);
        }

        @Test
        public void testC_delete() {
            //Xofers x = (Xofers) helperX.getLastItem();
            //assertEquals(xofer.getId(), x.getId(),0.0);
            helperX.delete(xofer.getId());
            Xofers result = (Xofers)helperX.read(xofer.getId());
            assertNull(result);
        }
//
//        @Test
//        public void testD_getAll() {
//            List<Xofers> listac = helper.getAll();
//            listac.forEach(System.out::println);
//
//        }
   }