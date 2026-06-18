
package com.hp.ws.cdm.alert;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Constraints;


/**
 * Moving to 1.5.0-beta.1 for ratification
 * 
 */
public class Com_hp_cdm_service_alert_version_1_schema {

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("alerts")
    @Expose
    private Alerts alerts;
    @SerializedName("alert")
    @Expose
    private Alert alert;
    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("errorAlerts")
    @Expose
    private Alerts errorAlerts;
    @SerializedName("errorAlert")
    @Expose
    private Alert errorAlert;
    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("criticalAlerts")
    @Expose
    private Alerts criticalAlerts;
    @SerializedName("criticalAlert")
    @Expose
    private Alert criticalAlert;
    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("alertsPrivate")
    @Expose
    private Alerts alertsPrivate;
    @SerializedName("alertPrivate")
    @Expose
    private Alert alertPrivate;
    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("errorAlertsPrivate")
    @Expose
    private Alerts errorAlertsPrivate;
    @SerializedName("errorAlertPrivate")
    @Expose
    private Alert errorAlertPrivate;
    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("criticalAlertsPrivate")
    @Expose
    private Alerts criticalAlertsPrivate;
    @SerializedName("criticalAlertPrivate")
    @Expose
    private Alert criticalAlertPrivate;
    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    @SerializedName("alertsPrivateFp")
    @Expose
    private Alerts alertsPrivateFp;
    @SerializedName("alertPrivateFp")
    @Expose
    private Alert alertPrivateFp;
    @SerializedName("capabilities")
    @Expose
    private Constraints capabilities;

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getAlerts() {
        return alerts;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setAlerts(Alerts alerts) {
        this.alerts = alerts;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getErrorAlerts() {
        return errorAlerts;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setErrorAlerts(Alerts errorAlerts) {
        this.errorAlerts = errorAlerts;
    }

    public Alert getErrorAlert() {
        return errorAlert;
    }

    public void setErrorAlert(Alert errorAlert) {
        this.errorAlert = errorAlert;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getCriticalAlerts() {
        return criticalAlerts;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setCriticalAlerts(Alerts criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    public Alert getCriticalAlert() {
        return criticalAlert;
    }

    public void setCriticalAlert(Alert criticalAlert) {
        this.criticalAlert = criticalAlert;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getAlertsPrivate() {
        return alertsPrivate;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setAlertsPrivate(Alerts alertsPrivate) {
        this.alertsPrivate = alertsPrivate;
    }

    public Alert getAlertPrivate() {
        return alertPrivate;
    }

    public void setAlertPrivate(Alert alertPrivate) {
        this.alertPrivate = alertPrivate;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getErrorAlertsPrivate() {
        return errorAlertsPrivate;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setErrorAlertsPrivate(Alerts errorAlertsPrivate) {
        this.errorAlertsPrivate = errorAlertsPrivate;
    }

    public Alert getErrorAlertPrivate() {
        return errorAlertPrivate;
    }

    public void setErrorAlertPrivate(Alert errorAlertPrivate) {
        this.errorAlertPrivate = errorAlertPrivate;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getCriticalAlertsPrivate() {
        return criticalAlertsPrivate;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setCriticalAlertsPrivate(Alerts criticalAlertsPrivate) {
        this.criticalAlertsPrivate = criticalAlertsPrivate;
    }

    public Alert getCriticalAlertPrivate() {
        return criticalAlertPrivate;
    }

    public void setCriticalAlertPrivate(Alert criticalAlertPrivate) {
        this.criticalAlertPrivate = criticalAlertPrivate;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public Alerts getAlertsPrivateFp() {
        return alertsPrivateFp;
    }

    /**
     * List of alerts in order of priority/severity/sequence number
     * 
     */
    public void setAlertsPrivateFp(Alerts alertsPrivateFp) {
        this.alertsPrivateFp = alertsPrivateFp;
    }

    public Alert getAlertPrivateFp() {
        return alertPrivateFp;
    }

    public void setAlertPrivateFp(Alert alertPrivateFp) {
        this.alertPrivateFp = alertPrivateFp;
    }

    public Constraints getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Constraints capabilities) {
        this.capabilities = capabilities;
    }

}
