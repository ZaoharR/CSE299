package car_games;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.Timer;

public class Tree extends JComponent {
    
    private int yPosition;
    private final Timer timer;
    private final BufferedImage treeImage;
    
    public Tree(int x, int y, int speed, BufferedImage image) {
        setSize(26, 30);
        yPosition = y;
        setLocation(x, y);
        timer = new Timer(speed, (e) -> {
            yPosition += 10;
            setLocation(getLocation().x, yPosition);
            repaint();
        });
        treeImage = image;
    }

    public void start() {
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    public void setSpeed(int speed) {
        timer.setDelay(speed);
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.drawImage(treeImage, 0, 0, this);
    }
    
}
