package an.kondratev.springwallets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class SpringWalletsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWalletsApplication.class, args);
	}

}
