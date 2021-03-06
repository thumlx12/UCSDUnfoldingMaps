package module4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 *
 * @author Lixuan Mao
 *         Date: Feb 2, 2017
 */
public class EarthquakeCityMap extends PApplet {

    // We will use member variables, instead of local variables, to store the data
    // that the setUp and draw methods will need to access (as well as other methods)
    // You will use many of these variables, but the only one you should need to add
    // code to modify is countryQuakes, where you will store the number of earthquakes
    // per country.

    // You can ignore this.  It's to get rid of eclipse warnings
    private static final long serialVersionUID = 1L;

    // IF YOU ARE WORKING OFFILINE, change the value of this variable to true
    private static final boolean offline = false;

    /**
     * This is where to find the local tiles, for working without an Internet connection
     */
    public static String mbTilesString = "blankLight-1-3.mbtiles";


    //feed with magnitude 2.5+ Earthquakes
    private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
    //private String earthquakesURL = "test2.atom";

    // The files containing city names and info and country names and info
    private String cityFile = "city-data.json";
    private String countryFile = "countries.geo.json";

    // The map
    private UnfoldingMap map;

    // Markers for each city
    private List<Marker> cityMarkers;
    // Markers for each earthquake
    private List<Marker> quakeMarkers;

    // A List of country markers
    private List<Marker> countryMarkers;

    private List<PointFeature> earthquakes;


    public void setup() {
        // (1) Initializing canvas and map tiles
        size(1500, 768, OPENGL);
        if (offline) {
            map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
            earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
        } else {
            map = new UnfoldingMap(this, width - 1280, 0, 1280, 768, new OpenStreetMap.OpenStreetMapProvider());
            // IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
            //earthquakesURL = "2.5_week.atom";
        }
        MapUtils.createDefaultEventDispatcher(this, map);
        map.zoomAndPanTo(2, new Location(33.424360f, -14.577995f));

        // FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
        // one of the lines below.  This will work whether you are online or offline
        //earthquakesURL = "test1.atom";
        //earthquakesURL = "test2.atom";

        // WHEN TAKING THIS QUIZ: Uncomment the next line
        //earthquakesURL = "quiz1.atom";


        // (2) Reading in earthquake data and geometric properties
        //     STEP 1: load country features and markers
        List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
        countryMarkers = MapUtils.createSimpleMarkers(countries);

        //     STEP 2: read in city data
        List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
        cityMarkers = new ArrayList<Marker>();
        for (Feature city : cities) {
            cityMarkers.add(new CityMarker(city));
        }

        //     STEP 3: read in earthquake RSS feed
        earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
        quakeMarkers = new ArrayList<Marker>();

        for (PointFeature feature : earthquakes) {
            //check if LandQuake
            if (isLand(feature)) {
                quakeMarkers.add(new LandQuakeMarker(feature));
            }
            // OceanQuakes
            else {
                quakeMarkers.add(new OceanQuakeMarker(feature));
            }
        }

        // could be used for debugging
        printQuakes();

        // (3) Add markers to map
        //     NOTE: Country markers are not added to the map.  They are used
        //           for their geometric properties
        map.addMarkers(quakeMarkers);
        map.addMarkers(cityMarkers);

    }  // End setup


    public void draw() {
        background(0);
        map.draw();
        addKey();

    }

