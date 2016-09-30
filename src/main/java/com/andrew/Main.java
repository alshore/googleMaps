package com.andrew;

import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.*;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {

        // initialize scanner
        Scanner s = new Scanner(System.in);
        // initialize key variable as null
        String key = null;
        // initialize reader with try-catch block, opening geokey text file for reading
        try (BufferedReader bufReader = new BufferedReader(new FileReader("geokey.txt"))) {
            key = bufReader.readLine();
        }
        // catch io exception
        catch (IOException ioe) {
            System.out.println("Cannot find key file.");
            System.out.println("Error message: " + ioe);
        }
        // create api context setting api key
        GeoApiContext context = new GeoApiContext().setApiKey(key);
        // initialize geocoding request variable
        GeocodingApiRequest locReq = new GeocodingApiRequest(context);
        // prompt user for input
        System.out.println("Please enter a location: ");
        String location = s.nextLine();
        // send user input for address request to google api
        GeocodingResult[] geocodingResArray = locReq.address(location).await();
        // use bounds and geometry to attempt creating bounds for results
        Bounds bounds = new Geometry().bounds;
        // print location request data for confirmation of return
        System.out.println(locReq);
        // if results exist, display lat long using geometry specified
        if (geocodingResArray.length > 0) {
            GeocodingResult geoCode1 = geocodingResArray[0];
            LatLng latlng = geoCode1.geometry.location;
            System.out.println(latlng);
            ElevationResult[] elevResult = ElevationApi.getByPoints(context, latlng).await();
        } // if no results based on user input, display error
        else {
            System.out.println("Couldn't locate results!");
        }

    }
}