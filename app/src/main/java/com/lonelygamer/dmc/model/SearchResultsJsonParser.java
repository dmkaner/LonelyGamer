package com.lonelygamer.dmc.model;

/**
 * Created by dyl on 2/20/18.
 */

import com.lonelygamer.dmc.GameInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Parses the JSON output of a search query.
 */
public class SearchResultsJsonParser
{
    private GameJsonParser gameParser = new GameJsonParser();

    /**
     * Parse the root result JSON object into a list of results.
     *
     * @param jsonObject The result's root object.
     * @return A list of results (potentially empty), or null in case of error.
     */
    public List<HighlightedResult<GameInformation>> parseResults(JSONObject jsonObject)
    {
        if (jsonObject == null)
            return null;

        List<HighlightedResult<GameInformation>> results = new ArrayList<>();
        JSONArray hits = jsonObject.optJSONArray("hits");
        if (hits == null)
            return null;

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null)
                continue;

            GameInformation movie = gameParser.parse(hit);
            if (movie == null)
                continue;

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null)
                continue;
            JSONObject highlightTitle = highlightResult.optJSONObject("title");
            if (highlightTitle == null)
                continue;
            String value = highlightTitle.optString("value");
            if (value == null)
                continue;
            HighlightedResult<GameInformation> result = new HighlightedResult<>(movie);
            result.addHighlight("title", new Highlight("title", value));
            results.add(result);
        }
        return results;
    }
}
