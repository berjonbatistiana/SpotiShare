package com.spotishare.frey.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by frey on 11/14/16.
 */

public class SpotifyProfile {

    boolean success = false;

    String birthdate;
    String coutnryCode;
    String displayName;
    String email;
    HashMap<String, String> extUrl;
    Followers followers;
    ArrayList<Image> images;
    String product;
    String type;
    String uri;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCoutnryCode() {
        return coutnryCode;
    }

    public void setCoutnryCode(String coutnryCode) {
        this.coutnryCode = coutnryCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, String> getExtUrl() {
        return extUrl;
    }

    public void setExtUrl(HashMap<String, String> extUrl) {
        this.extUrl = extUrl;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SpotifyProfile (String stringSpotifyProfile){

        try {
            JSONObject jsonSpotifyProfile = new JSONObject(stringSpotifyProfile);

            birthdate = jsonSpotifyProfile.getString("birthdate");
            coutnryCode = jsonSpotifyProfile.getString("countryCode");
            displayName = jsonSpotifyProfile.getString("displayName");
            email = jsonSpotifyProfile.getString("email");
            product = jsonSpotifyProfile.getString("product");
            type = jsonSpotifyProfile.getString("type");
            uri = jsonSpotifyProfile.getString("uri");

            JSONObject jsonExtUrl = jsonSpotifyProfile.getJSONObject("external_urls");
            Iterator<String> iter = jsonExtUrl.keys();
            while (iter.hasNext()){
                String key = iter.next();
                String value = jsonExtUrl.getString(key);
                extUrl.put(key, value);
            }

            JSONObject jsonFollowers = jsonSpotifyProfile.getJSONObject("followers");
            followers.setHref(jsonFollowers.getString("href"));
            followers.setTotal(jsonFollowers.getInt("total"));

            JSONArray jsonImagesArray = jsonSpotifyProfile.getJSONArray("images");
            for (int c = 0; c < jsonImagesArray.length(); c++){
                JSONObject jsonImage = jsonImagesArray.getJSONObject(c);
                Image image = new Image();
                image.setHeight(jsonImage.getInt("height"));
                image.setUrl(jsonImage.getString("url"));
                image.setWidth(jsonImage.getInt("width"));
                images.add(image);
            }

            success = true;
        } catch (JSONException e){
            e.printStackTrace();
            success = false;
        }

    }

    class Followers {
        String href;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        int total;
    }

    class Image {

        int height;
        String url;
        int width;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

    }

}
