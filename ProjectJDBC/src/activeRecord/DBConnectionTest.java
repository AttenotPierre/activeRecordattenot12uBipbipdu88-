package activeRecord;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class DBConnectionTest {


    private String getCurrentDBName(Connection c) throws SQLException {
        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DATABASE()")) {
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return null;
    }

    @Test
    public void testConnectionTypeEtSingleton() throws Exception {
        // On repart sur la base par défaut
        DBConnection.setNomDB("testpersonne");

        Connection c1 = DBConnection.getConnection();
        Connection c2 = DBConnection.getConnection();

        // 1) Le type doit bien être java.sql.Connection
        assertTrue("La connexion doit être une java.sql.Connection",
                c1 instanceof java.sql.Connection);

        // 2) Singleton : c1 et c2 doivent être le même objet
        assertSame("DBConnection.getConnection() doit renvoyer toujours la même instance",
                c1, c2);
    }

    @Test
    public void testChangementDeBase() throws Exception {
        // On commence par se connecter à la base par défaut
        DBConnection.setNomDB("testpersonne");
        Connection c1 = DBConnection.getConnection();
        String db1 = getCurrentDBName(c1);
        assertEquals("testpersonne", db1);

        // On demande maintenant une autre base
        DBConnection.setNomDB("testpersonne2");
        Connection c2 = DBConnection.getConnection();
        String db2 = getCurrentDBName(c2);

        // La base doit avoir changé
        assertEquals("testpersonne2", db2);

        // Et comme on recrée une connexion après setNomDB,
        // c1 et c2 ne devraient pas être le même objet
        assertNotSame("Après changement de base, la connexion doit être recréée", c1, c2);
    }
}
