package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsListParcel;

import java.util.List;

/**
 * Created by amankapoor on 07/08/17.
 */

public class NewsTopicEvent {

    private String error;
    private List<NewsListParcel.NewsTopics> topics;

    public NewsTopicEvent(String error) {
        this.error = error;
        this.topics = null;
    }

    public NewsTopicEvent(List<NewsListParcel.NewsTopics> topics) {
        this.topics = topics;
        this.error = null;
    }

    public String getError() {
        return error;
    }

    public List<NewsListParcel.NewsTopics> getTopics() {
        return topics;
    }
}
