package activeRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Film {


    private int id;
    private String titre;
    private int id_real;


    public Film(String titre, Personne realisateur) {
        this.id = -1;
        this.titre = titre;
        this.id_real = realisateur.getId();
    }


    private Film(int id, String titre, int id_real) {
        this.id = id;
        this.titre = titre;
        this.id_real = id_real;
    }

    // --- Getters / Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getId_real() {
        return id_real;
    }

    public void setId_real(int id_real) {
        this.id_real = id_real;
    }

    @Override
    public String toString() {
        return "Film(" + id + ") " + titre + " [réalisateur id=" + id_real + "]";
    }

    // 11.1 findById : retourne le Film correspondant à l'id

    public static Film findById(int idRecherche) throws SQLException {
        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM film WHERE id = ?";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setInt(1, idRecherche);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Film(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getInt("id_real")
            );
        } else {
            return null;
        }
    }


    /**
     * 11.2 getRealisateur : retourne le réalisateur du film
     * @return
     * @throws SQLException
     */

    public Personne getRealisateur() throws SQLException {
        return Personne.findById(this.id_real);
    }


    // 12.1 Création et suppression de la table
       public static void createTable() throws SQLException {
        Connection c = DBConnection.getConnection();
        String sql = "CREATE TABLE film ("
                + "id INTEGER AUTO_INCREMENT, "
                + "titre VARCHAR(100), "
                + "id_real INTEGER, "
                + "PRIMARY KEY(id), "
                + "FOREIGN KEY(id_real) REFERENCES Personne(id)"
                + ")";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.executeUpdate();
    }

    public static void deleteTable() throws SQLException {
        Connection c = DBConnection.getConnection();
        String sql = "DROP TABLE film";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.executeUpdate();
    }

       /**
        * 12.2 save : sauvegarde le film dans la base de données
        * @throws SQLException
        * @throws RealisateurAbsentException
        */
       public void save() throws SQLException, RealisateurAbsentException {
        if (this.id == -1) {
            saveNew();
        } else {
            update();
        }
    }

    private void saveNew() throws SQLException, RealisateurAbsentException {
        if (this.id_real == -1) {
            throw new RealisateurAbsentException("Le réalisateur doit être sauvegardé avant le film");
        }

        Connection c = DBConnection.getConnection();
        String sql = "INSERT INTO film (titre, id_real) VALUES (?, ?)";
        PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, this.titre);
        ps.setInt(2, this.id_real);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            this.id = rs.getInt(1);
        }
    }

    private void update() throws SQLException, RealisateurAbsentException {
        if (this.id_real == -1) {
            throw new RealisateurAbsentException("Le réalisateur doit être sauvegardé avant le film");
        }

        Connection c = DBConnection.getConnection();
        String sql = "UPDATE film SET titre = ?, id_real = ? WHERE id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, this.titre);
        ps.setInt(2, this.id_real);
        ps.setInt(3, this.id);
        ps.executeUpdate();
    }

    /**
        * 12.4 delete : supprime le film de la base de données
     * @throws SQLException
     */
    public void delete() throws SQLException {
        Connection c = DBConnection.getConnection();
        String sql = "DELETE FROM film WHERE id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, this.id);
        ps.executeUpdate();
        this.id = -1;
    }


    /**
     * 13.1 findByRealisateur : retourne la liste des films d'un réalisateur
     * @param realisateur
     * @return
     * @throws SQLException
     */
    public static List<Film> findByRealisateur(Personne realisateur) throws SQLException {
        ArrayList<Film> res = new ArrayList<>();

        Connection connect = DBConnection.getConnection();
        String sql = "SELECT * FROM film WHERE id_real = ? ORDER BY id";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setInt(1, realisateur.getId());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            res.add(new Film(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getInt("id_real")
            ));
        }

        return res;
    }
}