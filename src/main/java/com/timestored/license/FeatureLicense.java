package com.timestored.license;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.Date;

@XStreamAlias("feature")
class FeatureLicense {
    @XStreamAsAttribute
    private final String title;
    @XStreamAsAttribute
    private final Date startDate;
    @XStreamAsAttribute
    private final Date endDate;

    FeatureLicense(String title, Date endDate, Date startDate) {
        this.title = Preconditions.checkNotNull(title);
        if (endDate != null && startDate != null) {
            Preconditions.checkArgument(endDate.after(startDate));
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    FeatureLicense(String title, Date endDate) {
        this(title, endDate, null);
    }

    FeatureLicense(String title) {
        this(title, null);
    }

    String getTitle() {
        return this.title;
    }

    boolean isPermitted() {
        return License.validStartEnd(this.startDate, this.endDate);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("startDate", this.startDate).add("endDate", this.endDate).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.title, this.startDate, this.endDate);
    }

    public boolean equals(Object object) {
        if (object instanceof FeatureLicense) {
            FeatureLicense that = (FeatureLicense) object;
            return (Objects.equal(this.title, that.title) && Objects.equal(this.startDate, that.startDate) && Objects.equal(this.endDate, that.endDate));
        }

        return false;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\license\FeatureLicense.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */