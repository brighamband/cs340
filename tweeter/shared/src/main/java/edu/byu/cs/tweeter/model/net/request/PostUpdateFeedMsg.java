package edu.byu.cs.tweeter.model.net.request;

public class PostUpdateFeedMsg {
    private String authorAlias;
    private long timestamp;
    private String post;

    private PostUpdateFeedMsg() {}

    public PostUpdateFeedMsg(String authorAlias, long timestamp, String post) {
        this.authorAlias = authorAlias;
        this.timestamp = timestamp;
        this.post = post;
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
}
