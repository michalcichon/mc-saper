/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcsaper;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Mine class of the game
 * @author Michał Cichoń
 */
public class McSaperMainFrame extends javax.swing.JFrame {
    
    /**
     * This listener is helpful to handle click events on the buttons
     */
    class MineButtonMouseListener extends MouseAdapter {
            boolean pressed;
            MineButton button;
            int x;
            int y;
            
            /**
             * Constructor of the listener. Set button reference and its coordinates
             * @param button
             * @param x
             * @param y 
             */
            public MineButtonMouseListener(MineButton button, int x, int y) {
                this.button = button;
                this.x = x;
                this.y = y;
            }

            /**
             * Standard mousePressed event handler
             * @param e mouse event
             */
            @Override
            public void mousePressed(MouseEvent e) {
                button.getModel().setArmed(true);
                button.getModel().setPressed(true);
                pressed = true;
            }

            /**
             * Mouse released event. Check if we have right clicked or left clicked
             * @param e passed mouse event
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                button.getModel().setArmed(false);
                button.getModel().setPressed(false);

                if (pressed) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        hasRightClicked(x, y);
                    }
                    else {
                        revealCell(x, y);
                    }
                }
                
                pressed = false;

            }

            /**
             * Mouse exited
             * @param e mouse event
             */
            @Override
            public void mouseExited(MouseEvent e) {
                pressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pressed = true;
            }                    
        }
    

