package com.dev.olivebakery.domain.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {

    private String userDefineErrorMessage;
    private String originalErrorMessage;
    private String requestURL;

    @Builder
    public ErrorDto(String userDefineErrorMessage, String originalErrorMessage, String requestURL) {
        this.userDefineErrorMessage = userDefineErrorMessage;
        this.originalErrorMessage = originalErrorMessage;
        this.requestURL = requestURL;
    }
}
