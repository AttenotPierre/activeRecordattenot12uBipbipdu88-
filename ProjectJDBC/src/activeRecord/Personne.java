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
    // (pas encore en base)
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

    // sera utile plus tard pour save(), mais on peut le laisser public
    public void setId(int id) {
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
    public static List<Personne> findAll() throws SQLException {
        List<Personne> res = new ArrayList<>();

        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM Personne ORDER BY id";

        try (PreparedStatement ps = connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                res.add(new Personne(id, nom, prenom));
            }
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

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, idRecherche);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    return new Personne(id, nom, prenom);
                } else {
                    return null;
                }
            }
        }
    }

    // ===============================================================
    // 7.3 findByName : retourne la liste des Personne dont le nom
    //      correspond au paramètre
    // ===============================================================
    public static List<Personne> findByName(String nomRecherche) throws SQLException {
        List<Personne> res = new ArrayList<>();

        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM Personne WHERE nom = ? ORDER BY id";

        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, nomRecherche);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    res.add(new Personne(id, nom, prenom));
                }
            }
        }

        return res;
    }
}
