package com.lonelygamer.dmc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.lonelygamer.dmc.model.GameJsonParser;
import com.lonelygamer.dmc.model.Highlight;
import com.lonelygamer.dmc.model.HighlightedResult;
import com.lonelygamer.dmc.GameInformation;

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

            GameInformation game = gameParser.parse(hit);
            if (game == null)
                continue;

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null)
                continue;
            JSONObject highlightTitle = highlightResult.optJSONObject("Name");
            if (highlightTitle == null)
                continue;
            String value = highlightTitle.optString("value");
            if (value == null)
                continue;
            HighlightedResult<GameInformation> result = new HighlightedResult<>(game);
            result.addHighlight("Name", new Highlight("Name", value));
            results.add(result);
        }
        return results;
    }
}