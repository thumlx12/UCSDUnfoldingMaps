package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/**
 * Implements a visual marker for earthquakes on an earthquake map
 *
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 */
public abstract class EarthquakeMarker extends SimplePointMarker {

    private boolean isPastDay;
    private float magnitude;

    // Did the earthquake occur on land?  This will be set by the subclasses.
    protected boolean isOnLand;

    // SimplePointMarker has a field "radius" which is inherited
    // by Earthquake marker:
    // protected float radius;
    //
    // You will want to set this in the constructor, either
    // using the thresholds below, or a continuous function
    // based on magnitude.


    /**
     * Greater than or equal to this threshold is a moderate earthquake
     */
    public static final float THRESHOLD_MODERATE = 5;
    /**
     * Greater than or equal to this threshold is a light earthquake
     */
    public static final float THRESHOLD_LIGHT = 4;

    /**
     * Greater than or equal to this threshold is an intermediate depth
     */
    public static final float THRESHOLD_INTERMEDIATE = 70;
    /**
     * Greater than or equal to this threshold is a deep depth
     */
    public static final float THRESHOLD_DEEP = 300;

    // ADD constants for colors


    // abstract method implemented in derived classes
    public abstract void drawEarthquake(PGraphics pg, float x, float y);


    // constructor
    public EarthquakeMarker(PointFeature feature) {
        super(feature.getLocation());
        // Add a radius property and then set the properties
        java.util.HashMap<String, Object> properties = feature.getProperties();
        this.magnitude = Float.parseFloat(properties.get("magnitude").toString());
        properties.put("radius", 2 * magnitude);
        setProperties(properties);
        this.radius = 1.75f * this.magnitude;
        String age = properties.get("age").toString();
        if (age.equals("Past Day") || age.equals("Past Hour")) {
            this.isPastDay = true;
        } else {
            this.isPastDay = false;
        }
    }


    // calls abstract method drawEarthquake and then checks age and draws X if needed
    public void draw(PGraphics pg, float x, float y) {
        // save previous styling
        pg.pushStyle();

        // determine color of marker from depth
        colorDetermine(pg);

        // call abstract method implemented in child class to draw marker shape
        drawEarthquake(pg, x, y);

        // OPTIONAL TODO: draw X over marker if within past day
        float CROSS_RADIUS = this.radius;
        if (this.isPastDay) {
            pg.fill(91, 89, 90);
            pg.line(x - 0.707f * CROSS_RADIUS, y - 0.707f * CROSS_RADIUS, x + 0.707f * CROSS_RADIUS, y + 0.707f * CROSS_RADIUS);
            pg.line(x + 0.707f * CROSS_RADIUS, y - 0.707f * CROSS_RADIUS, x - 0.707f * CROSS_RADIUS, y + 0.707f * CROSS_RADIUS);
        }

        // reset to previous styling
        pg.popStyle();

    }

    // determine color of marker from depth
    // We suggest: Deep = red, intermediate = blue, shallow = yellow
    // But this is up to you, of course.
    // You might find the getters below helpful.
    private void colorDetermine(PGraphics pg) {
        //TODO: Implement this method
        if (this.magnitude < 4.0) {
            pg.fill(72, 66, 244);
        } else {
            if (this.magnitude <= 4.9) {
                pg.fill(223, 244, 66);
            } else {
                pg.fill(209, 6, 6);
            }
        }
    }

	
	/*
     * getters for earthquake properties
	 */

    public float getMagnitude() {
        return this.magnitude;
        //return Float.parseFloat(getProperty("magnitude").toString());
    }

    public float getDepth() {
        return Float.parseFloat(getProperty("depth").toString());
    }

    public String getTitle() {
        return (String) getProperty("title");

    }

    public float getRadius() {
        return Float.parseFloat(getProperty("radius").toString());
    }

    public boolean isOnLand() {
        return isOnLand;
    }


}
