package core;

import utils.PropertiesReader;

public class Bootloader {

    public void load() {
        PropertiesReader propertiesReader = new PropertiesReader();
        propertiesReader.loadAllProperties();
    }
}
