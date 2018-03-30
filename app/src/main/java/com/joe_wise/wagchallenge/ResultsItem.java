package com.joe_wise.wagchallenge;

public class ResultsItem {
    //Class to store our user data from Stack Overflow

    //profile_image
    public String pic_url;
    //display_name
    public String name;
    //badge_counts
    public String bronze;
    public String silver;
    public String gold;

    public String getPic_url() {
        return pic_url;
    }

    public String getName() {
        return name;
    }

    public String getBronze() {
        return bronze;
    }

    public String getSilver() {
        return silver;
    }

    public String getGold() {
        return gold;
    }

    public String getBadges() {
        return "Bronze: " + getBronze() + " Silver: " + getSilver() + " Gold: " + getGold();
    }
}