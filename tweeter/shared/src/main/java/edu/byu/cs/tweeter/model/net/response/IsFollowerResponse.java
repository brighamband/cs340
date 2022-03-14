package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class IsFollowerResponse extends Response {
    private boolean isFollower;

    /**
     * Unsuccessful response
     */
    public IsFollowerResponse(String message) {
        super(false, message);
    }

    /**
     * Successful response
     */
    public IsFollowerResponse(boolean isFollower) {
        super(true);
        this.isFollower = isFollower;
    }

    public boolean getIsFollower() {
        return isFollower;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        IsFollowerResponse that = (IsFollowerResponse) param;

        return (Objects.equals(isFollower, that.isFollower) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFollower);
    }
}
