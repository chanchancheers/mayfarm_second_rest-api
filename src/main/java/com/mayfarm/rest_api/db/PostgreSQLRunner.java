package com.mayfarm.rest_api.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;


@Component
@RequiredArgsConstructor
@Slf4j
public class PostgreSQLRunner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override //java -jar this.jar a b c --d=1 --e=2로 실행할 경우 각 인자에 접근 가능
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()){
            log.info("'rest-api' DATABASE CONNECTED. ");
        }

        return;
    }




}