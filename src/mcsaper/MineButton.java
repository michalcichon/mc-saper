/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcsaper;

import java.awt.Color;
import javax.swing.JButton;

/**
 * Mine button class. We extends JButton because we want to have some extra
 * methods and pools (check if button is flat or not)
 *
 * @author Michał Cichoń
 */
public class MineButton extends JButton {

    private boolean flat = false;

    /**
     * Constructor of MineButton
     *
     * @param string String which is presented on the button
     */
    MineButton(String string) {
        super(string);
    }

    /**
     * Set flat boolean value
     *
     * @param flat flat or not flat
     */
    public void setFlat(boolean flat) {
        this.flat = flat;
        if (flat) {
            this.setBackground(new Color(220, 220, 220));
        } else {
            this.setBackground(new Color(241, 241, 241));
        }

    }

    /**
     * Check if the button is flat
     *
     * @return boolean value of flatness
     */
    public boolean isFlat() {
        return flat;
    }
}
