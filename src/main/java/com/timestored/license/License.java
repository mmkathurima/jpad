package com.timestored.license;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@XStreamAlias("license")
public class License {
    @XStreamAsAttribute
    private final String owner;
    private final List<ProductLicense> productLicense;
    @XStreamAsAttribute
    private Date startDate;
    @XStreamAsAttribute
    private Date endDate;
    @XStreamAsAttribute
    private String company;

    License(Date startDate, Date endDate, String ownersEmail, String company, List<ProductLicense> productLicense) {
        if (endDate != null && startDate != null) {
            Preconditions.checkArgument(endDate.after(startDate));
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = Preconditions.checkNotNull(ownersEmail);
        this.company = company;
        this.productLicense = productLicense;
    }

    static boolean validStartEnd(Date startDate, Date endDate) {
        boolean validEnd = (endDate == null || endDate.after(new Date()));
        boolean validStart = (startDate == null || startDate.before(new Date()));
        return (validStart && validEnd);
    }

    private static void appDates(StringBuilder sb, Date startDate, Date endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate != null) {
            sb.append(" from " + df.format(startDate));
        }
        if (endDate != null) {
            sb.append(" until " + df.format(endDate));
        }
    }

    public static License fromXML(String xml) {
        return (License) getXStream().fromXML(xml);
    }

    private static XStream getXStream() {
        XStream xs = new XStream();
        xs.processAnnotations(License.class);
        xs.processAnnotations(ProductLicense.class);
        xs.processAnnotations(FeatureLicense.class);
        return xs;
    }

    public boolean isPermitted() {
        return validStartEnd(this.startDate, this.endDate);
    }

    private ProductLicense getPermittedProductLicense(String product) {
        for (ProductLicense pl : this.productLicense) {
            if (pl.getTitle().equals(product) && pl.isPermitted()) {
                return pl;
            }
        }
        return null;
    }


    public boolean isPermitted(String product, String feature) {
        ProductLicense pl = getPermittedProductLicense(product);
        if (pl != null) {
            return pl.isPermitted(feature);
        }
        return false;
    }


    public boolean isExpired(String product) {
        ProductLicense pl = getPermittedProductLicense(product);
        if (pl != null) {
            return pl.isExpired();
        }
        return false;
    }


    public boolean isPermitted(String product) {
        return (getPermittedProductLicense(product) != null);
    }

    public String getOwner() {
        return this.owner;
    }

    public String getCompany() {
        return this.company;
    }

    void setCompany(String company) {
        this.company = company;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("startDate", this.startDate).add("endDate", this.endDate).add("owner", this.owner).add("company", this.company).add("productLicense", this.productLicense).toString();
    }

    public String getShortDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("License for " + getOwner());
        appDates(sb, this.startDate, this.endDate);

        for (ProductLicense pl : this.productLicense) {
            sb.append("\r\n" + pl.getTitle());
            appDates(sb, pl.getStartDate(), pl.getEndDate());
        }

        return sb.toString();
    }

    public String toXML() {
        return getXStream().toXML(this);
    }

    public int hashCode() {
        return Objects.hashCode(this.startDate, this.endDate, this.owner, this.company, this.productLicense);
    }

    public boolean equals(Object object) {
        if (object instanceof License) {
            License that = (License) object;
            return (Objects.equal(this.startDate, that.startDate) && Objects.equal(this.endDate, that.endDate) && Objects.equal(this.owner, that.owner) && Objects.equal(this.company, that.company) && Objects.equal(this.productLicense, that.productLicense));
        }


        return false;
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\license\License.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */