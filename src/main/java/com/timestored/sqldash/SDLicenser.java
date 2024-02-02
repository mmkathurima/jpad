package com.timestored.sqldash;

import com.timestored.TimeStored;
import com.timestored.license.License;
import com.timestored.license.LicenseBuilder;
import com.timestored.misc.InfoLink;
import com.timestored.misc.MessageSigner;


public class SDLicenser {
    public static final String PROD_NAME = "sqlDashboards";
    private static License license = LicenseBuilder.getInstance("free version").build();

    static {
        String l = SqlDashboardLauncher.getStoredPrefs().get(SqldKey.SIGNED_LICENSE, null);
        if (l != null)
            setSignedLicense(l);
    }

    public static boolean isPermissioned(Section section) {
        return license.isPermitted("sqlDashboards", section.getLicenseFeatureId());
    }

    public static boolean isPermissioned() {
        return license.isPermitted("sqlDashboards");
    }

    public static boolean isLicenseExpired() {
        return license.isExpired("sqlDashboards");
    }

    public static void warnUserThisIsOnlyDemo() {
        String msg = "<html>That functionality is not available in this restricted version.<br />For more information on the benefits of the full version,<br />please see the website:<br />" + TimeStored.Page.SQLDASH_BUY.toAnchor() + "</html>";


        String title = "qStudio Version";
        InfoLink.showMessageDialog(null, msg, title);
    }

    public static boolean requestPermission(Section section) {
        boolean p = isPermissioned(section);
        if (!p) {
            warnUserThisIsOnlyDemo();
        }
        return p;
    }

    public static boolean requestPermission() {
        boolean p = isPermissioned();
        if (!p) {
            warnUserThisIsOnlyDemo();
        }
        return p;
    }

    public static boolean setSignedLicense(String signedLicense) {
        if (signedLicense != null) {
            signedLicense = signedLicense.replace("\r", "").replace("\n", "");
            String m = MessageSigner.getMessage("MIHwMIGoBgcqhkjOOAQBMIGcAkEA/KaCzo4Syrom78z3EQ5SbbB4sF7ey80etKII864WF64B81uRpH5t9jQTxeEu0ImbzRMqzVDZkVG9xD7nN1kuFwIVAJYu3cw2nLqOuyYO5rahJtk0bjjFAkBnhHGyepz0TukaScUUfbGpqvJE8FpDTWSGkx0tFCcbnjUDC3H9c9oXkGmzLik1Yw4cIGI1TQ2iCmxBblC+eUykA0MAAkBDhrGYHGc6FoB5ljIjrrRPST5alK4SAzxEMZrxvcqb8OSGyz3G8VD1vNV2Aa9XoUqZPizlgBPylDMjUl7nnqoz", signedLicense);
            if (m != null) {
                License l = License.fromXML(m);
                license = l;
                SqlDashboardLauncher.getStoredPrefs().put(SqldKey.SIGNED_LICENSE, signedLicense);
                return true;
            }
        }
        return false;
    }

    public static String getLicenseText() {
        return license.getShortDescription();
    }

    public static String getLicenseXml() {
        return license.toXML();
    }

    public enum Section {
        PRO("pro"),
        DEV("dev");

        private final String licenseFeatureId;

        Section(String licenseFeatureId) {
            this.licenseFeatureId = licenseFeatureId;
        }

        public String getLicenseFeatureId() {
            return this.licenseFeatureId;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\SDLicenser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */