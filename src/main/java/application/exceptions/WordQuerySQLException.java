package application.exceptions;

import org.springframework.http.HttpStatus;

import java.sql.SQLException;

public class WordQuerySQLException extends RuntimeException {
    HttpStatus status;
    String message;

    public WordQuerySQLException(SQLException sqlException, String message) {
        super(message + "\n" + " - Original SQL State: " + sqlException.getSQLState() + "\n" +
                        "- Vendor Code: " + sqlException.getErrorCode() + "\n" +
                        "- Original Message: " + sqlException.getMessage());

        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
