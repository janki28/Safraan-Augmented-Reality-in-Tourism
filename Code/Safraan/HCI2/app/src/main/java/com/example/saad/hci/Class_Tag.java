package com.example.saad.hci;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Class_Tag {
    private String 	            name;
    private ArrayList<String>   siteNames;

    Class_Tag(JSONObject jsonObject) {
        siteNames               = new ArrayList<>();
        name 		            = jsonObject.optString("name");
        JSONArray siteNames1    = jsonObject.optJSONArray("site_names");

        assert siteNames1 != null;
        for (int i = 0; i < siteNames1.length(); i++) {
            siteNames.add(siteNames1.optString(i));
        }
    }

    public String 	            getName() 			    {return name;}
    ArrayList<String>    getSiteNames()          {return siteNames;}
}
