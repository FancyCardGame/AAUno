package at.fancycardgame.aauno;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

    // define font name, can be changed later on here
    private static final String menu_font = "Comic Book Bold.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set menu typeface
        setMenuTypeface();


        // prepare game stuff here


        findViewById(R.id.startGameMP).setOnClickListener(this);
        findViewById(R.id.optionsMP).setOnClickListener(this);
        findViewById(R.id.helpMP).setOnClickListener(this);
        findViewById(R.id.quitMP).setOnClickListener(this);
    }

    // method that takes *.ttf file, creates a typeface and applies it to the menu TextViews
    private void setMenuTypeface() {
        Typeface menu = Typeface.createFromAsset(getAssets(), menu_font);
        ((TextView)findViewById(R.id.startGameMP)).setTypeface(menu);
        ((TextView)findViewById(R.id.optionsMP)).setTypeface(menu);
        ((TextView)findViewById(R.id.helpMP)).setTypeface(menu);
        ((TextView)findViewById(R.id.quitMP)).setTypeface(menu);
    }

    @Override
    public void onClick(View clickedView) {
        //OnClickListener that determines which TextView has been clicked by ID
        int clickedViewID = clickedView.getId();

        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);


        if(clickedViewID==R.id.startGameMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    //
                    // START GAME HERE
                    //
                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedViewID==R.id.optionsMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    //
                    // VIEW OPTIONS MENU
                    //
                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedViewID==R.id.helpMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    //
                    // VIEW HELP MENU?/DIALOG?/TUTORIAL?
                    //
                }
            } );
            clickedView.startAnimation(a);

        } else if(clickedViewID==R.id.quitMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // hide menu
                    hideView(R.id.menu);

                    finish();
                    System.exit(0);
                }
            } );
            clickedView.startAnimation(a);
        }
        // if the same OnClickListener is used continue here with else if(...)
    }


    // method that sets a view invisible which is specified with a parameter
    public void hideView(int view) {
        findViewById(view).setVisibility(View.INVISIBLE);
    }

    // method that sets a view visible which is specified with a parameter
    public void showView(int view) {
        findViewById(view).setVisibility(View.VISIBLE);
    }
}
