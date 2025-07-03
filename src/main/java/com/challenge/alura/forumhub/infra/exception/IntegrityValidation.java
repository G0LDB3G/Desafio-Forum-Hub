package com.challenge.alura.forumhub.infra.exception;

import java.io.Serial;

public class IntegrityValidation extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public IntegrityValidation(String s) {
        super(s);
    }
}
