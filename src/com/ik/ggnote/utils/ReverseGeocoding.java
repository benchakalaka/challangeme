package com.ik.ggnote.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class ReverseGeocoding {

     private String Address1 = "" , Address2 = "" , City = "" , State = "" , Country = "" , County = "" , PIN = "";

     public void getAddress(String latitude, String longitude) {
          Address1 = "";
          Address2 = "";
          City = "";
          State = "";
          Country = "";
          County = "";
          PIN = "";

          try {

               JSONObject jsonObj = parser_Json.getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true");
               String Status = jsonObj.getString("status");
               if ( Status.equalsIgnoreCase("OK") ) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    for ( int i = 0; i < address_components.length(); i++ ) {
                         JSONObject zero2 = address_components.getJSONObject(i);
                         String long_name = zero2.getString("long_name");
                         JSONArray mtypes = zero2.getJSONArray("types");
                         String Type = mtypes.getString(0);

                         if ( TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "" ) {
                              if ( Type.equalsIgnoreCase("street_number") ) {
                                   Address1 = long_name + " ";
                              } else if ( Type.equalsIgnoreCase("route") ) {
                                   Address1 = Address1 + long_name;
                              } else if ( Type.equalsIgnoreCase("sublocality") ) {
                                   Address2 = long_name;
                              } else if ( Type.equalsIgnoreCase("locality") ) {
                                   // Address2 = Address2 + long_name + ", ";
                                   City = long_name;
                              } else if ( Type.equalsIgnoreCase("administrative_area_level_2") ) {
                                   County = long_name;
                              } else if ( Type.equalsIgnoreCase("administrative_area_level_1") ) {
                                   State = long_name;
                              } else if ( Type.equalsIgnoreCase("country") ) {
                                   Country = long_name;
                              } else if ( Type.equalsIgnoreCase("postal_code") ) {
                                   PIN = long_name;
                              }
                         }
                    }
               }

          } catch (Exception e) {
               e.printStackTrace();
          }

     }

     public String getAddress1() {
          return Address1;

     }

     public String getAddress2() {
          return Address2;

     }

     public String getCity() {
          return City;

     }

     public String getState() {
          return State;

     }

     public String getCountry() {
          return Country;

     }

     public String getCounty() {
          return County;

     }

     public String getPIN() {
          return PIN;
     }

     public static class parser_Json {
          public static JSONObject getJSONfromURL(String url) {

               // initialize
               InputStream is = null;
               String result = "";
               JSONObject jObject = null;

               // http post
               try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();

               } catch (Exception e) {
                    e.printStackTrace();
               }

               // convert response to string
               try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ( (line = reader.readLine()) != null ) {
                         sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
               } catch (Exception e) {
                    e.printStackTrace();
               }

               // try parse the string to a JSON object
               try {
                    jObject = new JSONObject(result);
               } catch (JSONException e) {
                    e.printStackTrace();
               }

               return jObject;
          }

     }

}