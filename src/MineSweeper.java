
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/** MineSweeper.java
 * 02/07/2017
 * @author Sam Te
 * 
 * Updated 25/01/19
 * - Added recursive function for 0 values that reveals panels near it.
 * - Messed up Win condition counter
 */
public class MineSweeper extends JFrame implements MouseListener
{
    private int rows;
    private int cols;
    private final int GAPS = 2;
    private int winCondition;
    private int clicks = 0;
    
    GamePanel[] gamePanels;
    JLabel[] gameLabels;
    
    public MineSweeper(int rows, int cols)
    {
        super("MineSweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((rows * 50), (cols * 50));
        setVisible(true);
        
        this.rows = rows;       
        this.cols = cols;
        setLayout(new GridLayout(rows, cols, GAPS, GAPS));
        
        createBoard(); // and add
        createBombs();
        setValues();
        addListeners();
    }
    
    public final void createBoard()
    {
        gamePanels = new GamePanel[rows * cols];
        gameLabels = new JLabel[rows * cols];
        int ro = 0;
        int co = 0;
        
        for (int i = 0; i < gamePanels.length; ++i)
        {
            gamePanels[i] = new GamePanel(ro, co);
            gamePanels[i].setBackground(Color.WHITE);
            add(gamePanels[i]);
            gameLabels[i] = new JLabel();
            gamePanels[i].add(gameLabels[i]);
            ++co;
            if (co % cols == 0)
            {
                ++ro;
                co = 0;
            }
        }
        this.validate();
    }
    
    public final void createBombs()
    {
        int max = rows * cols;
        int numBombs = 5; //max / 5;
        winCondition = max - numBombs;
        
        for (int i = 0; i < numBombs; ++i)
        {
            boolean placed;
            do
            {
                int rand = (int)(Math.random() * max);
                placed = false;
                if (gamePanels[rand].isBomb())
                {
                    
                }
                else
                {
                    gamePanels[rand].setBomb(true);
                    //gameLabels[rand].setText("BOMB");
                    placed = true;
                }
            } while (!placed);
        }
    }
    
    public final void setValues()
    {
        for (int i = 0; i < gamePanels.length; ++i)
        {
            if (gamePanels[i].isBomb())
            {
                int curRow = gamePanels[i].getRow();
                int curCol = gamePanels[i].getCol();
                
                int[] rowArray = {curRow - 1, curRow, curRow + 1};
                int[] colArray = {curCol - 1, curCol, curCol + 1};
                
                int position;
                for (int j = 0; j < rowArray.length; ++j)
                {
                    for (int k = 0; k < colArray.length; ++k)
                    {
                        if (rowArray[j] >= 0 && rowArray[j] < rows)
                        {
                            if ( colArray[k] >= 0 && colArray[k] < cols)
                            {
                                position = rowArray[j] * cols + colArray[k];
                                gamePanels[position].setValue(gamePanels[position].getValue() + 1);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public final void addListeners()
    {
        for (int i = 0; i < gameLabels.length; ++i)
        {
            gamePanels[i].addMouseListener(this);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent event)
    {
        Object source = event.getSource();
        
        // Left Mouse Click
        if(event.getButton() == MouseEvent.BUTTON1) 
        {
            boolean lose = false;

            for(int i = 0; i < gamePanels.length; ++i)
            {
                if (source == gamePanels[i])
                {
                    // test if the status of panel is free
                    if (gamePanels[i].getStatus() == 0){
                        // reveal will return a true of that panel is a bomb and false 
                        // if not
                        lose = reveal(gamePanels[i], gameLabels[i]);
                        
                        // if triggers a 0 panel then count number of revealed tiles
                        if (gamePanels[i].getValue() == 0)
                        {
                            clicks = countRevealed();
                        }
                        // if it is not a bomb then add to the win condition.
                        if (!lose){
                            ++clicks;
                        }
                    }
                }
            }
            
            
            // lose condition
            if (lose)
            {
                endGame();

            }

            //win condtion
            if (clicks == winCondition)
            {
                endGame();
                JOptionPane.showMessageDialog(null, "You have WON!!");
            }
        } // End of Left Mouse Click
        
        // Right Mouse Click
        if(event.getButton() == MouseEvent.BUTTON3) 
        {
            
            for(int i = 0; i < gamePanels.length; ++i)
            {
                if (source == gamePanels[i])
                {
                    switch(gamePanels[i].getStatus())
                    {
                        case 0:
                            gamePanels[i].setBackground(Color.RED);
                            gamePanels[i].setStatus(2);
                            break;
                        case 1:
                            break;
                        case 2:
                            gamePanels[i].setBackground(Color.WHITE);
                            gamePanels[i].setStatus(0);
                            break;
                    }
                }
            }
        } // end of Right Mouse Click
    }
    
    public void endGame()
    {
        revealAll();
    }
    
    /**
     * Returns a boolean value - True if input gamePanel is Bomb and false for 
     * Everything else
     * @param gamePanel
     * @param gameLabel
     * @return Boolean 
     */
    public boolean reveal(GamePanel gamePanel, JLabel gameLabel)
    {
        if (gamePanel.isBomb())
        {
            gameLabel.setText("BOMB");
            gamePanel.setBackground(Color.RED);
            gamePanel.setRevealed(true);
            gamePanel.setStatus(1); 
            return true;
        }
        else
        {
            gameLabel.setText("" + gamePanel.getValue());
            gamePanel.setBackground(Color.LIGHT_GRAY);
            gamePanel.setRevealed(true);
            gamePanel.setStatus(1);
                    
            // if the value is 0 
            // then Get the positions around the 0 value and reveal them 
            if(gamePanel.getValue() == 0)
            {
                int curRow = gamePanel.getRow();
                int curCol = gamePanel.getCol();
                
                int[] rowArray = {curRow - 1, curRow, curRow + 1};
                int[] colArray = {curCol - 1, curCol, curCol + 1};
                
                int position;
                for (int j = 0; j < rowArray.length; ++j)
                {
                    for (int k = 0; k < colArray.length; ++k)
                    {
                        if (rowArray[j] >= 0 && rowArray[j] < rows)
                        {
                            if (colArray[k] >= 0 && colArray[k] < cols)
                            {
                                position = rowArray[j] * cols + colArray[k];
//                                if (gamePanels[position].isRevealed())
//                                {
//                                    // if it is already revealed then do nothing
//                                }
//                                else
//                                {
//                                    // is not revealed then reveal it
//                                    reveal(gamePanels[position], gameLabels[position]);
//                                }

                                switch(gamePanels[position].getStatus())
                                {
                                    case 0:
                                        reveal(gamePanels[position], gameLabels[position]);
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
    
    public int countRevealed()
    {
        int count = 0;
        for (int i = 0; i < rows * cols; ++i)
        {
            if(gamePanels[i].getStatus() == 1)
            {
                ++count;
            }
        }
        System.out.println(count);
        return count;
    }
    
    public void findPosition(int row, int col)
    {
        
    }
    
    public void revealAll()
    {
        for (int i = 0; i < gamePanels.length; ++i)
        {
            if (gamePanels[i].isBomb())
            {
                gameLabels[i].setText("BOMB");
                gamePanels[i].setBackground(Color.RED);
                gamePanels[i].setRevealed(true);
            }
            else
            {
                gameLabels[i].setText("" + gamePanels[i].getValue());
                gamePanels[i].setBackground(Color.LIGHT_GRAY);
            }
        }
        
    }
    
//    public void changePanel(GamePanel panel)
//    {
//        if (panel.isBomb())
//        {
//            gameLabels[i].setText("BOMB");
//            gamePanels[i].setBackground(Color.RED);
//        }
//        else
//        {
//            gameLabels[i].setText("" + gamePanels[i].getValue());
//            gamePanels[i].setBackground(Color.LIGHT_GRAY);
//        }
//    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
    
    public static void main(String[] args)
    {
//        int rows = Integer.parseInt(JOptionPane.showInputDialog("Enter Rows"));
//        int cols = Integer.parseInt(JOptionPane.showInputDialog("Enter Columns"));
        int rows = 10;
        int cols = 10;
        MineSweeper frame = new MineSweeper(rows, cols);
    }
    
}
