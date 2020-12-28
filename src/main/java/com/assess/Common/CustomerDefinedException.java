package com.assess.Common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Data
@ToString
@EqualsAndHashCode(callSuper=true)
public class CustomerDefinedException extends RuntimeException{

    private Integer code;

    public CustomerDefinedException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomerDefinedException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

}
