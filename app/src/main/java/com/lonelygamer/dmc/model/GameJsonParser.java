package com.lonelygamer.dmc.model;

/**
 * Created by dyl on 2/20/18.
 */

import org.json.JSONObject;

import com.lonelygamer.dmc.GameInformation;

/**
 * Parses `Movie` instances from their JSON representation.
 */
public class GameJsonParser
{
    /**
     * Parse a single movie record.
     *
     * @param jsonObject JSON object.
     * @return Parsed movie, or null if error.
     */
    public GameInformation parse(JSONObject jsonObject)
    {
        if (jsonObject == null)
            return null;

        String title = jsonObject.optString("Name");
        String image = jsonObject.optString("FilePathName");
        if (title != null && image != null) {
            return new GameInformation(title, image);
        }
        return null;
    }
}