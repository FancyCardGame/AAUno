package at.fancycardgame.aauno;

import android.view.animation.Animation;

/**
 * Created by Thomas on 01.04.2015.
 */
// just an abstract class to overwrite only one method in OnClickListener in MainActivity
public abstract class AbstractAnimationListener implements Animation.AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
