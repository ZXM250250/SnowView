package com.wlj.snowviewtest.snow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

/**
 * 雪花的实体类 与自主绘制
 */
public class Snow {
    private int x;
    private int y;
    private int color;
    private int radius;
    private int rotation;
    private int speed;

    /**
     * 父布局的宽度
     */
    private int parentWidth;
    private int parentHeight;
    /**
     * 给雪花飘落找到三个 贝塞尔的控制点   为它绘制曲线下落
     */
    private Point startPoint;
    private Point middlePoint;
    private Point endPoint;

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    private int strokeWidth;

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(Point middlePoint) {
        this.middlePoint = middlePoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public int getParentWidth() {
        return parentWidth;
    }

    public void setParentWidth(int parentWidth) {
        this.parentWidth = parentWidth;
    }

    public int getParentHeight() {
        return parentHeight;
    }

    public void setParentHeight(int parentHeight) {
        this.parentHeight = parentHeight;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public static Snow generateSnow(int width, int height) {
        Snow snow = new Snow();
        snow.setParentWidth(width);
        snow.setParentHeight(height);
        Random random = new Random();
        /**
         * 雪花最起始被绘制的坐标
         */
        int x = random.nextInt(width);
        int y = -random.nextInt(height);

        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        int color = Color.argb(255,r,g,b);
        /**
         * 雪花的大小
         */
        int radius = 10 + random.nextInt(10);
        /**
         * 雪花的旋转的角度
         */
        int rotation = random.nextInt(60);
        /**
         * 雪花的飘动速度
         */
        int speed = (int) ((2 + Math.abs(radius - 20)) * 0.5);
        /**
         * 绘制雪花的时候的   线条的粗细
         */
        int strokeWidth = (int) (radius * 0.2);

        /**
         * 雪花运动过程中的三个点    三个点的位置各不相同   也就是为了给贝塞尔曲线的绘制  提供 起始点  控制点  终止点
         */
        Point start = new Point(random.nextInt(width), -random.nextInt(height));
        Point middle = new Point(random.nextInt(width), random.nextInt(height));
        Point end = new Point(random.nextInt(width), height + random.nextInt(height));

        snow.setX(x);
        snow.setY(y);
        snow.setColor(color);
        snow.setRadius(radius);
        snow.setRotation(rotation);
        snow.setSpeed(speed);
        snow.setStrokeWidth(strokeWidth);

        snow.setStartPoint(start);
        snow.setMiddlePoint(middle);
        snow.setEndPoint(end);


        return snow;
    }


    /**
     * 逻辑函数
     */
    public void step() {
        y += speed;
        /**
         * 如果雪花划出屏幕外边  那就回到上边重新下降
         */
        if (y > parentHeight) {
            y = -50;
        }
        /**
         * 每次绘制  移动一个角度  结果是  雪花的旋转特效
         *
         */
        setRotation(getRotation() + 1);
    }

    public void onDraw(Canvas canvas, Paint paint) {
        canvas.save();
        paint.setColor(getColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getStrokeWidth());
        //将画布旋转    相当于旋转了 雪花本身   并且更加的 节省资源
        canvas.rotate(getRotation(), getX(), getY());//

        /**
         * 绘制一瓣雪花的过程   再重复绘制 过程六次就可以得到完整的雪花
         * 画布的开始和结束   都要将画布保存和取出  重新绘制
         *
         * 可以保证每一片 雪花 绘制时 所拿到的画布都是  白纸一样的
         */
        for (int i = 0; i < 6; i++) {
            int lineStartX = getX();
            int lineEndX = getX() + getRadius();
            canvas.drawLine(lineStartX, getY(), lineEndX, getY(), paint);

            //雪花瓣的上片叶的绘制
            int line1StartX = (int) (getX() + getRadius() * 0.6);
            int line1StartY = getY();
            double degree60 = Math.toRadians(60);
            int line1EndX = (int) (line1StartX + radius * 0.4 * Math.cos(degree60));
            int line1EndY = (int) (line1StartY - radius * 0.4 * Math.sin(degree60));
            canvas.drawLine(line1StartX, line1StartY, line1EndX, line1EndY, paint);

                //雪花瓣的下片叶的绘制
            int line2StartX = (int) (getX() + getRadius() * 0.6);
            int line2StartY = getY();
            int line2EndX = (int) (line1StartX + radius * 0.4 * Math.cos(degree60));
            int line2EndY = (int) (line1StartY + radius * 0.4 * Math.sin(degree60));
            canvas.drawLine(line2StartX, line2StartY, line2EndX, line2EndY, paint);
                    //也是利用画布的选装绘制六瓣
            canvas.rotate(60, getX(), getY());
        }

        canvas.restore();
    }

    public int getX() {
        /**
         * 自变量t在 零到一之间的取值  根据雪花运动进度的情况 为贝塞尔中间左边的计算给出自变量
         */
        float t = getY() * 1.0f / (getEndPoint().y - getStartPoint().y);
        return BezierUtil.calculatePoint(t, getStartPoint(), getMiddlePoint(), getEndPoint()).x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
