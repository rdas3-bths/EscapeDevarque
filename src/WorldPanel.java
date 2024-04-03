import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;

public class WorldPanel extends JPanel implements MouseListener {

    private Rectangle playAgain;
    private World world;

    public WorldPanel() {
        this.addMouseListener(this);
        world = new World();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 10;
        int y = 10;


        for (Tile[] row : world.getTiles()) {
            for (Tile t : row) {
                g.drawImage(t.getImage(), x, y, null);
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



}