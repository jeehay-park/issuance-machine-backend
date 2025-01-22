package com.ictk.issuance.common.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Getter;
import lombok.RequiredArgsConstructor;

// @Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppCode implements Code {
    ACCESS_NOTAUTHORISED("E000003", "접근 권한이 없습니다", "You do not have access authorization"),
    ALREADY_REGISTERED("E000004", "이미 등록되어 있습니다", "It's already registered"),
    CANNOT_USE_NAME("E000006", "해당 명칭은 사용할 수 없습니다", "This name cannot be used"),
    DATA_INVALID("E000002", "데이터가 유효하지 않습니다", "Data is not valid"),
    DATA_NOT_PARSABLE("E120006", "파싱할수 없는 데이터 형식입니다", "Data format that cannot be parsed"),
    DATA_RANGE_INVALID("E120004", "요청 메시지 {0} 범위가 올바르지 않습니다", "Request message{0} range is not valid"),
    DEVICE_PROC_ERROR("E130002", "발급기 디바이스 요청중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine devices processing"),
    FAIL("E000012", "실패", "Fail"),

    HTTP_MEDIA_TYPE_NOT_SUPPORTED("E000010", "지원하지 않는 HTTP 미디어타입입니다. {0}", "The requested HTTP media type is not supported {0}"),
    HTTP_MESSAGE_NOT_READABLE("E000011", "본문을 읽을 수 없습니다. {0}", "Can not read HTTP contents. {0}"),
    HTTP_METHOD_NOT_SUPPORTED("E000009", "지원하지 않는 HTTP Method입니다. {0}", "The requested HTTP method is not supported {0}"),
    INFO_NOTEXIST("E000005", "요청하신 정보가 존재하지 않습니다", "The requested information does not exist"),
    ISSUANCE_CONFIG_ERROR("E130003", "발급 설정 처리중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine configuration processing"),

    MACHINE_PROC_ERROR("E130001", "발급기 요청중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine processing") //
    ,
    NO_HANDLER_FOUND("E000007", "요청하신 핸들러를 찾을 수 없습니다 {0}", "The requested handler can not be found {0}"),
    NO_RESOURCE_FOUND("E000008", "요청하신 리소스를 찾을 수 없습니다 {0}", "The requested resource can not be found {0}"),
    PROCESSING_1_ERROR("E120001", "처리중 {0} 에러가 발생하였습니다", "An error {0} occurred while processing") //
    ,
    PROCESSING_ERROR("E000001", "처리중 오류가 발생하였습니다", "An error occurred while processing"),
    REQ_BODY_ERROR("E120003", "바디 요청 메시지 {0}에 오류가 있습니다", "There is an error in the body request message {0}"),
    REQ_DATA_INVALID("E120005", "요청 메시지 필드 {0} 값 {1}이 올바르지 않습니다", "Invalid request message field{0} value {1}"),

    REQ_HEADER_ERROR("E120002", "헤더 필드 {0}에 오류가 있습니다", "Error in header field {0}"),
    SNRULE_PROC_ERROR("E130004", "시리얼넘버규칙 처리중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine SN-Rule processing"),
    SUCCESS("000000", "성공", "Success"),
    TRID_INVALID("E120010", "유효하지 않은 trId {0} 입니다", "This is a invalid trId {0}"),
    CODEENUM_PROC_ERROR("E130004", "코드 ENUM 처리중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine Code-ENUM processing"),
    PROGRAM_PROC_ERROR("E130005", "프로그램 처리중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine Program processing"),
    WORK_PROC_ERROR("E130006", "작업 처리중 {0} 에러가 발생하였습니다", "An error {0} occurred while Issuance-machine Work processing"),
    ;

    private final String code;
    private final String message;
    private final String enmessage;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String enmessage() {
        return enmessage;
    }

}
