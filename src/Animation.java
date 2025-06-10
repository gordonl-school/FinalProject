import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {//implements ActionListener {
    private ArrayList<BufferedImage> frames;
   // private Timer timer;
    private int currentFrame;
 //   private boolean paused;


//    public Animation(ArrayList<BufferedImage> frames, int delay) {
//        this.frames = frames;
//        currentFrame = 0;
//        timer = new Timer(delay, this);
//        timer.start();
//    }

    public Animation(ArrayList<BufferedImage> frames) {
        this.frames = frames;
        currentFrame = 0;
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

//    public void start() {
//        timer.start();
//    }
//    public void stop() {
//        timer.stop();
//    }

    public void advanceFrame() {
        currentFrame = (currentFrame + 1) % frames.size();
    }

//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() instanceof Timer) {
//            //This advances the animation to the next frame
//            //It also uses modulus to reset the frame to the beginning after the last frame
//            //In other words, this allows our animation to loop
//            currentFrame = (currentFrame + 1) % frames.size();
//        }
//    }
}
