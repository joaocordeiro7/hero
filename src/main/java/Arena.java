import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TerminalSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    private int width;
    private int height;
    public boolean running = true;
    private Hero hero;
    private Screen screen;
    private List<Wall> walls;
    public Arena(int width, int height, Screen screen){
        this.width = width;
        this.height = height;
        this.screen = screen;
        hero = new Hero(10,10);
        this.walls = createWalls();
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        for(Wall wall: walls){
            wall.draw(graphics);
        }
        hero.draw(graphics);

    }
    public void processKey(KeyStroke key) throws IOException{
        switch (key.getKeyType()){
            case ArrowUp:
                moveHero(hero.moveUp());
                break;
            case ArrowDown:
                moveHero(hero.moveDown());
                break;
            case ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case ArrowRight:
                moveHero(hero.moveRight());
                break;
            case EOF:
                running = false;
                break;
            default:
                if(key.getCharacter() == 'q'){
                    screen.close();
                }
        }
    }
    private boolean canHeroMove(Position position){
        int newX = position.getX();
        int newY = position.getY();
        return newX >= 0 && newX < width && newY >= 0 && newY < height;
    }
    public void moveHero(Position position){
        if(canHeroMove(position)){
            hero.setPosition(position);
        }
    }
    private List<Wall> createWalls(){
        List<Wall> walls = new ArrayList<>();
        for(int c = 0; c < width; c++){
            walls.add(new Wall(c,0));
            walls.add(new Wall(c, height -1));
        }
        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }

}
