package at.fancycardgame.aauno;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
    private ImageView cardImg;
    private ImageView backside;
    private int x,y;
    private int width,height;
    private Context context;
    private FrameLayout container;

    public UnoCard(final Context context, FrameLayout container, Point location, Drawable cardImg, String name, String description, String value, String color) {
        this.context = context;
        this.container = container;
        this.name = name;
        this.description = description;
        this.value = value;
        this.color = color;
        this.height = cardImg.getIntrinsicHeight();
        this.width = cardImg.getIntrinsicWidth();

        this.x=location.x;
        this.y=location.y;

        this.cardImg = new ImageView(container.getContext());
        this.cardImg.setImageDrawable(cardImg);

        // TODO
        // init backside of card
        // !!!!

        this.cardImg.setOnTouchListener(new View.OnTouchListener() {
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

        container.addView(this.cardImg);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)this.cardImg.getLayoutParams();
        params.width = MainActivity.scale(this.width)/2;
        params.height = MainActivity.scale(this.height)/2;

        params.leftMargin = this.x;
        params.topMargin = this.y;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        this.cardImg.setLayoutParams(params);
    }

    public String getName(String name) {
        return this.name;
    }

    public String getDescription(String descr) {
        return this.description;
    }

    public String getValue(String value) {
        return this.value;
    }

    public String getColor(String color) {
        return this.color;
    }

    public View getImageView() {
        return (View)this.cardImg;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        this.cardImg.invalidate();
    }

    public void setContainer(FrameLayout deckPos) {
        this.container.removeView(this.cardImg);
        this.container = deckPos;
        this.container.addView(this.cardImg);

    }
}
