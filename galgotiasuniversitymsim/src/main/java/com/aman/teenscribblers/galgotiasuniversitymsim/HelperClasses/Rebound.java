package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.view.MotionEvent;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by aman on 13-02-2015.
 */
public class Rebound {

    public static void AddRebound(final View myView) {


// Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

// Add a spring to the system.
        final Spring spring = springSystem.createSpring();
        //spring.setOvershootClampingEnabled(true);
        double TENSION = 50;
        double DAMPER = 3;
        spring.setSpringConfig(new SpringConfig(TENSION, DAMPER));

// Add a listener to observe the motion of the spring.
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                ViewHelper.setScaleX(myView, scale);
                ViewHelper.setScaleY(myView, scale);
            }
        });

        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        spring.setEndValue(1f);
                        break;
                    case MotionEvent.ACTION_UP:
                        spring.setEndValue(0f);
                        break;
                }

                return false;
            }
        });


    }
}
