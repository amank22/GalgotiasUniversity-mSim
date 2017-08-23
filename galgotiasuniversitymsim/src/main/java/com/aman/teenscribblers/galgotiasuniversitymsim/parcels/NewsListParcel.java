package com.aman.teenscribblers.galgotiasuniversitymsim.parcels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amankapoor on 05/08/17.
 */

public class NewsListParcel {

    /**
     * error : false
     * result : {"topics":[{"id":"2","name":"Admin","email":"kapoor.aman22@gmail.com"},{"id":"3","name":"TechOnly","email":"techonly@gu.com"},{"id":"4","name":"Placements","email":"placements@gu.com"},{"id":"7","name":"Kalakriti","email":"drama@gu.com"},{"id":"9","name":"StudioD","email":"studiod@gu.com"},{"id":"10","name":"Galgotias_Photography","email":"photo@gu.com"},{"id":"11","name":"GalgotiasNews","email":"galgotiasnews@gu.com"},{"id":"12","name":"AllNewsThings","email":"allnews@gu.com"}]}
     * status : 200
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("result")
    private String result;
    @SerializedName("topics")
    private List<NewsTopics> topics;
    @SerializedName("status")
    private int status;

    public List<NewsTopics> getTopics() {
        return topics;
    }

    public boolean getError() {
        return error;
    }

    public String getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }


    public static class NewsTopics {
        /**
         * id : 2
         * name : Admin
         * email : kapoor.aman22@gmail.com
         */

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("profile_pic")
        private String profilePic;
        @SerializedName("follows")
        private boolean follows;

        public String getProfilePic() {
            return profilePic;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public boolean isFollows() {
            return follows;
        }
    }
}
