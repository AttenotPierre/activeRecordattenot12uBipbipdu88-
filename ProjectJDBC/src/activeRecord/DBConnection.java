package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    // --- Singleton ---
    private static DBConnection instance = null;

    // --- Paramètres de connexion (encapsulés) ---
    private String userName = "root";
    private String password = "";
    private String serverName = "127.0.0.1";
    private String portNumber = "3306"; // MAMP
    private String dbName = "testpersonne"; // valeur par défaut

    // --- L'unique connexion ---
    private Connection connect = null;

    // Constructeur privé -> Singleton
    private DBConnection() {
        // on pourrait laisser le driver se charger automatiquement,
        // mais pour être explicite :
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("*** ERREUR lors du chargement du driver ***");
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer l'instance unique
    private static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Factory : fournit la Connection
    public static Connection getConnection() throws SQLException {
        DBConnection inst = getInstance();

        // Si pas encore de connexion ou si elle est fermée -> en créer une
        if (inst.connect == null || inst.connect.isClosed()) {
            inst.createConnection();
        }
        return inst.connect;
    }

    // Création effective de la connexion
    private void createConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        String urlDB = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;
        System.out.println("Connexion à : " + urlDB);
        connect = DriverManager.getConnection(urlDB, connectionProps);
    }

    /**
     * 3.2 setNomDB :
     * - change le nom de la base
     * - ferme l’ancienne connexion si elle existe
     * - la prochaine fois qu’on appellera getConnection(),
     *   une nouvelle connexion sera créée vers cette base.
     */
    public static void setNomDB(String nomDB) {
        DBConnection inst = getInstance();
        inst.dbName = nomDB;

        // on ferme l’ancienne connexion : la prochaine getConnection()
        // recréera une nouvelle connexion vers la nouvelle base
        if (inst.connect != null) {
            try {
                if (!inst.connect.isClosed()) {
                    inst.connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                inst.connect = null;
            }
        }
    }
}
