

package com.example.myreform.User.response;

import com.example.myreform.config.ResponseType;
import com.example.myreform.validation.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser extends ResponseType {
    public ResponseUser(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
    @Nullable
    @Autowired
    private String token;
    public ResponseUser(ExceptionCode exceptionCode, @Nullable String token) {
        super(exceptionCode);
        this.token = token;
    }
}