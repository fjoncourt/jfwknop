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
}
