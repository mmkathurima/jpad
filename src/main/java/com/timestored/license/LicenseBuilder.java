package com.timestored.license;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LicenseBuilder {
    private final List<ProductLicense> productLicenses = new ArrayList<>();
    private final String owner;
    private Date startDate;
    private Date endDate;
    private String company;

    private LicenseBuilder(String owner) {
        this.owner = Preconditions.checkNotNull(owner);
    }

    public static LicenseBuilder getInstance(String owner) {
        return new LicenseBuilder(owner);
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public LicenseBuilder addProduct(String productTitle, Date endDate, Date startDate) {
        ProductLicense pl = this.getProductLicense(productTitle);
        if (pl != null) {
            throw new IllegalArgumentException("product already exists");
        }
        this.productLicenses.add(new ProductLicense(productTitle, endDate, startDate));
        return this;
    }

    public LicenseBuilder addProduct(String productTitle, Date endDate) {
        return this.addProduct(productTitle, endDate, null);
    }

    public LicenseBuilder addProduct(String productTitle) {
        return this.addProduct(productTitle, null);
    }

    public LicenseBuilder addFeature(String productTitle, String title, Date endDate, Date startDate) {
        this.addFeature(productTitle, new FeatureLicense(title, endDate, startDate));
        return this;
    }

    public LicenseBuilder addFeature(String productTitle, String title, Date endDate) {
        this.addFeature(productTitle, new FeatureLicense(title, endDate));
        return this;
    }

    public LicenseBuilder addFeature(String productTitle, String title) {
        this.addFeature(productTitle, new FeatureLicense(title));
        return this;
    }

    private void addFeature(String productTitle, FeatureLicense fl) {
        ProductLicense prodLic = this.getProductLicense(productTitle);
        if (prodLic != null) {
            prodLic.addFeature(fl);
        } else {
            throw new IllegalArgumentException("The product must be added before you can assign features");
        }
    }

    public ProductLicense getProductLicense(String productTitle) {
        for (ProductLicense pl : this.productLicenses) {
            if (pl.getTitle().equals(productTitle)) {
                return pl;
            }
        }
        return null;
    }

    public License build() {
        return new License(this.startDate, this.endDate, this.owner, this.company, this.productLicenses);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\license\LicenseBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */