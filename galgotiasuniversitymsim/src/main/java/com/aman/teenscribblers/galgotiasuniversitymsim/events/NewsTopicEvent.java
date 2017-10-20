package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsTopicListParcel;

import java.util.List;

/**
 * Created by amankapoor on 07/08/17.
 */

public class NewsTopicEvent {

    private String error;
    private List<NewsTopicListParcel.NewsTopics> topics;

    public NewsTopicEvent(String error) {
        this.error = error;
        this.topics = null;
    }

    public NewsTopicEvent(List<NewsTopicListParcel.NewsTopics> topics) {
        this.topics = topics;
        this.error = null;
    }

    public String getError() {
        return error;
    }

    public List<NewsTopicListParcel.NewsTopics> getTopics() {
        return topics;
    }
}
