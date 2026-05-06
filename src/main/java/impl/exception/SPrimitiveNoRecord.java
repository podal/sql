package impl.exception;

import impl.SException;
import lombok.Getter;

public class SPrimitiveNoRecord extends SException {
    @Getter
    private final Class<?> recordClass;

    public SPrimitiveNoRecord(Class<?> recordClass) {
        super();
        this.recordClass = recordClass;
    }

}