    /**
     * Creates new form McSaperMainFrame
     */
    public McSaperMainFrame() {
        initComponents();
        boardSize = 10;
        numberOfMines = 10;
        finished = false;
        started = false;
        flagsCounter = 0;
        cellsCleared = 0;
        currentTime = 0;
        timerScore = 0;
        minesFlagged = 0;
        status = new int[boardSize][boardSize];
        buttons = new MineButton[boardSize][boardSize];
        scores = new ArrayList<>();
        
        ActionListener listener = new ActionListener(){

            /**
             * Standard action performed action
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer timerTemp = new Integer(timerLabel.getText()) + 1;
                timerLabel.setText(timerTemp.toString());
            }
            
        };
        
        timer = new Timer(1000, listener); 
        
        for(int i=0; i<boardSize; i++) {
            for(int j=0; j<boardSize; j++) {
                status[i][j] = BLANK_CELL;
                final MineButton button = new MineButton("");
                button.setForeground(Color.BLACK);
                button.setBackground(new Color(241, 241, 241));
                Border line = new LineBorder(new Color(100,100,100));
                Border margin = new EmptyBorder(5, 5, 5, 5);
                Border compound = new CompoundBorder(line, margin);
                button.setBorder(compound);
                button.setText("");
                button.addMouseListener(new MineButtonMouseListener(button, i, j));
                 
                this.buttons[i][j] = button;
                MainFramePanel.add(this.buttons[i][j]);
                
            }
        }
        
        flagsToGo.setText(String.valueOf(boardSize));
        timerLabel.setText("0");
    }
    
    /**
     * Reveal cell
     * @param i x coordinate
     * @param j y coordinate
     */
    public void revealCell(int i, int j) {
        if(finished){
            return;
        } else {
            cellsCleared++;
        }
        
        if(status[i][j] == FLAGGED_CELL || status[i][j] == QUESTION_CELL) {
            cellsCleared--;
            return;
        }
        
        MineButton button = buttons[i][j];
        GameBoard gb = (GameBoard) MainFramePanel;
        
        if(button.isFlat()){
            cellsCleared--;
            return;
        }
        
        if(cellsCleared == 90 && gb.getValue(i, j) != MINE) {
            leftClickButtonChange(button, i, j);
            won();
            return;
        }
        
        if(gb.getValue(i, j) == 0) {
            cellsCleared--;
            clear(i, j, true);
        }
        
        leftClickButtonChange(button, i, j);
        
        button.setFlat(true);
        
        if(gb.isMine(i, j)) {
            lost();
            cellsCleared--;
            return;
        }
        
        if(!started) {
            timer.start();
            started = true;
        }
    }
    
    /**
     * Left click action
     * @param button clicked button
     * @param i x coordinate
     * @param j y coordinate
     */
    public void leftClickButtonChange(MineButton button, int i, int j) {
        GameBoard gb = (GameBoard) MainFramePanel;
         button.setIcon(null);
        if(gb.getValue(i, j) == 0){
            button.setFlat(true);
            button.setText("");
        } else if(gb.getValue(i,j) == 1) {
            button.setText("1");
            button.setForeground(new Color(0,220,10));
        } else if(gb.getValue(i,j) == 2) {
            button.setText("2");
            button.setForeground(Color.blue);
        } else if(gb.getValue(i,j) == 3) {
            button.setText("3");
            button.setForeground(Color.darkGray);
        } else if(gb.getValue(i,j) == 4) {
            button.setText("4");
            button.setForeground(Color.orange);
        } else if(gb.getValue(i,j) == 5) {
            button.setText("5");
            button.setForeground(Color.pink);
        } else if(gb.getValue(i,j) == 6) {
            button.setText("6");
            button.setForeground(Color.red);
        } else if(gb.getValue(i,j) == 7) {
            button.setText("7");
            button.setForeground(Color.magenta);
        } else if(gb.getValue(i,j) == 8) {
            button.setText("8");
            button.setForeground(Color.black);
        } else if(gb.getValue(i, j) == MINE) {
            button.setText("");
            Image img = new ImageIcon(getClass().getResource("/resources/explosion.png")).getImage();
            Image scaledImage = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        }
    }
    
    /**
     * We have right clicked
     * @param i x coordinate
     * @param j y coordinate
     */
    public void hasRightClicked(int i, int j) {
        
        if(finished) {
            return;
        }
        
        if(!started){
            timer.start();
            started = true;
        }
        
        GameBoard gb = (GameBoard) MainFramePanel;
        MineButton button = buttons[i][j];
        if(!button.isFlat()){
            if(status[i][j] == BLANK_CELL){
                flagsCounter++;
                
                if(flagsCounter > numberOfMines) {
                    flagsToGo.setForeground(new Color(255,0,0));
                } else {
                    flagsToGo.setForeground(new Color(51,102,0));
                }
                
                flagsToGo.setText(String.valueOf(numberOfMines - flagsCounter));
                
                status[i][j] = FLAGGED_CELL;
                
                button.setFlat(false);
                
                Image img = new ImageIcon(getClass().getResource("/resources/flag-red.png")).getImage();
                Image scaledImage = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
                button.setText("");
                
                if(gb.getValue(i, j) == MINE) {
                    minesFlagged++;
                }
                
            }else if (status[i][j] == FLAGGED_CELL){
                flagsCounter--;
                flagsToGo.setText(String.valueOf(numberOfMines - flagsCounter));

                button.setFlat(false);
                status[i][j] = QUESTION_CELL;
                
                Image img = new ImageIcon(getClass().getResource("/resources/question_mark.png")).getImage();
                Image scaledImage = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
                button.setText("");
                
            }else if (status[i][j] == QUESTION_CELL){
                flagsToGo.setText(String.valueOf(numberOfMines - flagsCounter));
                button.setFlat(false);
                status[i][j] = BLANK_CELL;
                button.setIcon(null);
            }
        }else{
            button.setFlat(true);
        }
    }
    
    /**
     * We won!
     */
    void won(){
        timer.stop();
        finished = true;
        GameBoard gb = (GameBoard) MainFramePanel;
        
        for(int i = 0; i<boardSize; i++) {
            for(int j = 0; j<boardSize; j++) {
                MineButton button = buttons[i][j];
                if(button.isFlat() == false && gb.getValue(i, j) == MINE) {
                    button.setFlat(true);
                    button.setText("");
                    Image img = new ImageIcon(getClass().getResource("/resources/happy.png")).getImage();
                    Image scaledImage = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(scaledImage));
                }
            }
        }
        
        String name = JOptionPane.showInputDialog(null, "Podaj swoje imię:", "Wygrałeś!", 1);
        
        if(name != null) {
            saveScore(name, timerLabel.getText());
            displayHighscores();
        }
    }
    
    /**
     * Method to display high scores
     */
    void displayHighscores() {
        StringBuilder highscores = new StringBuilder();
        
        if(scores.isEmpty()){
            highscores.append("Brak wyników. Musisz zagrać! ;-)");
        }else {
            highscores.append("Najlepsze wyniki:\n");
            for(Score score: scores) {
                highscores.append(score.getName()).append(" ").append(score.getValue()).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, highscores.toString(), "Najlepsze wyniki", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Saves high score
     * @param name given name of the player
     * @param score score of the game, depends on the timer
     */
    void saveScore(String name, String score) {
        Score newScore = new Score();
        newScore.setName(name);
        newScore.setValue(new Integer(score));
        scores.add(newScore);
        
        Collections.sort(scores, new Comparator<Score>(){

            @Override
            public int compare(Score o1, Score o2) {
                return o1.getValue() - o2.getValue();
            }
        
        });
        
    }
    
    /**
     * We lost... :-(
     */
    void lost(){
        timer.stop();
        finished = true;
        GameBoard gb = (GameBoard) MainFramePanel;
        
        for(int i = 0; i<boardSize; i++) {
            for(int j = 0; j<boardSize; j++) {
                MineButton button = buttons[i][j];
                
                if(!button.isFlat() && gb.getValue(i, j) == MINE) {
                    button.setFlat(true);
                    button.setText("");
                    if(status[i][j] == FLAGGED_CELL) {
                        Image img = new ImageIcon(getClass().getResource("/resources/happy.png")).getImage();
                        Image scaledImage = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledImage));
                    } else {
                        Image img = new ImageIcon(getClass().getResource("/resources/bomb.png")).getImage();
                        Image scaledImage = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledImage));
                    }
                }
            }
        }
    }
    
