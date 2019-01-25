
import javax.swing.JPanel;

/**
 * 02/07/2017
 * @author Sam Te
 */
public class GamePanel extends JPanel
{
    private int value;
    private boolean bomb;
    private int row;
    private int col;
    private boolean revealed; // default value of false
    
    public GamePanel(int row, int col)
    {
        super();
        value = 0;
        bomb = false;
        revealed = false;
        this.row = row;
        this.col = col;
    }

    public boolean isBomb()
    {
        return bomb;
    }

    public void setBomb(boolean bomb)
    {
        this.bomb = bomb;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }
    
    public boolean isRevealed()
    {
        return revealed;
    }
    
    public void setRevealed(boolean revealed)
    {
        this.revealed = revealed;
    }
    
    
}
