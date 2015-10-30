//package com.aman.teenscribblers.galgotiasuniversitymsim.view;
//
//import android.os.Bundle;
//import android.view.MotionEvent;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.facebook.rebound.SimpleSpringListener;
//import com.facebook.rebound.Spring;
//import com.facebook.rebound.SpringConfig;
//import com.facebook.rebound.SpringSystem;
//import com.nineoldandroids.view.ViewHelper;
//
///**
// * Created by aman on 18-02-2015.
// */
//
//public class MaterialReboundAlert extends MaterialDialog {
//
//    private final static double TENSION = 50;
//    private final static double DAMPER = 3; //friction
//    SpringSystem springSystem;
//    Spring spring;
//
//    protected MaterialReboundAlert(Builder builder) {
//        super(builder);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // Create a system to run the physics loop for a set of springs.
//        springSystem = SpringSystem.create();
//
//        // Add a spring to the system.
//        spring = springSystem.createSpring();
//        //spring.setOvershootClampingEnabled(true);
//        spring.setSpringConfig(new SpringConfig(TENSION, DAMPER));
//
//        // Add a listener to observe the motion of the spring.
//        spring.addListener(new SimpleSpringListener() {
//
//            @Override
//            public void onSpringUpdate(Spring spring) {
//                // You can observe the updates in the spring
//                // state by asking its current value in onSpringUpdate.
//                float value = (float) spring.getCurrentValue();
//                float scale = 1f - (value * 0.5f);
//                ViewHelper.setScaleX(getCustomView(), scale);
//                ViewHelper.setScaleY(getCustomView(), scale);
//            }
//        });
//    }
//
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                spring.setEndValue(1f);
//                break;
//            case MotionEvent.ACTION_UP:
//                spring.setEndValue(0f);
//                break;
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    public static MaterialReboundAlert build(Builder b) {
//        return new MaterialReboundAlert(b);
//    }
//}
