package com.wlj.snowviewtest.snow;

import android.graphics.Point;

public class BezierUtil {

    public static Point calculatePoint(float t, Point p0, Point p1, Point p2) {
        Point point = new Point();
        float temp = 1 - t;


        /**
         * 二阶贝塞尔曲线的公式  算出曲线上 点坐标的位置
         */
        point.x= (int) (temp*temp*p0.x+2*t*temp*p1.x+t*t*p2.x);
        point.y= (int) (temp*temp*p0.y+2*t*temp*p1.y+t*t*p2.y);

        return point;
    }

}
