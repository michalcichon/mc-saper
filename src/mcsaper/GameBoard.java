/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcsaper;

import java.awt.GridLayout;
import java.util.Random;


/**
 * Main game board for application
 * @author Michał Cichoń
 */
public class GameBoard extends javax.swing.JPanel {

    /**
     * Creates new form GameBoard
     */
    public GameBoard() {
        this(10,10);
    }
    
    /**
     * Game board constructor
     * @param size size of Map
     * @param numberOfMines number of mines on board
     */
    public GameBoard(int size, int numberOfMines) {
        this.size = size;
        this.numberOfMines = numberOfMines;
        this.setLayout(new GridLayout(this.size, this.size));
        this.gameBoard = new int[this.size][this.size];
        
        for(int i=0; i<this.size; i++) {
            for(int j=0; j<this.size; j++) {
                gameBoard[i][j] = 0;
            }
        }
        
        generate();
        initComponents();
    }
    
    /**
     * Generates random game board 
     */
    private void generate() {
        for(int i=0; i<numberOfMines; i++) {
            int row, column;
            Random rand = new Random();
            do {
                row = rand.nextInt(this.size);
                column = rand.nextInt(this.size);
            }while(gameBoard[row][column] == MINE);
            
            gameBoard[row][column] = MINE;
            
            if ( (row-1) != -1 && (column -1) != -1 && gameBoard[row-1][column-1] != MINE)
            gameBoard[row-1][column-1]++;

            if ( (row-1) != -1 && gameBoard[row-1][column] != MINE)
                gameBoard[row-1][column]++;

            if ( (row-1) != -1 && (column + 1) != 10 && gameBoard[row-1][column+1] != MINE)
                gameBoard[row-1][column+1]++;

            if ( (column -1) != -1 && gameBoard[row][column-1] != MINE)
                gameBoard[row][column-1]++;

            if ( (column + 1) != 10 && gameBoard[row][column+1] != MINE)
                gameBoard[row][column+1]++;

            if ( (row+1) != 10 && (column -1) != -1 && gameBoard[row+1][column-1] != MINE)
                gameBoard[row+1][column-1]++;

            if ( (row+1) != 10 && gameBoard[row+1][column] != MINE)
                gameBoard[row+1][column]++;

            if ( (row+1) != 10 && (column+1) != 10 && gameBoard[row+1][column+1] != MINE)
                gameBoard[row+1][column+1]++;
            }
    }
    
    /**
     * Check if there is mine in given position
     * @param row row of the button
     * @param column column of the button
     * @return 
     */
    public boolean isMine(int row, int column)
    {
        return gameBoard[row][column] == MINE;
    }

    /**
     * Get value of the button
     * @param row row of the button
     * @param column column of the button
     * @return 
     */
    public int getValue(int row, int column)
    {
        return gameBoard[row][column];
    }
    
    /**
     * Size of the game board. Each dimension has the same size, so width is 
     * equal to height
     */
    private int size;
    
    /**
     * Number of mines
     */
    private int numberOfMines;
    
    /**
     * Game board's data structure
     */
    private int[][] gameBoard;
    
    /**
     * Logical value for mines
     */
    private final int MINE = 9;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}