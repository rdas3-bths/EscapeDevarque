import java.awt.Rectangle;

public class Entity {
    protected int row;
    protected int column;
    protected int minDamage;
    protected int maxDamage;
    protected Rectangle hpBar;
    protected int drawX;
    protected int drawY;

    public Entity(int row, int column, boolean player) {
        this.row = row;
        this.column = column;
        minDamage = 1;
        maxDamage = 2;
        if (player)
            hpBar = new Rectangle(-100, -100, 178, 30);
        else
            hpBar = new Rectangle(-100, -100, 70, 10);
    }

    public void setDrawCoordinates(int x, int y) {
        drawX = x;
        drawY = y;
    }

    public int getDrawX() {
        return drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public Rectangle getHpBar() {
        return hpBar;
    }

    public void setHpBar(Rectangle hpBar) {
        this.hpBar = hpBar;
    }

    public String damageDisplay() {
        return minDamage + "-" + maxDamage;
    }
}