    /**
     * Clear game board
     * @param i x coordinate
     * @param j y coordinate
     * @param allowedClear should we clear the space or not
     */
    void clear(int i, int j, boolean allowedClear){
        GameBoard gb = (GameBoard) MainFramePanel;
        MineButton button = buttons[i][j];
        
        if(button.isFlat() == false && gb.getValue(i, j) != MINE && allowedClear && status[i][j] != FLAGGED_CELL) {
            button.setFlat(true);
            leftClickButtonChange(button, i, j);

            cellsCleared++;

            if (cellsCleared == 90 && gb.getValue(i, j) != MINE)
            {
                leftClickButtonChange(button, i, j);
                won();
                return;
            }

            if (gb.getValue (i, j) == 0)
                allowedClear = true;
            else
                allowedClear = false;

            if ((i-1) != -1 && (j -1) != -1)
                clear(i-1, j-1, allowedClear);

            if ((i-1) != -1)
                clear(i-1, j, allowedClear);

            if ((i-1) != -1 && (j + 1) != 10)
                clear(i-1, j+1, allowedClear);

            if ((j -1) != -1)
                clear(i, j-1, allowedClear);

            if ((j + 1) != boardSize)
                clear(i, j+1, allowedClear);

            if ((i+1) != boardSize && (j -1) != -1)
                clear(i+1, j-1, allowedClear);

            if ((i+1) != boardSize)
                clear(i+1, j, allowedClear);

            if ((i+1) != boardSize && (j+1) != boardSize)
            {
                clear(i+1, j+1, allowedClear);
            }
        }
    }
    
