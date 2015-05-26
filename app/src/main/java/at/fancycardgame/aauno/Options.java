package at.fancycardgame.aauno;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Created by Christian on 26.05.2015.
 */
public class Options extends Activity implements View.OnClickListener  {

   // public ViewGroup userMgmtMenu;
    public ViewGroup createUserMenu;

    private View.OnClickListener mainOnClickListener = this;

    // define font name, can be changed later on here
    private static final String menu_font = "Comic Book Bold.ttf";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_options_page);

        this.createUserMenu = (ViewGroup)getLayoutInflater().inflate(R.layout.menu_createuser_page, null);

        findViewById(R.id.userMgmtMP).setOnClickListener(mainOnClickListener);
        findViewById(R.id.musicOnOffMP).setOnClickListener(mainOnClickListener);
        findViewById(R.id.effectsOnOffMP).setOnClickListener(mainOnClickListener);

        setOptionsMenuTypeface();



    }



    public void onClick(View clickedView) {


        //OnClickListener that determines which TextView has been clicked by ID
        int clickedID = clickedView.getId();

        // create pulse animation when clicking on menue textviews
        Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);

        if (clickedID == R.id.userMgmtMP) {
            a.setAnimationListener(new AbstractAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    // access user mgmt

                    startActivity(new Intent(Options.this,Usermanager.class));


                }
            });
            clickedView.startAnimation(a);

        }


    }
    // method that takes *.ttf file, creates a typeface and applies it to the menu TextViews
        private void setOptionsMenuTypeface() {
        Typeface menu = Typeface.createFromAsset(getAssets(), menu_font);
        setStringTypeface(R.id.userMgmtMP);
        setStringTypeface(R.id.musicOnOffMP);
        setStringTypeface(R.id.effectsOnOffMP);

    }

    private void setStringTypeface(int textview) {
        Typeface options_menu = Typeface.createFromAsset(getAssets(), menu_font);
        ((TextView) findViewById(textview)).setTypeface(options_menu);
    }


}