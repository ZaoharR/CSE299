package car_games;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


public class Car extends JComponent 
{

    private final BufferedImage carImage;
    private int carXPosition = 175;
    
    public Car(BufferedImage image) 
    {
        setSize(100, 100);
        setLocation(carXPosition, 360);
        carImage = image;
    }
    
    public void moveLeft() 
    {
        if (carXPosition != 75) 
        {
            carXPosition -= 100;
            setLocation(carXPosition, 360);
            repaint();
        }
    }
    
    public void moveRight() {
        if (carXPosition != 275) {
            carXPosition += 100;
            setLocation(carXPosition, 360);
            repaint();
        }
    }
      public void moveCenter() 
      {
        if (carXPosition != 180) 
        {
            carXPosition -= 175;
            setLocation(carXPosition, 360);
            repaint();
        }
    }
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        g.drawImage(carImage, 0, 0, this);
    }
            
}
