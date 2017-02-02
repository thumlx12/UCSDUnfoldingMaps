package module1;

import processing.core.*;

import java.util.Date;
import java.util.Calendar;


/**
 * Created by jackie on 17-1-27.
 */
public class TestClass extends PApplet {
    private PImage BackgroundImage;
    private Calendar Time;
    private double sec;
    private boolean isChange = false;

    public void setup() {
        size(1280, 720, OPENGL);
        BackgroundImage = loadImage("/home/jackie/Pictures/ss15_1366.jpg", "jpg");
        BackgroundImage.resize(0, height);
        image(BackgroundImage, 0, 0);
    }

    public void draw() {
        sec = isChange == true ? (sec * 1.1 ) : second();
        fill((int) ((255 * (sec / 60)) % 255), (int) ((209 * (sec / 60)) % 209), 0);
        ellipse((3 * width / 4), (height / 5), height / 5, height / 5);
    }

    public void mousePressed() {
        isChange = !isChange;
        redraw();
    }

//    private int x;
//    private int y;
//
//    public TestClass() {
//        this.x = 1;
//        this.y = 2;
//    }
//
//    public TestClass(int x1, int y1) {
//        this.x = x1;
//        this.y = y1;
//    }
//
//    public boolean isEqual(TestClass other) {
//        return this.x == other.x && this.y == other.y;
//    }
//
//    public static void increase(TestClass A){
//        A.x++;
//        A.y++;
//    }
//
//    public static void main(String[] argu) {
////        int a = 1;
////        String b = "Hello";
//        TestClass c = new TestClass();
//        TestClass d = new TestClass(2, 3);
//        increase(c);
//        System.out.println(d.x+"\t"+d.y);
//    }
}
