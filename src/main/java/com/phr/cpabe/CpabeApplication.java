package com.phr.cpabe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//@ImportResource({ "AWS.config" })
@SpringBootApplication
@EnableWebMvc
public class CpabeApplication  {

    public static void main(String[] args) {
        SpringApplication.run(CpabeApplication.class, args);
    }

}
