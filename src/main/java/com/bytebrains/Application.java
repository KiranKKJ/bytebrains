package com.bytebrains;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.bytebrains.service.ExcelService;
import com.bytebrains.service.WebscarpperService;

@ComponentScan({ "com.bytebrains" })
@SpringBootApplication
public class Application {

	public static void main(String args[]) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		ExcelService excelService = (ExcelService) context.getBean("excelService");
		WebscarpperService webscapperService = (WebscarpperService) context.getBean("webscarpperService");

		try {
			excelService.read();
			webscapperService.webscrap();
		} finally {
			context.close();
		}
	}
}
