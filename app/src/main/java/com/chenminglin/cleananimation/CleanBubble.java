package com.chenminglin.cleananimation;

import java.util.UUID;

public class CleanBubble {
    public String id = UUID.randomUUID().toString();
    public float cx;
    public float cy;
    public float radius;
    public float distance;
    //递减幅度
    public int decrement;

    //递减方法
    public void decrease() {

        if (distance > 0) {
            distance = distance - decrement;
        }else{
            return;
        }

        if (Math.abs(cx) > 0) {
            float sin = cx / distance;
            cx = cx - (sin * decrement);
        }

        if (Math.abs(cy) > 0) {
            float cos = cy / distance;
            cy = cy - (cos * decrement);
        }


    }

    @Override
    public String toString() {
        return "CleanBubble{" +
                "id='" + id + '\'' +
                ", cx=" + cx +
                ", cy=" + cy +
                ", radius=" + radius +
                ", distance=" + distance +
                ", decrement=" + decrement +
                '}';
    }
}
