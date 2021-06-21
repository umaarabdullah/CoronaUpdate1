package com.example.coronaupdate1.DataModel;

import com.google.gson.annotations.SerializedName;

public class CountryData {

    @SerializedName("country")
    private String countryName;
    @SerializedName("active")
    private int activeCases;
    @SerializedName("cases")
    private int totalCases;
    @SerializedName("todayCases")
    private int newCases;
    @SerializedName("deaths")
    private int totalDeaths;
    @SerializedName("todayDeaths")
    private int newDeaths;
    @SerializedName("recovered")
    private int totalRecovered;
    @SerializedName("todayRecovered")
    private int newRecovered;
    @SerializedName("tests")
    private int totalTests;

    private CountryInfo countryInfo;

    public CountryData(String countryName, int activeCases, int totalCases, int newCases, int totalDeaths, int newDeaths,
                       int totalRecovered, int newRecovered, int totalTests, CountryInfo countryInfo){

        this.countryName = countryName;
        this.activeCases = activeCases;
        this.totalCases = totalCases;
        this.newCases = newCases;
        this.totalDeaths = totalDeaths;
        this.newDeaths = newDeaths;
        this.totalRecovered = totalRecovered;
        this.newRecovered = newRecovered;
        this.totalTests = totalTests;
        this.countryInfo = countryInfo;

    }

    public String getCountryName() {
        return countryName;
    }

    public int getActiveCases() {
        return activeCases;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getNewCases() {
        return newCases;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }
}
