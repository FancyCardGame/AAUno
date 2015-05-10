package at.fancycardgame.aauno;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Thomas on 14.04.2015.
 */
public class UnoCard {

    private String name;
    private String description;
    private String value;
    private String color;
    private ImageView currentSide;
    private Drawable cardFront;
    private int x,y;
    private int width,height;
    private Context context;
    private FrameLayout container;

    public UnoCard(final Context context, FrameLayout container, Point location, Drawable cardFront, Drawable cardBack, String name, String description, String value, String color) {
        this.context = context;
        this.container = container;
        this.name = name;
        this.description = description;
        this.value = value;
        this.color = color;
        this.height = cardBack.getIntrinsicHeight();
        this.width = cardBack.getIntrinsicWidth();

        this.x=location.x;
        this.y=location.y;

        // init card current side
        this.currentSide = new ImageView(container.getContext());
        this.currentSide.setImageDrawable(cardBack);
        //
        this.cardFront = cardFront;

        // init card back
        //this.cardBack = new ImageView(container.getContext());
        //this.cardBack.setImageDrawable(cardBack);


        //init card front touch listener
        /*this.cardFront.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("","");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    return true;
                } else
                    return false;
            }
        });*/

        // init card back touch listener
        this.currentSide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("","");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    return true;
                } else
                    return false;
            }
        });


        container.addView(this.currentSide);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)this.currentSide.getLayoutParams();
        params.width = MainActivity.scale(this.width)/2;
        params.height = MainActivity.scale(this.height)/2;

        params.leftMargin = this.x;
        params.topMargin = this.y;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        this.currentSide.setLayoutParams(params);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription(String descr) {
        return this.description;
    }

    public String getValue() {
        return this.value;
    }

    public String getColor(String color) {
        return this.color;
    }

    public View getImageView() {
        return (View)this.currentSide;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        this.currentSide.invalidate();
    }

    public void setContainer(FrameLayout deckPos) {
        this.container.removeView(this.currentSide);
        this.container = deckPos;
        this.container.addView(this.currentSide);
    }

    public void viewFront() {
        this.container.removeView(this.currentSide);

        // init card current side
        //this.currentSide = new ImageView(container.getContext());
        this.currentSide.setImageDrawable(this.cardFront);

        if(this.currentSide.getLayoutParams()!=null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.currentSide.getLayoutParams();
            params.width = MainActivity.scale(this.width) / 2;
            params.height = MainActivity.scale(this.height) / 2;

            params.leftMargin = this.x;
            params.topMargin = this.y;
            params.gravity = Gravity.LEFT + Gravity.TOP;
            this.currentSide.setLayoutParams(params);

            this.container.addView(this.currentSide);
        }
    }
}
