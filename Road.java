package car_games;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;


public class Road extends JComponent {
    
    private Timer treeTimer;
    private final ArrayList<RoadStripe> stripesList = new ArrayList<>();
    private final ArrayList<Tree> treesList = new ArrayList<>();
    private Timer stripesTimer;
    private boolean gameStarted = false;
    private BufferedImage explosionImage;
    private BufferedImage explosionImagePart;
    private int explosionImageCounter = 0;
    public Point explosionPosition;
    private BufferedImage treeImage;
    private int stripeSpeed = 100;
    private int treeSpeed = 100;

    
    public Road() {
        setSize(400, 500);
        try {
            explosionImage = ImageIO.read(getClass().getResource("/resources/explo.png"));
            treeImage = ImageIO.read(getClass().getResource("/resources/tree.png"));
        }
        catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        prepareStripesTimer();
        prepareTreeTimer();
    }
    
    private void prepareStripesTimer() {
        stripesTimer = new Timer(2500, (e) -> {
            addNewStripes(stripeSpeed);
            removeRedundantStripes();
            repaint();
        });
    }
    
    private void prepareTreeTimer() {
        treeTimer = new Timer(1000, (e) -> {
            addNewTrees(treeSpeed);
            removeRedundantTrees();
            repaint();
        });
    }
    
    public void startTimers() {
        gameStarted = true;
        treeTimer.start();
        stripesTimer.start();
        startTimersInStripesList();
        startTimersInTreesList();
        addFirstTrees();
    }
    
    private void addFirstTrees() {
        Tree tree1 = new Tree(10, -100, 100, treeImage);
        add(tree1);
        treesList.add(tree1);
        tree1.start();
        Tree tree2 = new Tree(360, -100, 100, treeImage);
        add(tree2);
        treesList.add(tree2);
        tree2.start();
    }
    
    private void startTimersInStripesList() {
        stripesList.forEach((stripe) -> {
            stripe.startTimer();
        });
    }
    
    private void startTimersInTreesList() {
        treesList.forEach((tree) -> {
            tree.start();
        });
    }
    
    public void setTimersSpeed(int speed) {
        int spawnSpeed = prepareSpawnSpeed(speed);
        stripeSpeed = speed;
        treeSpeed = speed;
        stripesTimer.setDelay(spawnSpeed);
        treeTimer.setDelay(spawnSpeed / 2);
        stripesList.forEach((stripe) -> {
            stripe.setSpeed(speed);
        });
        treesList.forEach((tree) -> {
            tree.setSpeed(speed);
        });
    }
    
    private int prepareSpawnSpeed(int speed) {
        if (speed >= 70) {
            return 2000;
        } else if (speed >= 30) {
            return 1000;
        } else {
            return 500;
        }
    }
    
    public void prepareStripes() {
        RoadStripe stripe = new RoadStripe(147, 10, 100);
        stripesList.add(stripe);
        add(stripe);
        RoadStripe stripe2 = new RoadStripe(247, 10, 100);
        stripesList.add(stripe2);
        add(stripe2);
        RoadStripe stripe3 = new RoadStripe(147, 340, 100);
        stripesList.add(stripe3);
        add(stripe3);
        RoadStripe stripe4 = new RoadStripe(247, 340, 100);
        stripesList.add(stripe4);
        add(stripe4);
    }
    
    public void prepareTrees() {
        Tree tree = new Tree(10, 30, 100, treeImage);
        treesList.add(tree);
        add(tree);
        Tree tree1 = new Tree(360, 30, 100, treeImage);
        treesList.add(tree1);
        add(tree1);
        Tree tree2 = new Tree(10, 130, 100, treeImage);
        treesList.add(tree2);
        add(tree2);
        Tree tree3 = new Tree(360, 130, 100, treeImage);
        treesList.add(tree3);
        add(tree3);
        Tree tree4 = new Tree(10, 230, 100, treeImage);
        treesList.add(tree4);
        add(tree4);
        Tree tree5 = new Tree(360, 230, 100, treeImage);
        treesList.add(tree5);
        add(tree5);
        Tree tree6 = new Tree(10, 330, 100, treeImage);
        treesList.add(tree6);
        add(tree6);
        Tree tree7 = new Tree(360, 330, 100, treeImage);
        treesList.add(tree7);
        add(tree7);
        Tree tree8 = new Tree(10, 430, 100, treeImage);
        treesList.add(tree8);
        add(tree8);
        Tree tree9 = new Tree(360, 430, 100, treeImage);
        treesList.add(tree9);
        add(tree9);
    }
    
    private void addNewStripes(int speed) {
        RoadStripe stripe = new RoadStripe(147, -100, speed);
        add(stripe);
        stripesList.add(stripe);
        stripe.startTimer();
        RoadStripe stripe2 = new RoadStripe(247, -100, speed);
        add(stripe2);
        stripesList.add(stripe2);
        stripe2.startTimer();
    }
    
    private void addNewTrees(int speed) {
        Tree tree = new Tree(10, -130, speed, treeImage);
        add(tree);
        treesList.add(tree);
        tree.start();
        Tree tree1 = new Tree(360, -130, speed, treeImage);
        add(tree1);
        treesList.add(tree1);
        tree1.start();
    }
    
    private void removeRedundantStripes() {
        if(stripesList.get(0).getY() >= 500) {
            remove(stripesList.get(0));
            remove(stripesList.get(1));
            stripesList.remove(0);
            stripesList.remove(1);
        }
    }
    
    private void removeRedundantTrees() {
        if(treesList.get(0).getY() >= 500) {
            remove(treesList.get(0));
            remove(treesList.get(1));
            treesList.remove(0);
            treesList.remove(1);
        }
    }
    
    public void nextExplosion() {
        try {
            explosionImagePart = explosionImage.getSubimage(256 * explosionImageCounter, 0, 256, 256);
            explosionImageCounter++;
            repaint();
        } catch (Exception ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }
    
    public void stopAllTimers() {
        treeTimer.stop();
        stripesTimer.stop();
        stripesList.forEach((stripe) -> {
            stripe.stop();
        });
        treesList.forEach((tree) -> {
            tree.stop();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(48, 45, 45));
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setColor(new Color(48, 218, 54));
        g.fillRect(0, 0, 50, getSize().height);
        g.fillRect(350, 0, 50, getSize().height);
        if (!gameStarted) {
            g.setColor(Color.white);
            g.setFont(new Font("Calibri", Font.BOLD, 23));
            g.drawString("PRESS SPACE TO START", 90, 200);
        }
        if (explosionImagePart != null) {
            g.drawImage(explosionImagePart, explosionPosition.x - 100, explosionPosition.y - 100, this);
        }
    }
                
}
