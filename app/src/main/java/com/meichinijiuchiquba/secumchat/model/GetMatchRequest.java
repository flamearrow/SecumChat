package com.meichinijiuchiquba.secumchat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * {@link com.meichinijiuchiquba.secumchat.net.SecumAPI#getMatch(GetMatchRequest)}
 */

public class GetMatchRequest {
    private GetMatchRequest(String lat, String lng, String min_age, String max_age, String gender,
                            String tag, String distance_limit_in_m) {
        this.lat = lat;
        this.lng = lng;
        this.min_age = min_age;
        this.max_age = max_age;
        this.gender = gender;
        this.tag = tag;
        this.distance_limit_in_m = distance_limit_in_m;
    }

    @Expose
    @SerializedName("lat")
    String lat;

    @Expose
    @SerializedName("lng")
    String lng;

    @Expose
    @SerializedName("min_age")
    String min_age;

    @Expose
    @SerializedName("max_age")
    String max_age;

    @Expose
    @SerializedName("gender")
    String gender;

    @Expose
    @SerializedName("tag")
    String tag;

    @Expose
    @SerializedName("distance_limit_in_m")
    String distance_limit_in_m;

    public static class Builder {
        private String lat;
        private String lng;
        private String min_age;
        private String max_age;
        private String gender;
        private String tag;
        private String distance_limit_in_m;

        public GetMatchRequest build() {
            return new GetMatchRequest(lat, lng, min_age, max_age, gender, tag,
                    distance_limit_in_m);
        }

        public Builder setDistance_limit_in_m(String distance_limit_in_m) {
            this.distance_limit_in_m = distance_limit_in_m;
            return this;
        }

        public Builder setLat(String lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLng(String lng) {
            this.lng = lng;
            return this;
        }

        public Builder setMin_age(String min_age) {
            this.min_age = min_age;
            return this;
        }

        public Builder setMax_age(String max_age) {
            this.max_age = max_age;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

    }
}
