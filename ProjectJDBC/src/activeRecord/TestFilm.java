package activeRecord;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFilm {

    private Personne spielberg;
    private Personne scott;
    private Personne kubrick;

    @BeforeEach
    public void setup() throws SQLException, RealisateurAbsentException {
        DBConnection.setNomDB("testpersonne");

        /**
         * Créer la table Personne et la peupler
         */
        Personne.createTable();
        spielberg = new Personne("Spielberg", "Steven");
        spielberg.save();
        scott = new Personne("Scott", "Ridley");
        scott.save();
        kubrick = new Personne("Kubrick", "Stanley");
        kubrick.save();

        /**
         * Créer la table Film et la peupler
         */
        Film.createTable();
        new Film("Jurassic Park", spielberg).save();
        new Film("E.T.", spielberg).save();
        new Film("Blade Runner", scott).save();
        new Film("2001: A Space Odyssey", kubrick).save();
    }

    @AfterEach
    public void clean() throws SQLException {
        Film.deleteTable();
        Personne.deleteTable();
    }

    // 11.1 : test de findById
    @Test
    public void testFindById() throws SQLException {
        Film f1 = Film.findById(1);
        assertNotNull(f1, "findById(1) ne doit pas renvoyer null");
        assertEquals(1, f1.getId());
        assertEquals("Jurassic Park", f1.getTitre());

        Film fInconnu = Film.findById(9999);
        assertNull(fInconnu, "findById(9999) doit renvoyer null");
    }

    /**
     * 11.2 : test de getRealisateur
     * @throws SQLException
     */
    @Test
    public void testGetRealisateur() throws SQLException {
        Film f = Film.findById(1);
        Personne real = f.getRealisateur();

        assertNotNull(real);
        assertEquals("Spielberg", real.getNom());
        assertEquals("Steven", real.getPrenom());
    }

    //save new
    @Test
    public void testSaveNew() throws SQLException, RealisateurAbsentException {
        Film f = new Film("Minority Report", spielberg);
        assertEquals(-1, f.getId());
        f.save();
        assertNotEquals(-1, f.getId());

        Film fRetrouve = Film.findById(f.getId());
        assertNotNull(fRetrouve);
        assertEquals("Minority Report", fRetrouve.getTitre());
    }

    /**
     * 12.2 : test de save avec un réalisateur non sauvegardé
     */
    @Test
    public void testSaveAvecRealisateurNonSauvegarde() {
        Personne nouveauReal = new Personne("Nolan", "Christopher");
        Film f = new Film("Inception", nouveauReal);

        assertThrows(RealisateurAbsentException.class, () -> {
            f.save();
        });
    }

    //upd
    @Test
    public void testUpdate() throws SQLException, RealisateurAbsentException {
        Film f = Film.findById(1);
        f.setTitre("Jurassic Park (Updated)");
        f.save();

        Film fRetrouve = Film.findById(1);
        assertEquals("Jurassic Park (Updated)", fRetrouve.getTitre());
    }

    //del
    @Test
    public void testDelete() throws SQLException {
        Film f = Film.findById(1);
        assertNotNull(f);
        f.delete();
        assertEquals(-1, f.getId());

        Film f2 = Film.findById(1);
        assertNull(f2);
    }

    /**
     * 12.3 : test de findByRealisateur
     */
    @Test
    public void testFindByRealisateur() throws SQLException {
        List<Film> filmsSpielberg = Film.findByRealisateur(spielberg);

        assertEquals(2, filmsSpielberg.size());
        assertTrue(filmsSpielberg.stream()
                .anyMatch(f -> f.getTitre().equals("Jurassic Park")));
        assertTrue(filmsSpielberg.stream()
                .anyMatch(f -> f.getTitre().equals("E.T.")));

        List<Film> filmsScott = Film.findByRealisateur(scott);
        assertEquals(1, filmsScott.size());
        assertEquals("Blade Runner", filmsScott.get(0).getTitre());
    }

    /**
     * 12.4 : test exception si le réalisateur d'un film n'existe pas en base
     */
    @Test
    public void testFilmAvecRealisateurHorsBase() {
        Personne nouveauReal = new Personne("Tarantino", "Quentin");
        assertEquals(-1, nouveauReal.getId());

        Film f = new Film("Pulp Fiction", nouveauReal);

        assertThrows(RealisateurAbsentException.class, () -> {
            f.save();
        }, "Un film ne peut pas être sauvegardé si son réalisateur n'est pas dans la base");
    }
}