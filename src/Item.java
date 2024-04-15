
public class Item {
    private int row;
    private int column;
    private boolean collected;


    public Item(int row, int column) {

        this.collected = false;
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected() {
        collected = true;
    }

}
