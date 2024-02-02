package io.jpad;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Snip {
    @NotNull
    private String title = "";
    @NotNull
    private String tags = "";
    @NotNull
    private String description = "";
    @NotNull
    private String jpadCode = "";
    @NotNull
    private String htmlOut = "";
    @NotNull
    private String consoleOut = "";
    @NotNull
    private String bytecodeOut = "";
    @NotNull
    private String jpadVersion = "";
    private String snipId = "";

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Snip)) return false;
        Snip other = (Snip) o;
        if (!other.canEqual(this)) return false;
        Object this$title = getTitle(), other$title = other.getTitle();
        if (!Objects.equals(this$title, other$title)) return false;
        Object this$tags = getTags(), other$tags = other.getTags();
        if (!Objects.equals(this$tags, other$tags)) return false;
        Object this$description = getDescription(), other$description = other.getDescription();
        if (!Objects.equals(this$description, other$description))
            return false;
        Object this$jpadCode = getJpadCode(), other$jpadCode = other.getJpadCode();
        if (!Objects.equals(this$jpadCode, other$jpadCode)) return false;
        Object this$htmlOut = getHtmlOut(), other$htmlOut = other.getHtmlOut();
        if (!Objects.equals(this$htmlOut, other$htmlOut)) return false;
        Object this$consoleOut = getConsoleOut(), other$consoleOut = other.getConsoleOut();
        if (!Objects.equals(this$consoleOut, other$consoleOut))
            return false;
        Object this$bytecodeOut = getBytecodeOut(), other$bytecodeOut = other.getBytecodeOut();
        if (!Objects.equals(this$bytecodeOut, other$bytecodeOut))
            return false;
        Object this$jpadVersion = getJpadVersion(), other$jpadVersion = other.getJpadVersion();
        if (!Objects.equals(this$jpadVersion, other$jpadVersion))
            return false;
        Object this$snipId = getSnipId(), other$snipId = other.getSnipId();
        return Objects.equals(this$snipId, other$snipId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof Snip;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $title = getTitle();
        result = result * 59 + (($title == null) ? 0 : $title.hashCode());
        Object $tags = getTags();
        result = result * 59 + (($tags == null) ? 0 : $tags.hashCode());
        Object $description = getDescription();
        result = result * 59 + (($description == null) ? 0 : $description.hashCode());
        Object $jpadCode = getJpadCode();
        result = result * 59 + (($jpadCode == null) ? 0 : $jpadCode.hashCode());
        Object $htmlOut = getHtmlOut();
        result = result * 59 + (($htmlOut == null) ? 0 : $htmlOut.hashCode());
        Object $consoleOut = getConsoleOut();
        result = result * 59 + (($consoleOut == null) ? 0 : $consoleOut.hashCode());
        Object $bytecodeOut = getBytecodeOut();
        result = result * 59 + (($bytecodeOut == null) ? 0 : $bytecodeOut.hashCode());
        Object $jpadVersion = getJpadVersion();
        result = result * 59 + (($jpadVersion == null) ? 0 : $jpadVersion.hashCode());
        Object $snipId = getSnipId();
        return result * 59 + (($snipId == null) ? 0 : $snipId.hashCode());
    }

    public String toString() {
        return "Snip(title=" + getTitle() + ", tags=" + getTags() + ", description=" + getDescription() + ", jpadCode=" + getJpadCode() + ", htmlOut=" + getHtmlOut() + ", consoleOut=" + getConsoleOut() + ", bytecodeOut=" + getBytecodeOut() + ", jpadVersion=" + getJpadVersion() + ", snipId=" + getSnipId() + ")";
    }

    @NotNull
    public String getTitle() {
        return this.title;
    }

    public void setTitle(@NotNull String title) {
        if (title == null) throw new NullPointerException("title");
        this.title = title;
    }

    @NotNull
    public String getTags() {
        return this.tags;
    }

    public void setTags(@NotNull String tags) {
        if (tags == null) throw new NullPointerException("tags");
        this.tags = tags;
    }

    @NotNull
    public String getDescription() {
        return this.description;
    }

    public void setDescription(@NotNull String description) {
        if (description == null) throw new NullPointerException("description");
        this.description = description;
    }

    @NotNull
    public String getJpadCode() {
        return this.jpadCode;
    }

    public void setJpadCode(@NotNull String jpadCode) {
        if (jpadCode == null) throw new NullPointerException("jpadCode");
        this.jpadCode = jpadCode;
    }

    @NotNull
    public String getHtmlOut() {
        return this.htmlOut;
    }

    public void setHtmlOut(@NotNull String htmlOut) {
        if (htmlOut == null) throw new NullPointerException("htmlOut");
        this.htmlOut = htmlOut;
    }

    @NotNull
    public String getConsoleOut() {
        return this.consoleOut;
    }

    public void setConsoleOut(@NotNull String consoleOut) {
        if (consoleOut == null) throw new NullPointerException("consoleOut");
        this.consoleOut = consoleOut;
    }

    @NotNull
    public String getBytecodeOut() {
        return this.bytecodeOut;
    }

    public void setBytecodeOut(@NotNull String bytecodeOut) {
        if (bytecodeOut == null) throw new NullPointerException("bytecodeOut");
        this.bytecodeOut = bytecodeOut;
    }

    @NotNull
    public String getJpadVersion() {
        return this.jpadVersion;
    }

    public void setJpadVersion(@NotNull String jpadVersion) {
        if (jpadVersion == null) throw new NullPointerException("jpadVersion");
        this.jpadVersion = jpadVersion;
    }

    public String getSnipId() {
        return this.snipId;
    }

    public void setSnipId(String snipId) {
        this.snipId = snipId;
    }

    Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("title", this.title);
        m.put("tags", this.tags);
        m.put("description", this.description);
        m.put("jpadCode", this.jpadCode);
        m.put("htmlOut", this.htmlOut);
        m.put("consoleOut", this.consoleOut);
        m.put("bytecodeOut", this.bytecodeOut);
        m.put("jpadVersion", this.jpadVersion);
        if (!this.snipId.isEmpty()) {
            m.put("snipId", this.snipId);
        }
        return m;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\Snip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */