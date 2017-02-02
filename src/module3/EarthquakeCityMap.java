package module3;

//Java utilities libraries

import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 *
 * @author Lixuan Mao
 *         Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

    // You can ignore this.  It's to keep eclipse from generating a warning.
    private static final long serialVersionUID = 1L;

    // IF YOU ARE WORKING OFFLINE, change the value of this variable to true
    private static final boolean offline = false;

    // Less than this threshold is a light earthquake
    public static final float THRESHOLD_MODERATE = 5;
    // Less than this threshold is a minor earthquake
    public static final float THRESHOLD_LIGHT = 4;

    /**
     * This is where to find the local tiles, for working without an Internet connection
     */
    public static String mbTilesString = "blankLight-1-3.mbtiles";

    // The map
    private UnfoldingMap map;

    //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
    //private String earthquakesURL="http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.atom";

    List<Marker> EqMarkers;


    public void setup() {
        size(1500, 768, OPENGL);

        if (offline) {
            map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
            earthquakesURL = "2.5_week.atom";    // Same feed, saved Aug 7, 2015, for working offline
        } else {
            map = new UnfoldingMap(this, width - 1280, 0, 1280, 768, new OpenStreetMap.OpenStreetMapProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
            //earthquakesURL = "2.5_week.atom";
        }

        map.zoomAndPanTo(2, new Location(33.424360f, -14.577995f));
        MapUtils.createDefaultEventDispatcher(this, map);

        //Use provided parser to collect properties for each earthquake
        //PointFeatures have a getLocation method
        List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

        // These print statements show you (1) all of the relevant properties
        // in the features, and (2) how to get one property and use it
//	    if (earthquakes.size() > 0) {
//	    	PointFeature f = earthquakes.get(0);
//	    	System.out.println(f.getProperties());
//	    	Object magObj = f.getProperty("magnitude");
//	    	float mag = Float.parseFloat(magObj.toString());
//	    	// PointFeatures also have a getLocation method
//	    }
        EqMarkers = new ArrayList<Marker>();
        for (int i = 0; i < earthquakes.size(); i++) {
            EqMarkers.add(createMarker(earthquakes.get(i)));
        }
        map.addMarkers(EqMarkers);

        // Here is an example of how to use Processing's color method to generate
        // an int that represents the color yellow.
        //int yellow = color(255, 255, 0);

        //TODO: Add code here as appropriate
    }

    // A suggested helper method that takes in an earthquake feature and
    // returns a SimplePointMarker for that earthquake
    // TODO: Implement this method and call it from setUp, if it helps
    private MyOwnMarker createMarker(PointFeature feature) {
        // finish implementing and use this method, if it helps.
        float eq_mag = parseFloat(feature.getProperty("magnitude").toString());
        MyOwnMarker eq_marker = new MyOwnMarker(feature.getLocation(), eq_mag);
        return eq_marker;
    }

    public void draw() {
        background(102, 99, 99);
        map.draw();
        addKey();
    }

    public void keyPressed() {
        if (key == 'r') {
            map.zoomAndPanTo(2, new Location(33.424360f, -14.577995f));
        }
    }


    // helper method to draw key in GUI
    // TODO: Implement this method to draw the key
    private void addKey() {
        // Remember you can use Processing's graphics methods here
        fill(244, 230, 230);
        rect(10, 10, 200, 200, 5, 5, 5, 5);

        textSize(20);
        fill(0, 102, 153);
        text("Earthquake Key", 32, 30);

        textSize(15);
        fill(20, 19, 19);
        text("5.0+ Magnitude", 82, 70);
        text("4.0+ Magnitude", 82, 120);
        text("Below 4.0", 82, 170);

        fill(209, 6, 6);
        ellipse(62, 65, 9, 9);
        fill(223, 244, 66);
        ellipse(62, 115, 6, 6);
        fill(72, 66, 244);
        ellipse(62, 165, 3, 3);
    }
}
