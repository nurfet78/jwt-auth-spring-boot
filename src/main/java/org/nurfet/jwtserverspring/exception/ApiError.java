package org.nurfet.jwtserverspring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private int status;

    private String error;

    private String url;

    private String timestamp;

    private String details;
}

