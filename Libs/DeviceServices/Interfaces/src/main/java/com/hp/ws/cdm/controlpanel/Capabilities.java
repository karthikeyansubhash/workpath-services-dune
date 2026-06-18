
package com.hp.ws.cdm.controlpanel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Capabilities {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * Width of the control panel in pixels
     * 
     */
    @SerializedName("screenWidthPx")
    @Expose
    private Integer screenWidthPx;
    /**
     * Height of the control panel in pixels
     * 
     */
    @SerializedName("screenHeightPx")
    @Expose
    private Integer screenHeightPx;
    /**
     * Physical width of control panel in mm
     * 
     */
    @SerializedName("screenWidthMm")
    @Expose
    private Integer screenWidthMm;
    /**
     * Physical height of control panel in mm
     * 
     */
    @SerializedName("screenHeightMm")
    @Expose
    private Integer screenHeightMm;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("touch")
    @Expose
    private Property.FeatureEnabled touch;
    /**
     * The bit depth of the control panel
     * 
     */
    @SerializedName("bitsPerPixel")
    @Expose
    private Integer bitsPerPixel;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("color")
    @Expose
    private Property.FeatureEnabled color;
    /**
     * Degree with respect to the device: 0, 90, 180, 270
     * 
     */
    @SerializedName("orientation")
    @Expose
    private Integer orientation;
    /**
     * Ratio of the control panel height to width
     * 
     */
    @SerializedName("aspectRatio")
    @Expose
    private String aspectRatio;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("soundSupported")
    @Expose
    private Property.FeatureEnabled soundSupported;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("brightnessSupported")
    @Expose
    private Property.FeatureEnabled brightnessSupported;
    /**
     * The name of the experience presented on the control panel
     * 
     */
    @SerializedName("experience")
    @Expose
    private String experience;
    /**
     * The image formats supported on the control panel
     * 
     */
    @SerializedName("imageFormats")
    @Expose
    private List<ImageFormat> imageFormats = new ArrayList<ImageFormat>();
    /**
     * The minimum size in pixels for a home screen application image
     * 
     */
    @SerializedName("homeScreenAppImageSizeMin")
    @Expose
    private Integer homeScreenAppImageSizeMin;
    /**
     * The maximum size in pixels for a home screen application image
     * 
     */
    @SerializedName("homeScreenAppImageSizeMax")
    @Expose
    private Integer homeScreenAppImageSizeMax;
    /**
     * The hard keys supported by the control panel
     * 
     */
    @SerializedName("hardKeys")
    @Expose
    private List<HardKey> hardKeys = new ArrayList<HardKey>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("leds")
    @Expose
    private Property.FeatureEnabled leds;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("physicalKeyboard")
    @Expose
    private Property.FeatureEnabled physicalKeyboard;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("iterativeSoundSupported")
    @Expose
    private Property.FeatureEnabled iterativeSoundSupported;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Width of the control panel in pixels
     * 
     */
    public Integer getScreenWidthPx() {
        return screenWidthPx;
    }

    /**
     * Width of the control panel in pixels
     * 
     */
    public void setScreenWidthPx(Integer screenWidthPx) {
        this.screenWidthPx = screenWidthPx;
    }

    /**
     * Height of the control panel in pixels
     * 
     */
    public Integer getScreenHeightPx() {
        return screenHeightPx;
    }

    /**
     * Height of the control panel in pixels
     * 
     */
    public void setScreenHeightPx(Integer screenHeightPx) {
        this.screenHeightPx = screenHeightPx;
    }

    /**
     * Physical width of control panel in mm
     * 
     */
    public Integer getScreenWidthMm() {
        return screenWidthMm;
    }

    /**
     * Physical width of control panel in mm
     * 
     */
    public void setScreenWidthMm(Integer screenWidthMm) {
        this.screenWidthMm = screenWidthMm;
    }

    /**
     * Physical height of control panel in mm
     * 
     */
    public Integer getScreenHeightMm() {
        return screenHeightMm;
    }

    /**
     * Physical height of control panel in mm
     * 
     */
    public void setScreenHeightMm(Integer screenHeightMm) {
        this.screenHeightMm = screenHeightMm;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getTouch() {
        return touch;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setTouch(Property.FeatureEnabled touch) {
        this.touch = touch;
    }

    /**
     * The bit depth of the control panel
     * 
     */
    public Integer getBitsPerPixel() {
        return bitsPerPixel;
    }

    /**
     * The bit depth of the control panel
     * 
     */
    public void setBitsPerPixel(Integer bitsPerPixel) {
        this.bitsPerPixel = bitsPerPixel;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getColor() {
        return color;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setColor(Property.FeatureEnabled color) {
        this.color = color;
    }

    /**
     * Degree with respect to the device: 0, 90, 180, 270
     * 
     */
    public Integer getOrientation() {
        return orientation;
    }

    /**
     * Degree with respect to the device: 0, 90, 180, 270
     * 
     */
    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    /**
     * Ratio of the control panel height to width
     * 
     */
    public String getAspectRatio() {
        return aspectRatio;
    }

    /**
     * Ratio of the control panel height to width
     * 
     */
    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSoundSupported() {
        return soundSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSoundSupported(Property.FeatureEnabled soundSupported) {
        this.soundSupported = soundSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBrightnessSupported() {
        return brightnessSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBrightnessSupported(Property.FeatureEnabled brightnessSupported) {
        this.brightnessSupported = brightnessSupported;
    }

    /**
     * The name of the experience presented on the control panel
     * 
     */
    public String getExperience() {
        return experience;
    }

    /**
     * The name of the experience presented on the control panel
     * 
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    /**
     * The image formats supported on the control panel
     * 
     */
    public List<ImageFormat> getImageFormats() {
        return imageFormats;
    }

    /**
     * The image formats supported on the control panel
     * 
     */
    public void setImageFormats(List<ImageFormat> imageFormats) {
        this.imageFormats = imageFormats;
    }

    /**
     * The minimum size in pixels for a home screen application image
     * 
     */
    public Integer getHomeScreenAppImageSizeMin() {
        return homeScreenAppImageSizeMin;
    }

    /**
     * The minimum size in pixels for a home screen application image
     * 
     */
    public void setHomeScreenAppImageSizeMin(Integer homeScreenAppImageSizeMin) {
        this.homeScreenAppImageSizeMin = homeScreenAppImageSizeMin;
    }

    /**
     * The maximum size in pixels for a home screen application image
     * 
     */
    public Integer getHomeScreenAppImageSizeMax() {
        return homeScreenAppImageSizeMax;
    }

    /**
     * The maximum size in pixels for a home screen application image
     * 
     */
    public void setHomeScreenAppImageSizeMax(Integer homeScreenAppImageSizeMax) {
        this.homeScreenAppImageSizeMax = homeScreenAppImageSizeMax;
    }

    /**
     * The hard keys supported by the control panel
     * 
     */
    public List<HardKey> getHardKeys() {
        return hardKeys;
    }

    /**
     * The hard keys supported by the control panel
     * 
     */
    public void setHardKeys(List<HardKey> hardKeys) {
        this.hardKeys = hardKeys;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getLeds() {
        return leds;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setLeds(Property.FeatureEnabled leds) {
        this.leds = leds;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPhysicalKeyboard() {
        return physicalKeyboard;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPhysicalKeyboard(Property.FeatureEnabled physicalKeyboard) {
        this.physicalKeyboard = physicalKeyboard;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIterativeSoundSupported() {
        return iterativeSoundSupported;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIterativeSoundSupported(Property.FeatureEnabled iterativeSoundSupported) {
        this.iterativeSoundSupported = iterativeSoundSupported;
    }

}
