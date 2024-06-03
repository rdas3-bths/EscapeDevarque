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
    private long startTime;
    private boolean gamePaused;

    public WorldPanel() {
        startTime = System.currentTimeMillis();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        gamePaused = true;
        world = new World();
        try {
            noVision = ImageIO.read(new File("tiles/out_of_vision.png"));
        }
        catch (IOException e) {
            noVision = null;
            e.printStackTrace();
        }
    }

    private void drawOverlay(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.GRAY);
        g2.fillRect(105, 100, 720, 500);
        g2.setColor(Color.BLACK);
        g2.drawRect(105, 100, 721, 501);
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("--- Game Paused ---", 225, 150);
        g.drawString("WASD to move", 225, 200);
        g.drawString("Move into enemies to attack!", 225, 250);
        g.drawString("Break walls with your pickaxe", 225, 300);
        g.drawString("Find the key, fight the boss, and find the exit!", 225, 450);
        g.drawString("Press ESC to unpause/pause", 225, 500);
    }

    private void drawMiniMap(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Stroke s = g2.getStroke();
        g2.setStroke(new java.awt.BasicStroke(3));
        g2.setColor(Color.darkGray);
        g.drawRect(948, 493, 325, 244);
        //g2.setStroke(new java.awt.BasicStroke(0));
        g2.setStroke(s);
        int x = 951;
        int y = 495;
        int delta = 8;
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 40; j++) {
                if (world.getTiles()[i][j].isVisible() || world.cheatMode()) {
                    if (world.getTiles()[i][j].getTileType() == 0) {
                        g2.setColor(Color.GRAY);
                    }
                    else if (world.getTiles()[i][j].getTileType() == 1) {
                        g2.setColor(Color.BLACK);
                    }
                    if (world.getTiles()[i][j].hasPlayer()) {
                        g2.setColor(Color.BLUE);
                    }
                    if (world.getTiles()[i][j].hasEnemy()) {
                        g2.setColor(Color.RED);
                    }
                    if (world.getTiles()[i][j].hasItem()) {
                        g2.setColor(Color.YELLOW);
                    }
                    if (world.getShop().getRow() == i && world.getShop().getCol() == j) {
                        g2.setColor(Color.MAGENTA);
                    }
                }
                else {
                    g2.setColor(Color.WHITE);
                }
                g.drawRect(x, y, delta, delta);
                g2.fillRect(x, y, delta, delta);
                g2.setColor(Color.BLACK);
                x += delta;
            }
            x = 951;
            y += delta;
        }
    }

    private void drawShop(Graphics g) {
        g.drawString("Welcome to the shop!", 1000, 500);
        g.drawString("Repair Pick Axe (5g)", 1000, 550);
        g.drawString("Heal 10HP (3g)", 1000, 600);
        g.drawString("+2 damage (10g)", 1000, 650);
        g.drawRect((int)world.getShop().getRepairButton().getX(),
                (int)world.getShop().getRepairButton().getY(),
                (int)world.getShop().getRepairButton().getWidth(),
                (int)world.getShop().getRepairButton().getHeight());
        g.drawRect((int)world.getShop().getHealButton().getX(),
                (int)world.getShop().getHealButton().getY(),
                (int)world.getShop().getHealButton().getWidth(),
                (int)world.getShop().getHealButton().getHeight());
        g.drawRect((int)world.getShop().getDamageButton().getX(),
                (int)world.getShop().getDamageButton().getY(),
                (int)world.getShop().getDamageButton().getWidth(),
                (int)world.getShop().getDamageButton().getHeight());
    }

    private void animate() {
        long currentTime = System.currentTimeMillis();
        double timeElapsed = (double)(currentTime-startTime)/1000;
        if (timeElapsed > 0.3) {
            startTime = System.currentTimeMillis();
            world.getPlayer().nextFrame();
            for (Enemy e : world.getEnemies()) {
                e.nextFrame();
            }
        }
    }

    public void paintComponent(Graphics g) {


        super.paintComponent(g);
        animate();
        int playerRow = world.getPlayer().getRow();
        int playerCol = world.getPlayer().getColumn();

        int startRow = playerRow - 4;
        int endRow = playerRow + 4;
        int startCol = playerCol - 4;
        int endCol = playerCol + 6;

        int xPos = 10;
        int yPos = 10;

        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                boolean valid = world.validPosition(i, j);
                if (valid) {
                    Tile t = world.getTiles()[i][j];
                    g.drawImage(t.getImage(false), xPos, yPos, null);
                }
                else {
                    g.drawImage(noVision, xPos, yPos, null);
                }

                if (i == playerRow && j == playerCol) {
                    g.drawImage(world.getPlayer().getImage(false), xPos+8, yPos+8, null);
                }
                if (i == world.getKey().getRow() && j == world.getKey().getColumn() && !world.getKey().isCollected()) {
                    g.drawImage(world.getKey().getImage(false), xPos+2, yPos+7, null);
                }
                if (i == world.getShop().getRow() && j == world.getShop().getCol()) {
                    g.setFont(new Font("Courier New", Font.BOLD, 20));
                    g.drawImage(world.getShop().getImage(false), xPos+13, yPos+25, null);
                }

                for (Enemy e : world.getEnemies()) {
                    if (i == e.getRow() && j == e.getColumn()) {
                        if (e.getCurrentHP() > 0) {
                            e.setDrawCoordinates(xPos + 3, yPos + 13);
                            g.drawImage(e.getImage(false), xPos + 3, yPos + 23, null);
                        }
                    }
                }

                for (Coin c : world.getCoins()) {
                    if (i == c.getRow() && j == c.getColumn() && !c.isCollected()) {
                        g.drawImage(c.getImage(false), xPos+15, yPos+10, null);
                    }
                }

                xPos = xPos + 93;
            }
            xPos = 10;
            yPos = yPos + 96;
        }

        g.setFont(new Font("Courier New", Font.BOLD, 15));

        Rectangle playerHpBar = world.getPlayer().getHpBar();
        playerHpBar.setLocation(1090, 10);
        drawHPBar(playerHpBar, g, world.getPlayer().getCurrentHP(), world.getPlayer().getMaxHP(), true, world.getPlayer());
        g.drawString("Player HP: " + world.getPlayer().healthDisplay(), 1000, 25);

        g.drawString("Key collected: " + world.getKey().isCollected(), 1000, 55);

        g.drawString("Boss slain: " + world.bossSlain(), 1000, 85);

        g.drawString("Gold collected: " + world.getPlayer().getGold(), 1000, 115);

        g.drawString("Pickaxe Durability: " + world.getPlayer().getPickAxeDurability() + "/10", 1000, 145);

        g.drawString("Damage: " + world.getPlayer().damageDisplay(), 1000, 175);

        int position = 150;
        for (Enemy e : world.getEnemies()) {
            if (e.getCanSeePlayer()) {
                Rectangle hpBar = e.getHpBar();
                hpBar.setLocation(1082, position);
                drawHPBar(hpBar, g, e.getCurrentHP(), e.getMaxHP(), false, e);
            }
            position += 40;
        }

        if (world.getShop().getBeingVisited()) {
            drawShop(g);
        }
        else {
            drawMiniMap(g);
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

        if (gamePaused) {
            drawOverlay(g);
        }


    }

    public void drawHPBar(Rectangle hpBar, Graphics g, int currentHP, int maxHP, boolean player, Entity e) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        if (e instanceof Enemy) {
            g.drawRect((int)e.getDrawX(), (int)e.getDrawY(),
                    (int)hpBar.getWidth(), (int)hpBar.getHeight());
        }
        else {
            g.drawRect((int)hpBar.getX(), (int)hpBar.getY(),
                    (int)hpBar.getWidth(), (int)hpBar.getHeight());
        }

        double hp_percent = (double)currentHP/maxHP;
        double fill_width = hpBar.getWidth() * hp_percent;
        if (player)
            g2.setColor(Color.GREEN);
        else
            g2.setColor(Color.RED);
        if (e instanceof Enemy) {
            g2.fillRect((int)e.getDrawX(), (int)e.getDrawY(),
                    (int)fill_width, (int)hpBar.getHeight());
            g2.setColor(Color.BLACK);
            g.setFont(new Font("Courier New", Font.BOLD, 10));
            g.drawString(((Enemy)e).healthDisplay(), e.getDrawX()+2, e.getDrawY()+7);
        }
        else {
            g2.fillRect((int)hpBar.getX(), (int)hpBar.getY(),
                    (int)fill_width, (int)hpBar.getHeight());
        }

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
                world.getPlayer().heal(10);
                world.getPlayer().spendGold(3);
            }
        }
        else if ((world.getShop().getBeingVisited() && world.getShop().getDamageButton().contains(position))) {
            if (world.getPlayer().getGold() > 10) {
                world.getPlayer().setMinDamage(world.getPlayer().getMinDamage()+2);
                world.getPlayer().setMaxDamage(world.getPlayer().getMaxDamage()+2);
                world.getPlayer().spendGold(10);
            }
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

        if ((int)key == 27) {
            gamePaused = !gamePaused;
        }

        if (!gamePaused) {
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

}