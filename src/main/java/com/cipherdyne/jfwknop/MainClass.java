package com.cipherdyne.jfwknop;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainClass {

    public static void main(final String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        javax.swing.SwingUtilities.invokeLater(() -> new MainWindowController());
    }

    //YXplcnR5dWlvcHFzZGZnaDAxMjM0NTY3ODkwMTIzNDU=
    //Yd/3bM6F2CRh1LkckhO5T/6myh9LB1bEXyatzmgNRlA=
}
