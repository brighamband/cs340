package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class GetFollowingCountResponse extends Response {

    private int count;

    /**
     * Unsuccessful response
     */
    public GetFollowingCountResponse(String message) {
        super(false, message);
    }

    /**
     * Successful response
     */
    public GetFollowingCountResponse(int count) {
        super(true);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        GetFollowingCountResponse that = (GetFollowingCountResponse) param;

        return (Objects.equals(count, that.count) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
