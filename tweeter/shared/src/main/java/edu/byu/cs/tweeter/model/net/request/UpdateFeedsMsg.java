package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

public class UpdateFeedsMsg {
    private String authorAlias;
    private long timestamp;
    private String post;
    private List<String> followerAliases;

    private UpdateFeedsMsg() {}

    public UpdateFeedsMsg(String authorAlias, long timestamp, String post, List<String> followerAliases) {
        this.authorAlias = authorAlias;
        this.timestamp = timestamp;
        this.post = post;
        this.followerAliases = followerAliases;
    }

    public String getAuthorAlias() {
        return authorAlias;
    }

    public void setAuthorAlias(String authorAlias) {
        this.authorAlias = authorAlias;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getFollowerAliases() {
        return followerAliases;
    }

    public void setFollowerAliases(List<String> followerAliases) {
        this.followerAliases = followerAliases;
    }
}
