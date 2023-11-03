import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TerminalSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Arena {
    private int width;
    private int height;
    public boolean running = true;
    private Hero hero;
    private Screen screen;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;

    public Arena(int width, int height, Screen screen) {
        this.width = width;
        this.height = height;
        this.screen = screen;
        hero = new Hero(10, 5);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        for (Wall wall : walls) {
            wall.draw(graphics);
        }
        for (Coin coin : coins) {
            coin.draw(graphics);
        }
        for (Monster monster : monsters) {
            monster.draw(graphics);
        }
        hero.draw(graphics);
    }

    public void processKey(KeyStroke key) throws IOException {
        moveMonsters();
        int remainingCoins = retrieveCoins();

        // Check for monster collisions
        if (verifyMonsterCollisions()) {
            running = false;
            System.out.println("You were caught by a monster!");
        }

        // Check if all coins are collected
        if (remainingCoins == 0) {
            running = false;
            System.out.println("Congratulations! You collected all the coins!");
        }
        switch (key.getKeyType()) {
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
                if (key.getCharacter() == 'q') {
                    screen.close();
                }
        }
    }

    private boolean canHeroMove(Position position) {
        for (Wall wall : walls) {
            if (wall.getPosition().equals(position)) {
                return false;
            }
        }
        return position.getX() >= 0 && position.getX() < width && position.getY() >= 0 && position.getY() < height;
    }

    public void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
        }
    }

    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }
        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }

    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int x, y;
            do {
                x = random.nextInt(width - 2) + 1;
                y = random.nextInt(height - 2) + 1;
            } while (x == hero.getPosition().getX() && y == hero.getPosition().getY() || coinExistsAt(x, y, coins));
            coins.add(new Coin(x, y));
        }
        return coins;
    }

    private boolean coinExistsAt(int x, int y, List<Coin> coins) {
        for (Coin coin : coins) {
            if (coin.getPosition().getX() == x && coin.getPosition().getY() == y) {
                return true;
            }
        }
        return false;
    }

    private List<Monster> createMonsters() {
        Random random = new Random();
        ArrayList<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int x, y;
            do {
                x = random.nextInt(width - 2) + 1;
                y = random.nextInt(height - 2) + 1;
            } while (x == hero.getPosition().getX() && y == hero.getPosition().getY() || coinExistsAt(x, y, coins) || monsterExistsAt(x, y, monsters));
            monsters.add(new Monster(x, y));
        }
        return monsters;
    }

    private boolean monsterExistsAt(int x, int y, List<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.getPosition().getX() == x && monster.getPosition().getY() == y) {
                return true;
            }
        }
        return false;
    }

    private int retrieveCoins() {
        int remainingCoins = coins.size();
        for (Iterator<Coin> iterator = coins.iterator(); iterator.hasNext();) {
            Coin coin = iterator.next();
            if (hero.getPosition().equals(coin.getPosition())) {
                iterator.remove();
                remainingCoins--;
            }
        }
        return remainingCoins;
    }


    private void moveMonsters() {
        for (Monster monster : monsters) {
            Position newPos = monster.move();
            if (canHeroMove(newPos) && !coinExistsAt(newPos.getX(), newPos.getY(), coins) && !monsterExistsAt(newPos.getX(), newPos.getY(), monsters)) {
                monster.setPosition(newPos);
            }
        }
    }

    private boolean verifyMonsterCollisions() {
        for (Monster monster : monsters) {
            if (hero.getPosition().equals(monster.getPosition())) {
                return true;
            }
        }
        return false;
    }
}
