package com.chenminglin.cleananimation;

import java.util.UUID;

/**
 * 用于记录外边小点点
 */
public class CleanBubble {
    public String id = UUID.randomUUID().toString();
    //中心x坐标
    public float cx;
    //中心y坐标
    public float cy;
    //半径
    public float radius;
    //中心距离
    public float distance;
    //保存初始化时的中心距离，用于计算其透明度
    public float initDistance;
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
