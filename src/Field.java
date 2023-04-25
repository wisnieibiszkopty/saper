public class Field {

    static public int discovered_fields = 0;
    final private int x_position;
    final private int y_position;
    private boolean is_mine = false;
    private boolean discovered = false;
    private boolean flagged = false;
    private boolean checked = false;
    private int number_of_mines;

    Field(int x, int y){
        this.x_position = x;
        this.y_position = y;
    }

    public int getX_position() {
        return x_position;
    }

    public int getY_position() {
        return y_position;
    }

    public boolean isMine() {
        return is_mine;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isChecked(){ return checked; }

    public int getNumber_of_mines() {
        return number_of_mines;
    }

    public void setIs_mine(boolean is_mine) {
        this.is_mine = is_mine;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
        discovered_fields++;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void setChecked(boolean checked){ this.checked = checked; }

    public void setNumber_of_mines(int number_of_mines) {
        this.number_of_mines = number_of_mines;
    }
}
