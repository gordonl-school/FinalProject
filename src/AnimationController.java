import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AnimationController implements ActionListener {
    private ArrayList<Animation> animations;
    private Timer timer;

    public AnimationController(int delay) {
        animations = new ArrayList<>();
        timer = new Timer(delay, this);
        timer.start();
    }

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Animation animation : animations) {
            animation.advanceFrame();
        }
    }
}