    // helper method to draw key in GUI
    // TODO: Update this method as appropriate
    private void addKey() {
        // Remember you can use Processing's graphics methods here
        fill(255, 250, 240);
        rect(10, 50, 200, 300);

        fill(0);
        textAlign(LEFT, CENTER);
        textSize(15);
        text("Earthquake Key", 50, 75);
        textSize(12);
        fill(color(160, 24, 49));
        triangle(50 - 7 * 0.866f, 105 + 7 * 0.5f, 50, 105 - 7, 50 + 7 * 0.866f, 105 + 7 * 0.5f);
        fill(color(255, 255, 255));
        ellipse(50, 135, 10, 10);
        rect(45, 160, 10, 10);

        fill(color(255, 0, 0));
        ellipse(50, 225, 10, 10);
        fill(color(255, 255, 0));
        ellipse(50, 255, 10, 10);
        fill(color(0, 0, 255));
        ellipse(50, 285, 10, 10);

        fill(color(255, 255, 255));
        ellipse(50, 315, 10, 10);
        fill(91, 89, 90);
        line(50 - 0.707f * 5, 315 - 0.707f * 5, 50 + 0.707f * 5, 315 + 0.707f * 5);
        line(50 + 0.707f * 5, 315 - 0.707f * 5, 50 - 0.707f * 5, 315 + 0.707f * 5);

        fill(0, 0, 0);
        text("City Marker", 75, 105);
        text("Land Quake", 75, 135);
        text("Ocean Quake", 75, 165);
        text("Size ~ Magnitude", 50, 195);

        text("Deep", 75, 225);
        text("Intermediate ", 75, 255);
        text("Shallow", 75, 285);
        text("Happened in Past Day", 75, 315);
    }

    public void keyPressed() {
        if (key == 'r') {
            map.zoomAndPanTo(2, new Location(33.424360f, -14.577995f));
        }
    }

    // Checks whether this quake occurred on land.  If it did, it sets the
    // "country" property of its PointFeature to the country where it occurred
    // and returns true.  Notice that the helper method isInCountry will
    // set this "country" property already.  Otherwise it returns false.
    private boolean isLand(PointFeature earthquake) {

        // IMPLEMENT THIS: loop over all countries to check if location is in any of them

        // TODO: Implement this method using the helper method isInCountry
        for (Marker country : countryMarkers) {
            if (isInCountry(earthquake, country)) {
                return true;
            }
        }

        return false;

        // not inside any country
        //return false;
    }

    // prints countries with number of earthquakes
    // You will want to loop through the country markers or country features
    // (either will work) and then for each country, loop through
    // the quakes to count how many occurred in that country.
    // Recall that the country markers have a "name" property,
    // And LandQuakeMarkers have a "country" property set.
    private void printQuakes() {
        // TODO: Implement this method
        Map<String, Integer> quake_times_map = new HashMap<String, Integer>();
        int OceanTimes = 0;
        for (PointFeature earthquake : earthquakes) {
            if (earthquake.getProperty("country") == null) {
                OceanTimes += 1;
            } else {
                String country_name = earthquake.getProperty("country").toString();
                if (!quake_times_map.containsKey(country_name)) {
                    quake_times_map.put(country_name, 1);
                } else {
                    int QuakeTimes = quake_times_map.get(country_name) + 1;
                    quake_times_map.put(country_name, QuakeTimes);
                }
            }
        }
        for (String country : quake_times_map.keySet()) {
            System.out.println(country + ":\t" + quake_times_map.get(country));
        }

        System.out.println("OCEAN QUAKES:\t" + OceanTimes);


    }


    // helper method to test whether a given earthquake is in a given country
    // This will also add the country property to the properties of the earthquake
    // feature if it's in one of the countries.
    // You should not have to modify this code
    private boolean isInCountry(PointFeature earthquake, Marker country) {
        // getting location of feature
        Location checkLoc = earthquake.getLocation();

        // some countries represented it as MultiMarker
        // looping over SimplePolygonMarkers which make them up to use isInsideByLoc
        if (country.getClass() == MultiMarker.class) {

            // looping over markers making up MultiMarker
            for (Marker marker : ((MultiMarker) country).getMarkers()) {

                // checking if inside
                if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
                    earthquake.addProperty("country", country.getProperty("name"));

                    // return if is inside one
                    return true;
                }
            }
        }

        // check if inside country represented by SimplePolygonMarker
        else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
            earthquake.addProperty("country", country.getProperty("name"));

            return true;
        }
        return false;
    }

}
