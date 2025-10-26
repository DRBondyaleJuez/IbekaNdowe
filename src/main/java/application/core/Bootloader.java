package application.core;

import application.utils.CloudinaryPropertiesReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import application.utils.PropertiesReader;

public class Bootloader {

    public Bootloader() {
        System.out.println("Bootloader constructor called by Spring.");
    }

    public void load() {
        System.out.println("Bootloader load() method executed by @PostConstruct.");
        PropertiesReader propertiesReader = new PropertiesReader();
        propertiesReader.loadAllProperties();
        CloudinaryPropertiesReader cloudinaryPropertiesReader = new CloudinaryPropertiesReader();
        cloudinaryPropertiesReader.loadAllProperties();
    }
}
