package car_games;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.Timer;


public class PoliceCar extends JComponent 
{
    
    private final BufferedImage policeCarImage;
    private int yPosition;
    private final Timer timer;
    
    public PoliceCar(int roadNumber, int velocityInMs, BufferedImage image) 
    {
        setSize(100, 100);
        setStartingPositionFromRoadNumber(roadNumber);
        policeCarImage = image;
        timer = new Timer(velocityInMs, (e) -> 
        {
            yPosition += 10;
            setLocation(getLocation().x, yPosition);
            repaint();
        });
    }
    
    private void setStartingPositionFromRoadNumber(int roadNumber) 
    {
        switch (roadNumber) {
            case 1:
                roadNumber = 75;
                break;
            case 2:
                roadNumber = 175;
                break;
            case 3:
                roadNumber = 275;
                break;
        }
        yPosition = -100;
        setLocation(roadNumber, yPosition);
    }
    
    public void start() 
    {
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(policeCarImage, 0, 0, this);
    }
    
}
