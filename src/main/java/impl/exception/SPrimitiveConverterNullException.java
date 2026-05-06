package impl.exception;

import impl.SException;
import lombok.Getter;

@Getter
public class SPrimitiveConverterNullException extends SException {
    private Class<?> methodType;
    private String methodName;

    public SPrimitiveConverterNullException(Class<?> methodType, String methodName) {
        super();
        this.methodType = methodType;
        this.methodName = methodName;
    }
}
