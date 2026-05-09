package impl.exception;

import sql.SException;
import lombok.Getter;

public class SPrimitiveNoRecord extends SException {
    @Getter
    private final Class<?> recordClass;

    public SPrimitiveNoRecord(Class<?> recordClass) {
        super();
        this.recordClass = recordClass;
    }

}
