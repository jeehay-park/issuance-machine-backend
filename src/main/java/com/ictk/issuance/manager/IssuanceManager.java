package com.ictk.issuance.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
@Component
public class IssuanceManager {

    // 테이블 생성
    synchronized public String createTable(String database, String table, BiFunction<String,String,String> handleTable ) {

        return handleTable.apply(database, table);

    }


}
