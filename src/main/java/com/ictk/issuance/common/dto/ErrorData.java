package com.ictk.issuance.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorData {
    private String code;
    private String message;
}
