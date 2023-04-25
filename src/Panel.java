import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Panel extends JPanel{
    static final int SCREEN_WIDTH = 500;
    static final int SCREEN_HEIGHT = 500;
    static final int FIELD_UNITS = 10;
    static Field[][] fields = new Field[FIELD_UNITS][FIELD_UNITS];
    static final int UNIT_SIZE = SCREEN_WIDTH/FIELD_UNITS;
    int mines = 20;
    boolean game_over = false;
    boolean running = true;
    boolean first_try = true;
    Random random;
    final Color light_green = new Color(124,252,0);
    final Color green = new Color(0,128,0);
    final Color light_orange = new Color(255,204,0);
    final Color orange = new Color(255,153,0);
    Map<Integer, Color> numbers = new TreeMap<>();

    Panel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addMouseListener(new myAdapter());
        random = new Random();
        generateMines();
        fillMap();
    }

    void fillMap(){
        numbers.put(1, Color.blue);
        numbers.put(2, Color.green);
        numbers.put(3, Color.red);
        numbers.put(4, Color.magenta);
        numbers.put(5, Color.yellow);
        numbers.put(6, Color.red);
        numbers.put(7, Color.blue);
        numbers.put(8, Color.green);
    }

    void gameEnd(Graphics g){
        if(!running){
            String message;

            if(game_over) {
               message = "Game Over";
                for (int i = 0; i < FIELD_UNITS; i++) {
                    for (int j = 0; j < FIELD_UNITS; j++) {
                        if (fields[i][j].isMine()) {
                            g.setColor(Color.red);
                            g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                            drawMine(g,i,j);
                        }
                    }
                }
            }

            else {
                message = "Wygrana!";
                for (int i = 0; i < FIELD_UNITS; i++) {
                    for (int j = 0; j < FIELD_UNITS; j++) {
                        if (fields[i][j].isMine()) {
                            if((i%2!=0 && j%2==0) || (i%2==0 && j%2!=0)) g.setColor(light_green);
                            else g.setColor(green);
                            g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        }
                    }
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Verdana", Font.BOLD, SCREEN_WIDTH / 8));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(message, (SCREEN_WIDTH - metrics.stringWidth(message)) / 2, SCREEN_HEIGHT / 2);

        }
    }

    void drawFlag(Graphics g, int i, int j){
        if(fields[i][j].isFlagged() && !fields[i][j].isDiscovered()){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke((float)UNIT_SIZE/15));
            g2.draw(new Line2D.Float(i*UNIT_SIZE+(float)UNIT_SIZE/2, j*UNIT_SIZE+(float)UNIT_SIZE/4,
                    i*UNIT_SIZE+(float)UNIT_SIZE/2, j*UNIT_SIZE+UNIT_SIZE));

            g.setColor(Color.red);
            Polygon triangle = new Polygon();
            triangle.addPoint(i*UNIT_SIZE+UNIT_SIZE/2, j*UNIT_SIZE+UNIT_SIZE/4);
            triangle.addPoint(i*UNIT_SIZE+UNIT_SIZE/2, j*UNIT_SIZE+UNIT_SIZE*3/4);
            triangle.addPoint(i*UNIT_SIZE, j*UNIT_SIZE+UNIT_SIZE/2);
            g.fillPolygon(triangle);
        }
    }

    void drawMine(Graphics g, int i, int j){
        g.setColor(Color.gray);
        g.fillOval(i*UNIT_SIZE+UNIT_SIZE/8, j*UNIT_SIZE+UNIT_SIZE/8, UNIT_SIZE*3/4, UNIT_SIZE*3/4);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke((float)UNIT_SIZE/20));

        g2.draw(new Line2D.Float(i*UNIT_SIZE+(float)UNIT_SIZE/16,j*UNIT_SIZE+(float)UNIT_SIZE/2,
                i*UNIT_SIZE+(float)UNIT_SIZE*15/16,j*UNIT_SIZE+(float)UNIT_SIZE/2));

        g2.draw(new Line2D.Float(i*UNIT_SIZE+(float)UNIT_SIZE/2,j*UNIT_SIZE+(float)UNIT_SIZE/16,
                i*UNIT_SIZE+(float)UNIT_SIZE/2,j*UNIT_SIZE+(float)UNIT_SIZE*15/16));

        g2.draw(new Line2D.Float(i*UNIT_SIZE+(float)UNIT_SIZE*3/16, j*UNIT_SIZE+(float)UNIT_SIZE*3/16,
                i*UNIT_SIZE+(float)UNIT_SIZE*13/16, j*UNIT_SIZE+(float)UNIT_SIZE*13/16));

        g2.draw(new Line2D.Float(i*UNIT_SIZE+(float)UNIT_SIZE*3/16, j*UNIT_SIZE+(float)UNIT_SIZE*13/16,
                i*UNIT_SIZE+(float)UNIT_SIZE*13/16, j*UNIT_SIZE+(float)UNIT_SIZE*3/16));
    }

    public void draw(Graphics g){

        for(int i=0;i<FIELD_UNITS;i++){
            for(int j=0;j<FIELD_UNITS;j++){

                if((i%2!=0 && j%2==0) || (i%2==0 && j%2!=0)){
                    if(fields[i][j].isDiscovered()) g.setColor(light_orange);
                    else g.setColor(light_green);
                }
                else{
                    if(fields[i][j].isDiscovered()) g.setColor(orange);
                    else g.setColor(green);
                }

                g.fillRect(i*UNIT_SIZE,j*UNIT_SIZE,UNIT_SIZE,UNIT_SIZE);

                // rysowanie cyfr na polach
                if(fields[i][j].isDiscovered()){
                    int number = fields[i][j].getNumber_of_mines();
                    Color c = numbers.get(number);
                    g.setColor(c);
                    g.setFont(new Font("Times New Roman",Font.BOLD,UNIT_SIZE));
                    int x = i*UNIT_SIZE + UNIT_SIZE/4;
                    int y = j*UNIT_SIZE + UNIT_SIZE - UNIT_SIZE/5;
                    g.drawString(Integer.toString(number),x,y);
                }
                drawFlag(g, i ,j);
            }
        }
        gameEnd(g);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // potwór ale działa
    int countMines(Field f){
        int num_of_mines = 0;

        for(int i=f.getX_position()-1; i<f.getX_position()+2; i++)
            try { if (fields[i][f.getY_position()-1].isMine()) num_of_mines++; }
            catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }

        try { if (fields[f.getX_position()-1][f.getY_position()].isMine()) num_of_mines++; }
        catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }

        try{ if(fields[f.getX_position()+1][f.getY_position()].isMine()) num_of_mines++; }
        catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }

        for(int i=f.getX_position()-1; i<f.getX_position()+2; i++)
            try{ if (fields[i][f.getY_position()+1].isMine()) num_of_mines++; }
            catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }

        return num_of_mines;
    }

    void generateMines(){

        for(int i=0;i<FIELD_UNITS;i++){
            for(int j=0;j<FIELD_UNITS;j++){
                fields[i][j] = new Field(i, j);
            }
        }

        int i, j, iteration=0;
        do{
            i = random.nextInt(FIELD_UNITS);
            j = random.nextInt(FIELD_UNITS);
            if(!fields[i][j].isMine()){
                fields[i][j].setIs_mine(true);
                iteration++;
            }
        }while(iteration != mines);

        // Teraz można się odwoływać do getera min
        for(int k=0;k<FIELD_UNITS;k++){
            for(int l=0;l<FIELD_UNITS;l++){
                fields[k][l].setNumber_of_mines(countMines(fields[k][l]));
            }
        }
    }

    void replaceMine(Field f){
        int i, j;
        boolean move_on = false;
        do{
            i = random.nextInt(FIELD_UNITS);
            j = random.nextInt(FIELD_UNITS);
            if(!fields[i][j].isMine()){
                fields[i][j].setIs_mine(true);
                move_on = true;
            }
        }while(!move_on);
        f.setIs_mine(false);

        for(int k=0;k<FIELD_UNITS;k++){
            for(int l=0;l<FIELD_UNITS;l++){
                fields[k][l].setNumber_of_mines(countMines(fields[k][l]));
            }
        }
    }

    void discoverZeros(Field f){
        f.setChecked(true);
        int x = f.getX_position();
        int y = f.getY_position();
        Field[] check = {
                fields[x-1][y-1],
                fields[x][y-1],
                fields[x+1][y-1],
                fields[x-1][y],
                fields[x+1][y],
                fields[x-1][y+1],
                fields[x][y+1],
                fields[x+1][y+1]
        };

        for(int i=0; i<8; i++){
            try {
                if (!check[i].isDiscovered() && !check[i].isMine() && !check[i].isChecked() && f.getNumber_of_mines() == 0) {
                    check[i].setDiscovered(true);
                    if(check[i].getNumber_of_mines() == 0) discoverZeros(check[i]);
                    if (check[i].isFlagged()) check[i].setFlagged(true);
                }
            }
            catch(ArrayIndexOutOfBoundsException e){ System.err.println("Znowu nic się nie dzieje");}
        }
    }

    void checkIfCanBeDiscovered(Field field, Field f){
        try {
            if (!f.isDiscovered() && !f.isMine() && !f.isChecked() && running && field.getNumber_of_mines() == 0) {
                f.setDiscovered(true);
                if(f.getNumber_of_mines() == 0) discoverZeros(f);
                if (f.isFlagged()) f.setFlagged(true);
            }
        }
        catch(ArrayIndexOutOfBoundsException e){ System.err.println("Znowu nic się nie dzieje");}
    }

    void discoverZero(Field f){
        for(int i=f.getX_position()-1; i<f.getX_position()+2; i++)
            try { checkIfCanBeDiscovered(f, fields[i][f.getY_position()-1]); }
            catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }

        try { checkIfCanBeDiscovered(f, fields[f.getX_position()-1][f.getY_position()]); }
        catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }

        try{ checkIfCanBeDiscovered(f, fields[f.getX_position()+1][f.getY_position()]); }
        catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }
        
        for(int i=f.getX_position()-1; i<f.getX_position()+2; i++)
            try{ checkIfCanBeDiscovered(f, fields[i][f.getY_position()+1]); }
            catch(ArrayIndexOutOfBoundsException e) {System.err.println("Nic się nie dzieje"); }
    }

    void isRunning(){
        if(Field.discovered_fields + mines == FIELD_UNITS*FIELD_UNITS) running = false;
    }

    class myAdapter extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e){
            // Zamieniamy współrzędne na indeksy pola
            int x = e.getX();
            int y = e.getY();
            x = (x - (x % UNIT_SIZE)) / UNIT_SIZE;
            y = (y - (y % UNIT_SIZE)) / UNIT_SIZE;

            // Jeśli zostanie naciśnięty lewy przycisk myszy odkrywamy pole
            // i sprawdzamy czy trafiliśmy na minę
            if(e.getButton() == MouseEvent.BUTTON1 && running) {
                if(first_try){
                    if(fields[x][y].isMine()) replaceMine(fields[x][y]);
                    first_try = false;
                    fields[x][y].setDiscovered(true);
                }
                else {
                    fields[x][y].setDiscovered(true);
                    if (fields[x][y].isMine()){
                        game_over = true;
                        running = false;
                    }
                }
                discoverZero(fields[x][y]);
            }

            else if(e.getButton() == MouseEvent.BUTTON3 && running){
                if(!fields[x][y].isFlagged()) {
                    if (!fields[x][y].isDiscovered()) fields[x][y].setFlagged(true);
                }
                else{
                    if (!fields[x][y].isDiscovered()) fields[x][y].setFlagged(false);
                }
            }

            isRunning();
            repaint();
        }
    }

}
