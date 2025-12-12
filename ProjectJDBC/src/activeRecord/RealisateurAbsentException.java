package activeRecord;

/**
 * Exception levée lorsqu'un réalisateur est absent (inexistant) en base de données.
 */

public class RealisateurAbsentException extends Exception {
    public RealisateurAbsentException(String message) {
        super(message);
    }
}