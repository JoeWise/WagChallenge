package com.joe_wise.wagchallenge;

import java.util.ArrayList;

public class Response {

    //List of ResultsItems
    private ArrayList<ResultsItem> results = new ArrayList<ResultsItem>();

    //has_more JSON variable indicating if there is another page of data
    private boolean hasMore;

    public ArrayList<ResultsItem> getResults() {
        return results;
    }

    public void addResult(ResultsItem r) {
        results.add(r);
    }

    public void setHasMore(boolean b) { hasMore = b; }

    public boolean getHasMore() { return hasMore; }

}