package application;

import application.core.Bootloader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Bootloader bootloader = new Bootloader();
        bootloader.load();
        SpringApplication.run(Main.class, args);

        //System.out.println("FINISH WITH NO ERRORS!!!!");
    }

}
