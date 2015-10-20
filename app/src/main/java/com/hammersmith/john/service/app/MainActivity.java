package com.hammersmith.john.service.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hammersmith.john.service.R;

public class MainActivity extends AppCompatActivity {
    ViewFlipper viewFlipper;
    GestureDetector mGestureDetector;
    Transition transitionInflater;
    int[] re = {
            R.drawable.selantra_restaurant,
            R.drawable.text_mot,
            R.drawable.logo,
            R.drawable.sb
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        getSupportActionBar().hide();
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        TextView hotel = (TextView) findViewById(R.id.hotel);
        TextView spa = (TextView) findViewById(R.id.spa);
        TextView resta = (TextView) findViewById(R.id.resta);
        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), HotelActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent2);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        spa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });
        resta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), RestaActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent1);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        for (int i = 0 ; i < re.length ; i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(re[i]);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this,customGestureDetector);


    }



    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(getApplicationContext(), R.anim.left_in);
                viewFlipper.setOutAnimation(getApplicationContext(), R.anim.left_out);
                viewFlipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {

                viewFlipper.setInAnimation(getApplicationContext(), R.anim.right_in);
                viewFlipper.setOutAnimation(getApplicationContext(), R.anim.right_out);
                viewFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
