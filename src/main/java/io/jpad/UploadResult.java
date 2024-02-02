package io.jpad;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class UploadResult {
    @NotNull
    private final ResultType type;
    @NotNull
    private final String message;
    @NotNull
    private final String url;
    private final long revisionId;

    public UploadResult(@NotNull ResultType type, @NotNull String message, @NotNull String url, long revisionId) {
        if (type == null) throw new NullPointerException("type");
        if (message == null) throw new NullPointerException("message");
        if (url == null) throw new NullPointerException("url");
        this.type = type;
        this.message = message;
        this.url = url;
        this.revisionId = revisionId;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UploadResult)) return false;
        UploadResult other = (UploadResult) o;
        if (!other.canEqual(this)) return false;
        Object this$type = getType(), other$type = other.getType();
        if (!Objects.equals(this$type, other$type)) return false;
        Object this$message = getMessage(), other$message = other.getMessage();
        if (!Objects.equals(this$message, other$message)) return false;
        Object this$url = getUrl(), other$url = other.getUrl();
        return Objects.equals(this$url, other$url) && (getRevisionId() == other.getRevisionId());
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploadResult;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = getType();
        result = result * 59 + (($type == null) ? 0 : $type.hashCode());
        Object $message = getMessage();
        result = result * 59 + (($message == null) ? 0 : $message.hashCode());
        Object $url = getUrl();
        result = result * 59 + (($url == null) ? 0 : $url.hashCode());
        long $revisionId = getRevisionId();
        return result * 59 + (int) ($revisionId >>> 32L ^ $revisionId);
    }

    public String toString() {
        return "UploadResult(type=" + getType() + ", message=" + getMessage() + ", url=" + getUrl() + ", revisionId=" + getRevisionId() + ")";
    }

    @NotNull
    public ResultType getType() {
        return this.type;
    }

    public long getRevisionId() {
        return this.revisionId;
    }

    String getMessage() {
        return this.message;
    }

    String getUrl() {
        return this.url;
    }

    boolean isSuccessful() {
        return this.type.equals(ResultType.SUCCESS);
    }

    enum ResultType {SUCCESS, ERROR}
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\UploadResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */