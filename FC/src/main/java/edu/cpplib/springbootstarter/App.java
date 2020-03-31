package edu.cpplib.springbootstarter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import edu.cpplib.service.storage.StorageProperties;
import edu.cpplib.service.storage.StorageService;

//start from here
@SpringBootApplication(scanBasePackages={"edu.cpplib.controller", 
										 "edu.cpplib.models", 
										 "edu.cpplib.service.storage",
										 "edu.cpplib.*"})
@EnableConfigurationProperties(StorageProperties.class)
//@SpringBootApplication //this annotation indicates this class is where spring boot starts from
//@ComponentScan(basePackages = {	"edu.cpplib.controller.main", 
//								"edu.cpplib.models", 
//								"edu.cpplib.storage"})
public class App {
	public static void main (String args[]){
		SpringApplication.run(App.class, args);
		//running at port 8080
		//class where you have the main method, that is, this class.
	}
	
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
}
