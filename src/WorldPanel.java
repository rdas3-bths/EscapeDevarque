import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;

public class WorldPanel extends JPanel implements MouseListener {

    private Rectangle playAgain;
    private Tile[][] maze;

    public WorldPanel() {
        this.addMouseListener(this);
        maze = World.generateWorld();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 10;
        int y = 10;


        for (Tile[] row : maze) {
            for (Tile t : row) {
                g.drawImage(t.getImage(), x, y, null);
                x = x + 24;
            }
            x = 10;
            y = y + 24;
        }

    }

    public void mousePressed(MouseEvent e) {
        maze = World.generateWorld();
    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }



}