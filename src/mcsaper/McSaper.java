/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcsaper;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * McSaper class creates frame for the application. Sets the size of the frame
 * and so on
 *
 * @author Michał Cichoń
 */
public class McSaper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame mainFrame = new McSaperMainFrame();
        mainFrame.setLocation(200, 100);
        mainFrame.setSize(400, 500);
        mainFrame.setMinimumSize(new Dimension(400, 500));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
    }
}
