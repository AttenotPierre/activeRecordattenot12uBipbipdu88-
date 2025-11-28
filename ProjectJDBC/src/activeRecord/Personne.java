package activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Personne {

    // --- Attributs ---
    private int id;
    private String nom;
    private String prenom;

    // --- Constructeur public pour créer une Personne "nouvelle" ---
    public Personne(String nom, String prenom) {
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
    }

    // --- Constructeur privé pour construire depuis la base ---
    private Personne(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    // --- Getters / Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {  // tu peux le laisser public
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "Personne(" + id + ") " + nom + " " + prenom;
    }

    // ===============================================================
    // 7.1 findAll : retourne tous les tuples de la table Personne
    // ===============================================================
    public static ArrayList<Personne> findAll() throws SQLException, ClassNotFoundException {
        ArrayList<Personne> res = new ArrayList<>();

        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM Personne ORDER BY id";
        PreparedStatement ps = connect.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            res.add(new Personne(id, nom, prenom));
        }

        return res;
    }

    // ===============================================================
    // 7.2 findById : retourne la Personne correspondant à l'id,
    //      ou null si elle n'existe pas
    // ===============================================================
    public static Personne findById(int idRecherche) throws SQLException {
        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM Personne WHERE id = ?";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setInt(1, idRecherche);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Personne(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
            );
        } else {
            return null;
        }
    }

    // ===============================================================
    // 7.3 findByName : retourne la liste des Personne dont le nom
    //      correspond au paramètre
    // ===============================================================
    public static List<Personne> findByName(String nomRecherche) throws SQLException {
        ArrayList<Personne> res = new ArrayList<>();

        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM Personne WHERE nom = ? ORDER BY id";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, nomRecherche);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            res.add(new Personne(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom")
            ));
        }

        return res;
    }
    //8.4 delete supprime la persone de la bd
    public void delete() throws SQLException {
        Connection c = DBConnection.getConnection();
        String sql = "DELETE FROM Personne WHERE id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, this.id);
        ps.executeUpdate();
        this.id = -1;
    }

    //8.1 cration et drop de la table
    //creatiion
    public static void createTable() throws SQLException {
        Connection c = DBConnection.getConnection();
        String sql = "CREATE TABLE Personne (id INTEGER AUTO_INCREMENT, nom VARCHAR(40), prenom VARCHAR(40), PRIMARY KEY(id))";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.executeUpdate();
    }

    //delete
    public static void deleteTable() throws SQLException {
        Connection c = DBConnection.getConnection();
        String sql = "DROP TABLE Personne";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.executeUpdate();
    }

    public void save() throws SQLException {
        if (this.id == -1) {
            saveNew();
        } else {
            update();
        }
    }
    public void saveNew() throws SQLException {
        Connection c = DBConnection.getConnection();
    }
    public void update() throws SQLException {
        Connection c = DBConnection.getConnection();

    }

}
