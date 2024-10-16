package com.ictk.issuance.common.dto;

import jakarta.validation.Valid;
import lombok.*;


@ToString
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Valid
public class CommonRequest<T> {

    private CommonReqHeader header;
    @Valid
    private T body;

}
