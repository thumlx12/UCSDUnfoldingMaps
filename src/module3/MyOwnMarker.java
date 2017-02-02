package module3;

import processing.core.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

/**
 * Created by jackie on 17-1-28.
 */
public class MyOwnMarker extends AbstractMarker {
    private float mag;
    private int small_marker = 3;
    private int mediu吗m_marker = 6;
    private int big_marker = 9;

    public MyOwnMarker(Location location) {
        super(location);
    }

    public MyOwnMarker(Location loc, float magnitude) {
        super(loc);
        this.mag = magnitude;
    }

    public void setMag(float r) {
        this.mag = r;
    }

    public void draw(PGraphics pg, float x, float y) {
        if (this.mag < 4.0) {
            pg.fill(72, 66, 244);
            pg.ellipse(x, y, small_marker, small_marker);
        } else {
            if (this.mag <= 4.9) {
                pg.fill(223, 244, 66);
                pg.ellipse(x, y, mediu吗m_marker, mediu吗m_marker);
            } else {
                pg.fill(209, 6, 6);
                pg.ellipse(x, y, big_marker, big_marker);
            }
        }
//        pg.fill(255, 100);
//        pg.ellipse(x, y, radius, radius);
//        pg.popStyle();
    }

    public boolean isInside(float x, float y, float mouse_x, float mouse_y) {
        double distance = Math.sqrt((mouse_x - x) * (mouse_x - x) + (mouse_y - y) * (mouse_y - y));
        if (this.mag < 4.0) {
            if (distance <= small_marker) {
                return true;
            } else {
                return false;
            }
        } else {
            if (this.mag <= 4.9) {
                if (distance <= mediu吗m_marker) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (distance <= big_marker) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
