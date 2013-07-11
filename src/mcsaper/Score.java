/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcsaper;

import java.io.Serializable;

/**
 * Score class, which included name and score
 * @author Michał Cichoń
 */
public class Score implements Serializable {

    /**
     * Name setter
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Score value setter
     * @param value 
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Name getter
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Score value getter
     * @return 
     */
    public int getValue() {
        return value;
    }
    
    private String name;
    private int value;
    
}
