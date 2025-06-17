package core;

import network.NdoweRestController;

// TODO: Init main as Spring application
public class Main {

    public static void main(String[] args) {
        Bootloader bootloader = new Bootloader();
        bootloader.load();
        NdoweRestController ndoweRestController = new NdoweRestController();

        System.out.println("FINISH WITH NO ERRORS!!!!");
    }

}
