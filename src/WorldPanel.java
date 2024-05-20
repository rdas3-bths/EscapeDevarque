import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class WorldPanel extends JPanel implements MouseListener, KeyListener {

    private World world;
    private BufferedImage noVision;


    public WorldPanel() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        world = new World();
        try {
            noVision = ImageIO.read(new File("tiles/out_of_vision.png"));
        }
        catch (IOException e) {
            noVision = null;
            System.out.println(e);
        }
    }

    private void drawCheatMode(Graphics g) {
        int x = 10;
        int y = 10;

        int playerRow = world.getPlayer().getRow();
        int playerCol = world.getPlayer().getColumn();

        for (int row = 0; row < world.getTiles().length; row++) {
            for (int col = 0; col < world.getTiles()[0].length; col++) {
                Tile t = world.getTiles()[row][col];
                if (t.isVisible() || world.cheatMode()) {
                    g.drawImage(t.getImage(true), x, y, null);
                    if (row == world.getKey().getRow() && col == world.getKey().getColumn() && !world.getKey().isCollected()) {
                        g.drawImage(world.getKey().getImage(), x+2, y+7, null);
                    }
                    if (row == world.getShop().getRow() && col == world.getShop().getCol()) {
                        g.setFont(new Font("Courier New", Font.BOLD, 20));
                        g.drawImage(world.getShop().getImage(), x, y, null);
                    }
                    for (Coin c : world.getCoins()) {
                        if (row == c.getRow() && col == c.getColumn() && !c.isCollected()) {
                            g.drawImage(c.getImage(), x+5, y+2, null);
                        }
                    }
                    for (Enemy e : world.getEnemies()) {
                        if (row == e.getRow() && col == e.getColumn()) {
                            if (e.getCurrentHP() > 0)
                                g.drawImage(e.getImage(true), x+3, y+3, null);
                        }
                    }
                }
                if (row == playerRow && col == playerCol) {
                    g.drawImage(world.getPlayer().getImage(true), x+2, y+2, null);
                }
                x = x + 24;
            }
            x = 10;
            y = y + 24;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!world.cheatMode()) {
            int playerRow = world.getPlayer().getRow();
            int playerCol = world.getPlayer().getColumn();

            int startRow = playerRow - 4;
            int endRow = playerRow + 4;
            int startCol = playerCol - 4;
            int endCol = playerCol + 6;

            int x = 10;
            int y = 10;

            for (int i = startRow; i < endRow; i++) {
                for (int j = startCol; j < endCol; j++) {
                    boolean valid = world.validPosition(i, j);
                    if (valid) {
                        Tile t = world.getTiles()[i][j];
                        g.drawImage(t.getImage(false), x, y, null);
                    }
                    else {
                        g.drawImage(noVision, x, y, null);
                    }

                    if (i == playerRow && j == playerCol) {
                        g.drawImage(world.getPlayer().getImage(false), x, y, null);
                    }

                    for (Enemy e : world.getEnemies()) {
                        if (i == e.getRow() && j == e.getColumn()) {
                            if (e.getCurrentHP() > 0)
                                g.drawImage(e.getImage(false), x+3, y+3, null);
                        }
                    }

                    x = x + 93;
                }
                x = 10;
                y = y + 96;
            }

        }

        else {
            drawCheatMode(g);
        }


        g.setFont(new Font("Courier New", Font.BOLD, 15));

        g.drawString("Pickaxe Durability: " + world.getPlayer().getPickAxeDurability() + "/10", 1000, 20);

        g.drawString("Key collected: " + world.getKey().isCollected(), 1000, 50);

        g.drawString("Gold collected: " + world.getPlayer().getGold(), 1000, 80);

        Rectangle playerHpBar = world.getPlayer().getHpBar();
        playerHpBar.setLocation(1090, 90);
        drawHPBar(playerHpBar, g, world.getPlayer().getCurrentHP(), world.getPlayer().getMaxHP(), true);

        g.drawString("Player HP: " + world.getPlayer().healthDisplay(), 1000, 110);

        int position = 150;
        for (Enemy e : world.getEnemies()) {
            if (e.getCanSeePlayer()) {
                Rectangle hpBar = e.getHpBar();
                hpBar.setLocation(1082, position);
                drawHPBar(hpBar, g, e.getCurrentHP(), e.getMaxHP(), false);
                g.drawString("Enemy HP: " + e.healthDisplay(), 1000, position+20);
            }
            position += 40;
        }

        if (world.getShop().getBeingVisited()) {
            g.drawString("Welcome to the shop!", 1000, 500);
            g.drawString("Repair Pick Axe (5g)", 1000, 550);
            g.drawString("Heal 20HP (3g)", 1000, 600);
            g.drawRect((int)world.getShop().getRepairButton().getX(),
                    (int)world.getShop().getRepairButton().getY(),
                    (int)world.getShop().getRepairButton().getWidth(),
                    (int)world.getShop().getRepairButton().getHeight());
            g.drawRect((int)world.getShop().getHealButton().getX(),
                    (int)world.getShop().getHealButton().getY(),
                    (int)world.getShop().getHealButton().getWidth(),
                    (int)world.getShop().getHealButton().getHeight());
        }

        if (world.getPlayer().getCurrentHP() <= 0) {
            world.endGame();
        }

        if (world.isGameOver()) {
            if (world.getPlayer().getCurrentHP() <= 0) {
                g.drawString("GAME OVER! YOU DIED!", 1000, 750);
            }
            else {
                g.drawString("GAME OVER! YOU WIN!", 1000, 750);
            }

        }


    }

    public void drawHPBar(Rectangle hpBar, Graphics g, int currentHP, int maxHP, boolean player) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g.drawRect((int)hpBar.getX(), (int)hpBar.getY(),
                (int)hpBar.getWidth(), (int)hpBar.getHeight());
        double hp_percent = (double)currentHP/maxHP;
        double fill_width = hpBar.getWidth() * hp_percent;
        if (player)
            g2.setColor(Color.GREEN);
        else
            g2.setColor(Color.RED);
        g2.fillRect((int)hpBar.getX(), (int)hpBar.getY(),
                (int)fill_width, (int)hpBar.getHeight());
        g2.setColor(Color.BLACK);
    }

    public void mousePressed(MouseEvent e) {
        Point position = e.getPoint();
        if (world.getShop().getBeingVisited() && world.getShop().getRepairButton().contains(position)) {
            if (world.getPlayer().getGold() > 4) {
                world.getPlayer().spendGold(5);
                world.getPlayer().repairPickAxe();
            }
        }
        else if ((world.getShop().getBeingVisited() && world.getShop().getHealButton().contains(position))) {
            if (world.getPlayer().getGold() > 2) {
                world.getPlayer().heal(20);
                world.getPlayer().spendGold(3);
            }
        }
        else
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

        if (!world.isGameOver()) {
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
            if (key == '-') {
                world.flipCheat();
            }
        }

    }

}