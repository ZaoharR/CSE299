package car_games;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;



public class GamePanel extends javax.swing.JPanel {

    private Car car;
    private Road road;
    private Timer policeCarsSpawnTimer;
    private Timer policeCarsSpeedIncreaseTimer;
    private ArrayList<PoliceCar> policeCarsList = new ArrayList<>();
    private int policeCarSpawnDelay = 3000;
    private int policeCarSpeed = 100;
    private final Random randomizer = new Random();
    private MediaPlayer player;
    private AudioClip soundPlayer;
    private Timer explosionTimer;
    private int explostionTimerCounter = 0;
    private Timer collisionCheckTimer;
    private int score = 0;
    private boolean intelligentPoliceCars = false;
    private BufferedImage policeCarImage;
    private BufferedImage carImage;
    private BufferedImage treeImage;
    
    public GamePanel() {
        initComponents();
        preparePanel();
        loadGame();
        prepareTimers();
        preparePlayers();
    }
    
    private void preparePanel() {
        try {
            policeCarImage = ImageIO.read(getClass().getResource("/resources/policeCar.png"));
            carImage = ImageIO.read(getClass().getResource("/resources/car.png"));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
        setFocusable(true);
        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel);
        
        policeCarsSpawnTimer = null;
        policeCarsSpeedIncreaseTimer = null;
        policeCarsList = new ArrayList<>();
        policeCarSpawnDelay = 3000;
        policeCarSpeed = 100;
        explosionTimer = null;
        explostionTimerCounter = 0;
        collisionCheckTimer = null;
        score = 0;
        intelligentPoliceCars = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                startGame();
                break;
            case KeyEvent.VK_LEFT:
                car.moveLeft();
                break;
           case KeyEvent.VK_0:
                car.moveCenter();
                break;
            case KeyEvent.VK_RIGHT:
                car.moveRight();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_formKeyPressed

    
    private void loadGame() {
        car = new Car(carImage);
        road = new Road();
        add(car);
        add(road);
        road.prepareStripes();
        road.prepareTrees();
        revalidate();
        repaint();
    }
    
    private void restartGame() {
        removeAll();
        initComponents();
        preparePanel();
        loadGame();
        prepareTimers();
        prepareTimers();
    }
    
    private void prepareTimers() {
        policeCarsSpawnTimer = new Timer(policeCarSpawnDelay, (e) -> {
            addNewPoliceCar();
            removeRedundantPoliceCars();
            revalidate();
            repaint();
        });
        policeCarsSpawnTimer.setInitialDelay(3000);
        
        policeCarsSpeedIncreaseTimer = new Timer(10000, (e) -> {
            if (policeCarSpeed > 26) {
                policeCarSpeed -= 10;
            } else {
                policeCarSpeed = 16;
                intelligentPoliceCars = true;
            }
            road.setTimersSpeed(policeCarSpeed);
        });
        
        explosionTimer = new Timer(50, (e) -> {
                road.nextExplosion();
                explostionTimerCounter++;
                if (explostionTimerCounter > 12) {
                    explosionTimer.stop();
                }
        });
        
        collisionCheckTimer = new Timer(policeCarSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (collisionOccured()) {
                    road.explosionPosition = new Point(car.getX(), car.getY());
                    explosionTimer.start();
                    player.stop();
                    soundPlayer.play();
                    stopAllTimers();
                    showAlert();
                    restartGame();
                }
            }
        });
    }
    
    private void preparePlayers() {
        URL backGr = getClass().getResource("/resources/bmw_sound.wav");
        URL expSound = getClass().getResource("/resources/explosion.wav");
        player = new MediaPlayer(new Media(backGr.toString()));
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                player.seek(Duration.ZERO);
            }
        });
        
        soundPlayer = new AudioClip(expSound.toString());
    }
    
    private void startGame() {
        road.startTimers();
        policeCarsSpawnTimer.start();
        policeCarsSpeedIncreaseTimer.start();      
        player.play();
        collisionCheckTimer.start();
    }
    
    private void addNewPoliceCar() {
        PoliceCar policeCar;
        if (intelligentPoliceCars) {
            policeCar = new PoliceCar(getCarCurrentRoadNumber(), policeCarSpeed, policeCarImage);
        } else {
            int roadNumber = randomizer.nextInt(3) + 1;
            policeCar = new PoliceCar(roadNumber, policeCarSpeed, policeCarImage);
        }
        policeCarsList.add(policeCar);
       
        remove(road);
        add(policeCar);
        add(road);
        policeCar.start();
    }
    
    private int getCarCurrentRoadNumber() {
        switch (car.getX()) {
            case 75:
                return 1;
            case 175:
                return 2;
            default:
                return 3;
        }
    }
    
    private void removeRedundantPoliceCars() {
        if (policeCarsList.get(0).getY() >= 500) {
            remove(policeCarsList.get(0));
            policeCarsList.remove(0);
            score=score+1;
            System.out.println("Pcar List : "+policeCarsList.size());
              System.out.println("Score:  "+score);
        }
    }
    
    private boolean collisionOccured() {
        if (policeCarsList.stream().anyMatch((policeCarr) -> (Math.abs(policeCarr.getX() - car.getX()) < 50 && Math.abs(policeCarr.getY() - car.getY()) < 100))) {
            return true;
        }
        return false;
    }
    
    private void stopAllTimers() {
        policeCarsSpawnTimer.stop();
        policeCarsSpeedIncreaseTimer.stop();
        collisionCheckTimer.stop();
        road.stopAllTimers();
        policeCarsList.forEach((policeCar) -> {
            policeCar.stop();
        });
    }
    
    private void showAlert() {
        if(score!=0){
              JOptionPane.showMessageDialog(null, "You lost! Your score was: " + (score+1));
        }else{
             JOptionPane.showMessageDialog(null, "You lost! Your score was: " + score);

        }
              
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
