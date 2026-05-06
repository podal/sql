package impl;

import java.sql.SQLException;

public class SException extends RuntimeException {
    public SException() {
        super();
    }

    public SException(SQLException e) {
        super(e);
    }

    public SException(String m) {
        super(m);
    }

}
