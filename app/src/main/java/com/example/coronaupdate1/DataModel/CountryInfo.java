package com.example.coronaupdate1.DataModel;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {

    @SerializedName("_id")
    private int countryId;
    @SerializedName("iso2")
    private String iso2;
    @SerializedName("iso3")
    private String iso3;
    @SerializedName("flag")
    private String flag;

    public CountryInfo(int countryId, String iso2, String iso3, String flag){
        this.countryId = countryId;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.flag = flag;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getIso2() {
        return iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public String getFlag() {
        return flag;
    }
}
