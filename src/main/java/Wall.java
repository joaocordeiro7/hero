import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor.RGB;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;

import java.io.IOException;

public class Wall extends Element {
    private Position position;
    private TextCharacter wallCharacter;

    public Wall(int x, int y)  {
        super(x,y);
        wallCharacter = new TextCharacter('#', TextColor.ANSI.GREEN, TextColor.ANSI.BLACK, SGR.BOLD);
        setPosition(new Position(x, y));
    }
    @Override
    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(wallCharacter.getForegroundColor());
        graphics.setBackgroundColor(wallCharacter.getBackgroundColor());
        SGR[] modifiersArray = wallCharacter.getModifiers().toArray(new SGR[0]);
        graphics.enableModifiers(modifiersArray);
        graphics.setCharacter(new TerminalPosition(getPosition().getX(), getPosition().getY()), wallCharacter.getCharacter());
    }
}


