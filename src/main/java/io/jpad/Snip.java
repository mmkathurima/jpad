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
        Object this$title = this.title;
        Object other$title = other.title;
        if (!Objects.equals(this$title, other$title)) return false;
        Object this$tags = this.tags;
        Object other$tags = other.tags;
        if (!Objects.equals(this$tags, other$tags)) return false;
        Object this$description = this.description;
        Object other$description = other.description;
        if (!Objects.equals(this$description, other$description))
            return false;
        Object this$jpadCode = this.jpadCode;
        Object other$jpadCode = other.jpadCode;
        if (!Objects.equals(this$jpadCode, other$jpadCode)) return false;
        Object this$htmlOut = this.htmlOut;
        Object other$htmlOut = other.htmlOut;
        if (!Objects.equals(this$htmlOut, other$htmlOut)) return false;
        Object this$consoleOut = this.consoleOut;
        Object other$consoleOut = other.consoleOut;
        if (!Objects.equals(this$consoleOut, other$consoleOut))
            return false;
        Object this$bytecodeOut = this.bytecodeOut;
        Object other$bytecodeOut = other.bytecodeOut;
        if (!Objects.equals(this$bytecodeOut, other$bytecodeOut))
            return false;
        Object this$jpadVersion = this.jpadVersion;
        Object other$jpadVersion = other.jpadVersion;
        if (!Objects.equals(this$jpadVersion, other$jpadVersion))
            return false;
        Object this$snipId = this.snipId;
        Object other$snipId = other.snipId;
        return Objects.equals(this$snipId, other$snipId);
    }

    protected boolean canEqual(Object other) {
        return other instanceof Snip;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        Object $title = this.title;
        result = result * 59 + (($title == null) ? 0 : $title.hashCode());
        Object $tags = this.tags;
        result = result * 59 + (($tags == null) ? 0 : $tags.hashCode());
        Object $description = this.description;
        result = result * 59 + (($description == null) ? 0 : $description.hashCode());
        Object $jpadCode = this.jpadCode;
        result = result * 59 + (($jpadCode == null) ? 0 : $jpadCode.hashCode());
        Object $htmlOut = this.htmlOut;
        result = result * 59 + (($htmlOut == null) ? 0 : $htmlOut.hashCode());
        Object $consoleOut = this.consoleOut;
        result = result * 59 + (($consoleOut == null) ? 0 : $consoleOut.hashCode());
        Object $bytecodeOut = this.bytecodeOut;
        result = result * 59 + (($bytecodeOut == null) ? 0 : $bytecodeOut.hashCode());
        Object $jpadVersion = this.jpadVersion;
        result = result * 59 + (($jpadVersion == null) ? 0 : $jpadVersion.hashCode());
        Object $snipId = this.snipId;
        return result * 59 + (($snipId == null) ? 0 : $snipId.hashCode());
    }

    public String toString() {
        return "Snip(title=" + this.title + ", tags=" + this.tags + ", description=" + this.description + ", jpadCode=" + this.jpadCode + ", htmlOut=" + this.htmlOut + ", consoleOut=" + this.consoleOut + ", bytecodeOut=" + this.bytecodeOut + ", jpadVersion=" + this.jpadVersion + ", snipId=" + this.snipId + ")";
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