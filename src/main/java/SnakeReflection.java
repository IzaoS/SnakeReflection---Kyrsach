/**
 * Класс SnakeReflection реализует игру "Змейка" с отражением от границ экрана.
 * <p>
 * Особенности:
 * <ul>
 *   <li>Случайное начальное положение и направление движения</li>
 *   <li>Отражение от стен по законам оптики (угол падения = угол отражения)</li>
 *   <li>Изменение скорости клавишами '+' и '-'</li>
 *   <li>Сбор еды (красных шариков) для увеличения длины змейки</li>
 *   <li>Отображение текущего счёта</li>
 * </ul>
 * 
 * @author @Izaos
 * @version 1.0
 */
import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SnakeReflection extends JPanel implements ActionListener {
    private static final Logger logger = LogManager.getLogger(SnakeReflection.class);
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int INITIAL_LENGTH = 3;

    // Минимальная и максимальная задержка (в миллисекундах)
    private static final int MIN_DELAY = 50;   // макс. скорость
    private static final int MAX_DELAY = 200;  // мин. скорость
    private static final int DEFAULT_DELAY = 100;

    private final ArrayList<Point> snake;
    private Point food;
    private char direction;
    private boolean running = false;
    private Timer timer;
    private int score = 0;
    private int delay = DEFAULT_DELAY; // текущая задержка = скорость
    private final Random random;

    public SnakeReflection() {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        random = new Random();
        snake = new ArrayList<>();
        startGame();
    }
/**
* Запускает новую игру: инициализирует змейку, генерирует еду и запускает таймер.
* Сбрасывает счёт и скорость к начальным значениям.
*/
    public void startGame() {
        snake.clear();

        // Случайное направление
        char[] dirs = {'U', 'D', 'L', 'R'};
        direction = dirs[random.nextInt(dirs.length)];

        // Случайная позиция головы
        int startX, startY;
        do {
            startX = random.nextInt(PANEL_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            startY = random.nextInt(PANEL_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        } while (!isSafeStart(startX, startY, direction));

        // Голова
        snake.add(new Point(startX, startY));

        // Тело 
        for (int i = 1; i < INITIAL_LENGTH; i++) {
            Point prev = snake.get(i - 1);
            Point next = new Point(prev);
            switch (direction) {
                case 'U' -> next.y += UNIT_SIZE;
                case 'D' -> next.y -= UNIT_SIZE;
                case 'L' -> next.x += UNIT_SIZE;
                case 'R' -> next.x -= UNIT_SIZE;
            }

            if (next.x < 0 || next.x >= PANEL_WIDTH || next.y < 0 || next.y >= PANEL_HEIGHT) {
                break;
            }
            snake.add(next);
        }

        newFood();
        score = 0;
        delay = DEFAULT_DELAY;
        running = true;
        if (timer != null) timer.stop();
        timer = new Timer(delay, this);
        timer.start();
    }

 /**
 * Проверяет, что начальная позиция змейки безопасна
 * и всё тело помещается внутри игрового поля.
 *
 * @param x начальная координата X головы
 * @param y начальная координата Y головы
 * @param dir направление движения
 * @return true, если стартовая позиция допустима
 */
    private boolean isSafeStart(int x, int y, char dir) {
        int tailX = x, tailY = y;
        switch (dir) {
            case 'U' -> tailY += (INITIAL_LENGTH - 1) * UNIT_SIZE;
            case 'D' -> tailY -= (INITIAL_LENGTH - 1) * UNIT_SIZE;
            case 'L' -> tailX += (INITIAL_LENGTH - 1) * UNIT_SIZE;
            case 'R' -> tailX -= (INITIAL_LENGTH - 1) * UNIT_SIZE;
        }
        return tailX >= 0 && tailX < PANEL_WIDTH && tailY >= 0 && tailY < PANEL_HEIGHT;
    }
	
/**
 * Генерирует новую еду (красный шарик) в случайной позиции,
 * не пересекающейся с телом змейки.
 */
    public void newFood() {
        int x, y;
        Point foodPoint;
        do {
            x = random.nextInt(PANEL_WIDTH / UNIT_SIZE) * UNIT_SIZE;
            y = random.nextInt(PANEL_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
            foodPoint = new Point(x, y);
        } while (snake.stream().filter(p -> p.equals(foodPoint)).findAny().isPresent()); // еда не на змейке
        food = foodPoint;
    }
	
/**
 * Переопределённый метод отрисовки компонента.
 * Вызывает метод {@link #draw(Graphics)} для отрисовки игры.
 *
 * @param g графический контекст
 */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
	
/**
 * Отрисовывает все элементы игры: змейку, еду, счёт и скорость.
 *
 * @param g графический контекст
 */
    public void draw(Graphics g) {
        if (running) {
            // Еда
			if (food != null) {
				g.setColor(Color.RED);
				g.fillOval(food.x, food.y, UNIT_SIZE, UNIT_SIZE);
            }
            // Змейка
            for (int i = 0; i < snake.size(); i++) {
                float t = (snake.size() > 1) ? (float) i / (snake.size() - 1) : 0.0f; 
				
                int r = (int) (255 + t * (0 - 255));
                int gC = (int) (255 + t * (128 - 255));  
                int b = 0;    
				
				g.setColor(new Color(r, gC, b));
				
                Point p = snake.get(i);
                g.fillOval(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Счёт и скорость
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 10, 20);
            g.drawString("Speed: " + (MAX_DELAY - delay + MIN_DELAY) / 10, 10, 40); // условная шкала
        }
    }
	
/**
 * Выполняет одно движение змейки:
 * <ul>
 *   <li>Сдвигает все сегменты тела</li>
 *   <li>Перемещает голову в текущем направлении</li>
 *   <li>Обрабатывает отражение от границ экрана</li>
 *   <li>Проверяет поедание еды и увеличивает счёт при необходимости</li>
 * </ul>
 */
public void move() {
    if (snake.isEmpty()) {
        logger.warn("Attempt to move the empty snake.");
        return;
    }
    logger.debug("Snake is mooving. Direction: {}, Score: {}", direction, score);

    moveBody();
    Point newHead = calculateNewHead();
    handleWallReflection(newHead);
    snake.set(0, newHead);
    handleFoodCollision(newHead);
}

/**
 * Сдвигает сегменты тела змейки,
 * перемещая каждый элемент на позицию предыдущего.
 */
private void moveBody() {
    for (int i = snake.size() - 1; i > 0; i--) {
        snake.set(i, new Point(snake.get(i - 1)));
    }
}

/**
 * Вычисляет новую позицию головы змейки
 * в зависимости от текущего направления движения.
 *
 * @return новая позиция головы
 */
private Point calculateNewHead() {
    Point head = snake.get(0);
    Point newHead = new Point(head);
    switch (direction) {
        case 'U' -> newHead.y -= UNIT_SIZE;
        case 'D' -> newHead.y += UNIT_SIZE;
        case 'L' -> newHead.x -= UNIT_SIZE;
        case 'R' -> newHead.x += UNIT_SIZE;
    }
    return newHead;
}

/**
 * Обрабатывает отражение змейки от границ игрового поля
 * и корректирует направление движения.
 *
 * @param newHead новая позиция головы
 */
private void handleWallReflection(Point newHead) {
    if (newHead.x < 0) {
        newHead.x = 0;
        if (direction == 'L') direction = 'R';
    } else if (newHead.x >= PANEL_WIDTH) {
        newHead.x = PANEL_WIDTH - UNIT_SIZE;
        if (direction == 'R') direction = 'L';
    }

    if (newHead.y < 0) {
        newHead.y = 0;
        if (direction == 'U') direction = 'D';
    } else if (newHead.y >= PANEL_HEIGHT) {
        newHead.y = PANEL_HEIGHT - UNIT_SIZE;
        if (direction == 'D') direction = 'U';
    }
}

/**
 * Проверяет столкновение головы змейки с едой.
 * Увеличивает счёт и длину змейки при поедании.
 *
 * @param newHead новая позиция головы
 */
private void handleFoodCollision(Point newHead) {
    if (newHead.equals(food)) {
        score++;
        snake.add(new Point(snake.get(snake.size() - 1)));
        newFood();
    }
}


/**
 * Обрабатывает событие таймера: вызывает {@link #move()} и обновляет отображение.
 *
 * @param e событие действия
 */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
/**
 * Обрабатывает нажатия клавиш управления змейкой
 * и изменения скорости игры.
 *
 * @param e событие нажатия клавиши
 */
        @Override
        public void keyPressed(KeyEvent e) {
            // Управление направлением
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }

            // Управление скоростью: '+' и '-'
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_EQUALS || keyCode == KeyEvent.VK_ADD) {
                delay = Math.max(MIN_DELAY, delay - 10);
                restartTimer();
            } else if (keyCode == KeyEvent.VK_MINUS || keyCode == KeyEvent.VK_SUBTRACT) {
                delay = Math.min(MAX_DELAY, delay + 10);
                restartTimer();
            }
        }

/**
 * Перезапускает таймер с текущей задержкой.
 * Используется при изменении скорости игры.
 */
        private void restartTimer() {
            if (timer != null) {
				timer.stop();
			}
            timer = new Timer(delay, SnakeReflection.this);
            timer.start();
        }
    }

	
/**
 * Возвращает список сегментов змейки.
 * 
 * @return список точек, представляющих тело змейки
 */
    public ArrayList<Point> getSnake() {
        return snake;
    }

    public Point getFood() {
        return food;
    }

    public boolean isRunning() {
        return running;
    }
	
/**
 * Устанавливает направление движения змейки.
 * 
 * @param direction новое направление ('U', 'D', 'L', 'R')
 */
    public void setDirection(char direction) {
        this.direction = direction;
    }

/**
 * Точка входа в приложение.
 * Инициализирует и отображает главное окно игры.
 *
 * @param args аргументы командной строки (не используются)
 */
        public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> {
				try{
					JFrame frame = new JFrame("Змейка-курсач");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setResizable(false);
					frame.add(new SnakeReflection());
					 frame.pack();
					 frame.setLocationRelativeTo(null);
					 frame.setVisible(true);
				} catch (Exception ex) {
					logger.error("Error creating windowed game", ex);
					JOptionPane.showMessageDialog(null,
						"A critical error has occurred: " + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
            });
        } catch (Exception e) {
            System.err.println("Critical error while starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}