    /**
     * Resets the game board
     */
    void reset(){
        finished = false;
        cellsCleared = 0;
        flagsCounter = 0;
        minesFlagged = 0;
        started = false;
        currentTime = 0;
        timer.stop();
        timerLabel.setText("0");
        flagsToGo.setText("10");
        
        MainFramePanel = new GameBoard();
        
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++ )
            {
                MineButton button = buttons[i][j];
                button.setFlat(false);
                button.setFlat(false);
                button.setText("");
                button.setIcon(null);
                status[i][j] = BLANK_CELL;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainFramePanel = new GameBoard();
        ToolPanel = new javax.swing.JPanel();
        newGameButton = new javax.swing.JButton();
        flagsToGo = new javax.swing.JLabel();
        timerLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        MainFrameMenu = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        newGameMenuItem = new javax.swing.JMenuItem();
        scoresMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        abutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("mcSaper");

        MainFramePanel.setLayout(new java.awt.GridLayout(10, 10));

        newGameButton.setBackground(new java.awt.Color(241, 241, 241));
        newGameButton.setForeground(new java.awt.Color(241, 241, 241));
        newGameButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/new.png"))); // NOI18N
        newGameButton.setAlignmentY(0.0F);
        newGameButton.setBorder(null);
        newGameButton.setFocusable(false);
        newGameButton.setOpaque(false);
        newGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameButtonActionPerformed(evt);
            }
        });

        flagsToGo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        flagsToGo.setForeground(new java.awt.Color(51, 102, 0));
        flagsToGo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        flagsToGo.setText("10");

        timerLabel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        timerLabel.setText("0");

        jLabel1.setText("Flagi:");

        jLabel2.setText("Czas:");

        javax.swing.GroupLayout ToolPanelLayout = new javax.swing.GroupLayout(ToolPanel);
        ToolPanel.setLayout(ToolPanelLayout);
        ToolPanelLayout.setHorizontalGroup(
            ToolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ToolPanelLayout.createSequentialGroup()
                .addContainerGap(190, Short.MAX_VALUE)
                .addComponent(newGameButton)
                .addGap(63, 63, 63)
                .addGroup(ToolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(timerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(flagsToGo, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addContainerGap())
        );
        ToolPanelLayout.setVerticalGroup(
            ToolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newGameButton)
            .addGroup(ToolPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ToolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flagsToGo)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ToolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timerLabel)
                    .addComponent(jLabel2)))
        );

        FileMenu.setText("Plik");

        newGameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newGameMenuItem.setText("Nowa gra");
        newGameMenuItem.setName("newGameMenuItem"); // NOI18N
        newGameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(newGameMenuItem);

        scoresMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        scoresMenuItem.setText("Wyniki");
        scoresMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scoresMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(scoresMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Wyjście");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(exitMenuItem);

        MainFrameMenu.add(FileMenu);

        HelpMenu.setText("Pomoc");

        abutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        abutMenuItem.setText("O programie");
        abutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abutMenuItemActionPerformed(evt);
            }
        });
        HelpMenu.add(abutMenuItem);

        MainFrameMenu.add(HelpMenu);

        setJMenuBar(MainFrameMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainFramePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ToolPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(ToolPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MainFramePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameMenuItemActionPerformed
        reset();
    }//GEN-LAST:event_newGameMenuItemActionPerformed

    private void newGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameButtonActionPerformed
        reset();
    }//GEN-LAST:event_newGameButtonActionPerformed

    private void abutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abutMenuItemActionPerformed
        String about = "mcSaper\n"
                + "Klon gry Saper napisany przy użyciu biblioteki Swing\n"
                + "Autor: Michał Cichoń, AGH 2013";
        JOptionPane.showMessageDialog(null, about, "O programie...", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_abutMenuItemActionPerformed

    private void scoresMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scoresMenuItemActionPerformed
        displayHighscores();
    }//GEN-LAST:event_scoresMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(McSaperMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(McSaperMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(McSaperMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(McSaperMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new McSaperMainFrame().setVisible(true);
            }
        });
    }
    
    private int boardSize;
    private int numberOfMines;
    private boolean finished;
    private boolean started;
    private int flagsCounter;
    private int cellsCleared;
    private int currentTime;
    private int timerScore;
    private int minesFlagged;
    private int status[][];
    private MineButton[][] buttons;
    private Timer timer;
    private ArrayList<Score> scores;
    
    private final int BLANK_CELL = 0;
    private final int FLAGGED_CELL = 1;
    private final int QUESTION_CELL = 2;
    private final int MINE = 9;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JMenuBar MainFrameMenu;
    private javax.swing.JPanel MainFramePanel;
    private javax.swing.JPanel ToolPanel;
    private javax.swing.JMenuItem abutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JLabel flagsToGo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton newGameButton;
    private javax.swing.JMenuItem newGameMenuItem;
    private javax.swing.JMenuItem scoresMenuItem;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables
}
