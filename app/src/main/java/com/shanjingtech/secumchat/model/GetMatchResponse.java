package com.shanjingtech.secumchat.model;

/**
 * Created by flamearrow on 2/26/17.
 */

public class GetMatchResponse {
    public boolean success;
    public String caller;
    public String callee;

    public boolean shouldDial() {
        return true;
    }
}
