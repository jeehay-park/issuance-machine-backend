package com.ictk.issuance.common.exception;

import com.ictk.issuance.common.constants.Code;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.List;

@Getter
public class IctkException extends RuntimeException {

    private final String trId;
    private final Code code;

    public IctkException(Code code) {
        super(code.message());
        this.trId = "";
        this.code = code;
    }
    public IctkException(String trId, Code code) {
        super(code.message());
        this.trId = trId;
        this.code = code;
    }

    public IctkException(String trId, Code code, Object...params) {
        super(MessageFormat.format(code.message().replace("'", "''"), params));
        this.trId = trId;
        this.code = code;
    }

    public IctkException(String trId, Code code, List<String> params) {
        super(MessageFormat.format(code.message().replace("'", "''"), params.toArray()));
        this.trId = trId;
        this.code = code;
    }

    public IctkException(String trId, String message, Code code) {
        super(message);
        this.trId = trId;
        this.code = code;
    }

}
