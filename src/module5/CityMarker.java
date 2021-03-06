package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Implements a visual marker for cities on an earthquake map
 *
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {

    private final int markerType = 0;

    public static int TRI_SIZE = 6;  // The size of the triangle marker

    public CityMarker(Location location) {
        super(location);
        this.radius = TRI_SIZE;
    }


    public CityMarker(Feature city) {
        super(((PointFeature) city).getLocation(), city.getProperties());
        this.radius = TRI_SIZE;
        // Cities have properties: "name" (city name), "country" (country name)
        // and "population" (population, in millions)
    }


    /**
     * Show the title of the city if this marker is selected
     */
    public void showTitle(PGraphics pg, float x, float y) {

        // TODO: Implement this method
        pg.pushStyle();
        pg.fill(8, 18, 209);
        pg.textSize(12);
        pg.textAlign(37, 39);
        String title = getCity() + ',' + getCountry() + ",Population: " + getStringProperty("population") + "M";
        pg.text(title, x + this.radius + 2, y);
        pg.popStyle();

    }


    /* Local getters for some city properties.
     */
    public String getCity() {
        return getStringProperty("name");
    }

    public String getCountry() {
        return getStringProperty("country");
    }

    public float getPopulation() {
        return Float.parseFloat(getStringProperty("population"));
    }

    public int getMarkerType(){
        return this.markerType;
    }

    public void drawMarker(PGraphics pg, float x, float y) {
        pg.fill(150, 30, 30);
        pg.triangle(x, y - TRI_SIZE, x - TRI_SIZE, y + TRI_SIZE, x + TRI_SIZE, y + TRI_SIZE);
    }

}
