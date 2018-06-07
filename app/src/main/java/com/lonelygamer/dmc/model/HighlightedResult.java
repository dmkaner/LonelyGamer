package com.lonelygamer.dmc.model;

/**
 * Created by dyl on 2/20/18.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * An highlighted results holds a data model object along with any number of highlights for this
 * object's attributes.
 *
 * @param <T> The data model type.
 */
public class HighlightedResult<T>
{
    private T result;
    private Map<String, Highlight> highlights = new HashMap<>();

    public HighlightedResult(T result)
    {
        this.result = result;
    }

    public T getResult()
    {
        return result;
    }

    public Highlight getHighlight(String attributeName)
    {
        return highlights.get(attributeName);
    }

    public void addHighlight(String attributeName, Highlight highlight)
    {
        highlights.put(attributeName, highlight);
    }
}
