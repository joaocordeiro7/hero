import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

import static com.googlecode.lanterna.input.KeyType.*;

public class Game {
    public Screen screen;
    private Arena arena;
    public boolean running = true;
    public Game() {
        try {
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);
            screen.startScreen();
            screen.doResizeIfNecessary();
            arena = new Arena(40,20, screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void draw() throws IOException{
        TextGraphics textGraphics = screen.newTextGraphics();
        screen.clear();
        arena.draw(textGraphics);
        screen.refresh();
    }

    public void run() throws IOException {
        while(running) {
            draw();
            KeyStroke key = screen.readInput();
            processKey(key);
            if(!running){
                screen.close();
            }
        }
    }

    private void processKey(KeyStroke key) throws IOException{
        arena.processKey(key);
        if(key.getCharacter() != null && key.getCharacter() == 'q'){
            running = false;
        }
    }
}