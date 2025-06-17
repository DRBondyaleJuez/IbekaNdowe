package exceptions;

import org.springframework.http.HttpStatus;

import java.sql.SQLException;

public class WordQuerySQLException extends SQLException {
    HttpStatus status;
    String message;

    public WordQuerySQLException(SQLException sqlException, String message) {
        super(message + " - Original SQL State: " + sqlException.getSQLState() +
                        ", Vendor Code: " + sqlException.getErrorCode() +
                        ", Original Message: " + sqlException.getMessage(),
                sqlException.getSQLState(),
                sqlException.getErrorCode(),
                sqlException.getCause() != null ? sqlException.getCause() : sqlException);

        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
