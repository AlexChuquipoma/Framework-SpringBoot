package ec.edu.ups.icc.fundamentos01.exception.domain;

import org.springframework.http.HttpStatus;
import ec.edu.ups.icc.fundamentos01.exception.base.ApplicationException;

public class BusinessException extends ApplicationException {

    public BusinessException(String message) {
        super(HttpStatus.valueOf(422), message);
    }

    protected BusinessException(HttpStatus status, String message) {
        super(status, message);
    }
}
