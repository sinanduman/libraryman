package com.sinanduman.library;

import com.sinanduman.library.config.SecurityConfiguration;
import com.sinanduman.library.config.Swagger2Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Import({SecurityConfiguration.class, Swagger2Configuration.class})
@SpringBootApplication
@EnableSwagger2
public class LibrarymanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibrarymanApplication.class, args);
    }

}
