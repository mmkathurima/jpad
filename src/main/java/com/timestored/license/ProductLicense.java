package com.timestored.license;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XStreamAlias("product")
class ProductLicense {
    @XStreamAsAttribute
    private final String title;
    @XStreamAsAttribute
    private final Date startDate;
    @XStreamAsAttribute
    private final Date endDate;
    @XStreamAsAttribute
    private final List<FeatureLicense> featureLicenses = new ArrayList<FeatureLicense>();

    ProductLicense(String title, Date endDate, Date startDate) {
        this.title = Preconditions.checkNotNull(title);
        if (endDate != null && startDate != null) {
            Preconditions.checkArgument(endDate.after(startDate));
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    String getTitle() {
        return this.title;
    }

    boolean addFeature(FeatureLicense fl) {
        if (this.getFeatureLicense(fl.getTitle()) != null) {
            throw new IllegalArgumentException("Feature of that name already exists");
        }
        return this.featureLicenses.add(fl);
    }

    List<FeatureLicense> getFeatureLicenses() {
        return this.featureLicenses;
    }

    boolean isPermitted() {
        return License.validStartEnd(this.startDate, this.endDate);
    }

    public boolean isExpired() {
        return (this.endDate != null && this.endDate.after(new Date()));
    }

    private FeatureLicense getFeatureLicense(String featureTitle) {
        for (FeatureLicense fl : this.featureLicenses) {
            if (fl.getTitle().equals(featureTitle)) {
                return fl;
            }
        }
        return null;
    }

    boolean isPermitted(String feature) {
        FeatureLicense fl = this.getFeatureLicense(feature);
        if (fl != null) {
            return fl.isPermitted();
        }
        return false;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("title", this.title).add("startDate", this.startDate).add("endDate", this.endDate).add("featureLicenses", this.featureLicenses).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.title, this.startDate, this.endDate, this.featureLicenses);
    }

    public boolean equals(Object object) {
        if (object instanceof ProductLicense) {
            ProductLicense that = (ProductLicense) object;
            return (Objects.equal(this.title, that.title) && Objects.equal(this.startDate, that.startDate) && Objects.equal(this.endDate, that.endDate) && Objects.equal(this.featureLicenses, that.featureLicenses));
        }

        return false;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\license\ProductLicense.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */