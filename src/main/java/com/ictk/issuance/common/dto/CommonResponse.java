package com.ictk.issuance.common.dto;


import com.ictk.issuance.common.constants.AppCode;
import com.ictk.issuance.common.exception.IctkException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private CommonRespHeader header;
    private T body;

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(CommonRespHeader.builder()
                .trId("")
                .rtnCode(AppCode.SUCCESS.code())
                .rtnMessage(AppCode.SUCCESS.message()).build(), data);
    }

    public static <T> CommonResponse<T> ok(String trId, T data) {
        return new CommonResponse<>(CommonRespHeader.builder()
                .trId(trId)
                .rtnCode(AppCode.SUCCESS.code())
                .rtnMessage(AppCode.SUCCESS.message()).build(), data);
    }

    public static <T> CommonResponse<T> nok(String trId, T data) {
        return new CommonResponse<>(CommonRespHeader.builder()
                .trId(trId)
                .rtnCode(AppCode.FAIL.code())
                .rtnMessage(AppCode.FAIL.message()).build(), data);
    }

    public static <T> CommonResponse<T> ok(String trId) {
        return ok(trId, null);
    }


    public static <T> CommonResponse<T> of(IctkException appe) {
        return of(null, null, appe);
    }

    public static <T> CommonResponse<T> of(String trId, IctkException appe) {
        return of(null, trId, appe);
    }

    public static <T> CommonResponse<T> of(T data, String trId, IctkException appe) {
        return new CommonResponse<>(CommonRespHeader.builder()
                .trId(trId!=null?trId:(appe.getTrId()!=null?appe.getTrId():""))
                .rtnCode(appe.getCode().code())
                .rtnMessage( appe.getMessage() ).build(), data);
    }

}
