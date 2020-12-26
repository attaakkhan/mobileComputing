package com.mc.a2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MCAssingment2ServerApplication {
	 private int maxUploadSizeInMb = 10 * 1024 * 1024*100; // 10 MB


	public static void main(String[] args) {
		SpringApplication.run(MCAssingment2ServerApplication.class, args);
	}

}
