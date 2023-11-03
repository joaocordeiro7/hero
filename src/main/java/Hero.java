import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class Hero extends Element{
    private Position position;
    public Hero(int initialX, int initialY){
        super(initialX, initialY);
    }
    @Override
    public void draw(TextGraphics graphics){
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFF33F"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(getPosition().getX(), getPosition().getY()), "X");
    }

    public Position moveUp() {
        Position currentPosition = getPosition();
        return new Position(currentPosition.getX(), currentPosition.getY() - 1);
    }

    public Position moveDown() {
        Position currentPosition = getPosition();
        return new Position(currentPosition.getX(), currentPosition.getY() + 1);
    }

    public Position moveLeft() {
        Position currentPosition = getPosition();
        return new Position(currentPosition.getX() - 1, currentPosition.getY());
    }

    public Position moveRight() {
        Position currentPosition = getPosition();
        return new Position(currentPosition.getX() + 1, currentPosition.getY());
    }
}
