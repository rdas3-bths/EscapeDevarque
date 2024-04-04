import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;


public class WorldPanel extends JPanel implements MouseListener, KeyListener {

    private Rectangle playAgain;
    private World world;

    public WorldPanel() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        world = new World();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 10;
        int y = 10;

        int playerRow = world.getPlayer().getRow();
        int playerCol = world.getPlayer().getColumn();

        for (int row = 0; row < world.getTiles().length; row++) {
            for (int col = 0; col < world.getTiles()[0].length; col++) {
                Tile t = world.getTiles()[row][col];
                g.drawImage(t.getImage(), x, y, null);
                if (row == playerRow && col == playerCol) {
                    g.drawImage(world.getPlayer().getImage(), x+2, y+2, null);
                }
                x = x + 24;
            }
            x = 10;
            y = y + 24;
        }

    }

    public void mousePressed(MouseEvent e) {
        world = new World();
    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();

        if (key == 'w') {
            world.movePlayer("N");
        }
        if (key == 'a') {
            world.movePlayer("W");
        }
        if (key == 's') {
            world.movePlayer("S");
        }
        if (key == 'd') {
            world.movePlayer("E");
        }
    }

}