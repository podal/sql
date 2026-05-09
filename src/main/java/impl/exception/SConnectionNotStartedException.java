package impl.exception;

import sql.SException;

public class SConnectionNotStartedException extends SException {

    public SConnectionNotStartedException(String s) {
        super(s);
    }
}
