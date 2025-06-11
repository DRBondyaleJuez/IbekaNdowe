package core;

public class Main {

    public static void main(String[] args) {
        Bootloader bootloader = new Bootloader();
        bootloader.load();

        System.out.println("FINISH WITH NO ERRORS!!!!");
    }

}
