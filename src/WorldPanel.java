import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;


public class WorldPanel extends JPanel implements MouseListener, KeyListener {

    private World world;
    private Rectangle player_hp_bar;

    public WorldPanel() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
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
                if (t.isVisible() || world.cheatMode()) {
                    g.drawImage(t.getImage(), x, y, null);
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
                                g.drawImage(e.getImage(), x+3, y+3, null);
                        }
                    }
                }
                if (row == playerRow && col == playerCol) {
                    g.drawImage(world.getPlayer().getImage(), x+2, y+2, null);
                }
                x = x + 24;
            }
            x = 10;
            y = y + 24;
        }

        g.setFont(new Font("Courier New", Font.BOLD, 15));

        g.drawString("Pickaxe Durability: " + world.getPlayer().getPickAxeDurability() + "/10", 1000, 20);

        g.drawString("Key collected: " + world.getKey().isCollected(), 1000, 50);

        g.drawString("Gold collected: " + world.getPlayer().getGold(), 1000, 80);

        Rectangle playerHpBar = world.getPlayer().getPlayerHPBar();
        playerHpBar.setLocation(1090, 90);
        drawHPBar(playerHpBar, g, world.getPlayer().getCurrentHP(), world.getPlayer().getMaxHP(), true);

        g.drawString("Player HP: " + world.getPlayer().healthDisplay(), 1000, 110);

        int position = 150;
        for (Enemy e : world.getEnemies()) {
            if (e.getCanSeePlayer()) {
                Rectangle hpBar = e.getEnemyHpBar();
                hpBar.setLocation(1082, position);
                drawHPBar(hpBar, g, e.getCurrentHP(), e.getMaxHP(), false);
                g.drawString("Enemy HP: " + e.healthDisplay(), 1000, position+20);
            }
            position += 40;
        }

        if (world.getShop().getBeingVisited()) {
            g.drawString("Welcome to the shop!", 1000, 500);
            g.drawString("Repair Pick Axe", 1000, 550);
            g.drawRect((int)world.getShop().getRepairButton().getX(),
                    (int)world.getShop().getRepairButton().getY(),
                    (int)world.getShop().getRepairButton().getWidth(),
                    (int)world.getShop().getRepairButton().getHeight());
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
        else {
            world = new World();
        }

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