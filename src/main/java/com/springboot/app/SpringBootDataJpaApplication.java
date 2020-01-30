package com.springboot.app;

import com.springboot.app.services.IIUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	IIUploadFileService uploadFileService;

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		uploadFileService.deleteAll(); // borra carpeta upload
		uploadFileService.init(); // crea carpeta upload
/*
        String password = "12345";

        for(int i = 0; i<2; i++) {
            String bcryptPassword = passwordEncoder.encode(password);
            System.out.println(bcryptPassword);
        }
        */
	}
}
