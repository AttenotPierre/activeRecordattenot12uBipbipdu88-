package activeRecord;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPersonne {



    //cree la table  et les personnes randoms
    @BeforeEach
    public void setup() throws SQLException { // add automatique exeption car sinon ça fontionne pas
        DBConnection.setNomDB("testpersonne");
        Personne.createTable();
        new Personne("NomAlpha", "PrenomAlpha").save();
        new Personne("NomBeta", "PrenomBeta").save();
        new Personne("NomGamma", "PrenomGamma").save();
        new Personne("NomDelta", "PrenomDelta").save();
    }
    // supposons suelle existe

    @AfterEach
    public void clean() throws SQLException { // encore une fois
        Personne.deleteTable();
    }



    // 7.1 : test de findAll
    @Test
    public void testFindAll() throws SQLException, ClassNotFoundException {
        ArrayList<Personne> personnes = Personne.findAll();

        // Avec le SQL fourni, on sait qu'il y a au moins 4 personnes
        assertTrue(personnes.size() >= 4,
                "Il doit y avoir au moins 4 personnes dans la base");

        // On vérifie par exemple qu'il y a bien Spielberg / Steven
        boolean foundSpielberg = personnes.stream()
                .anyMatch(p -> p.getNom().equals("Spielberg")
                        && p.getPrenom().equals("Steven"));

        assertTrue(foundSpielberg,
                "Spielberg Steven doit être présent dans la base");
    }

    // 7.2 : test de findById
    @Test
    public void testFindById() throws SQLException {
        // D'après le SQL fourni, l'id 1 correspond à Spielberg / Steven
        Personne p1 = Personne.findById(1);
        assertNotNull(p1, "findById(1) ne doit pas renvoyer null");
        assertEquals(1, p1.getId());
        assertEquals("Spielberg", p1.getNom());
        assertEquals("Steven", p1.getPrenom());

        // Un id très grand ne doit pas exister
        Personne pInconnu = Personne.findById(9999);
        assertNull(pInconnu, "findById(9999) doit renvoyer null");
    }

    // 7.3 : test de findByName
    @Test
    public void testFindByName() throws SQLException {
        // D'après le SQL, nom = 'Scott', prenom = 'Ridley' pour id = 2
        List<Personne> scotts = Personne.findByName("Scott");

        assertFalse(scotts.isEmpty(),
                "findByName('Scott') ne doit pas renvoyer une liste vide");

        // Tous les résultats doivent avoir nom = "Scott"
        for (Personne p : scotts) {
            assertEquals("Scott", p.getNom());
        }

        // Tester un nom inexistant
        List<Personne> inconnus = Personne.findByName("NomQuiNexistePas");
        assertTrue(inconnus.isEmpty(),
                "findByName sur un nom inexistant doit renvoyer une liste vide");
    }

}
