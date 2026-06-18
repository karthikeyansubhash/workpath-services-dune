
package com.hp.ws.cdm.commonglossary;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Validator {

    /**
     * Unique name of resource. Same name as in the serviceDiscovery.
     * (Required)
     * 
     */
    @SerializedName("resourceGun")
    @Expose
    private String resourceGun;
    /**
     * JSON path to the property in the resource
     * (Required)
     * 
     */
    @SerializedName("propertyPointer")
    @Expose
    private String propertyPointer;
    /**
     * When disabled is applied to an individual data property, it means that property may not be modified. When disabled is applied to an object within options, it means that the disabled option may not be selected. Depending on the experience, views may either grey-out or hide a widget with a data property that has a disabled constraint.The default value for disabled is false.
     * 
     */
    @SerializedName("disabled")
    @Expose
    private Disabled disabled;
    /**
     * This field indicates if a property is supported on this device. The default value is true. Provided in capabilities
     * 
     */
    @SerializedName("supported")
    @Expose
    private Supported__1 supported;
    /**
     * A validator of type string that contains a messageId and optional message.
     * 
     */
    @SerializedName("pattern")
    @Expose
    private StringValidator pattern;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("minLength")
    @Expose
    private IntValidator minLength;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxLength")
    @Expose
    private IntValidator maxLength;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("minLengthInBytes")
    @Expose
    private IntValidator minLengthInBytes;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxLengthInBytes")
    @Expose
    private IntValidator maxLengthInBytes;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("minLengthInCodePoints")
    @Expose
    private IntValidator minLengthInCodePoints;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxLengthInCodePoints")
    @Expose
    private IntValidator maxLengthInCodePoints;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("min")
    @Expose
    private IntValidator min;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("max")
    @Expose
    private IntValidator max;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("step")
    @Expose
    private IntValidator step;
    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    @SerializedName("minDouble")
    @Expose
    private DoubleValidator minDouble;
    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxDouble")
    @Expose
    private DoubleValidator maxDouble;
    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    @SerializedName("stepDouble")
    @Expose
    private DoubleValidator stepDouble;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("minLargeInt")
    @Expose
    private LargeIntValidator minLargeInt;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxLargeInt")
    @Expose
    private LargeIntValidator maxLargeInt;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("stepLargeInt")
    @Expose
    private LargeIntValidator stepLargeInt;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("minSize")
    @Expose
    private IntValidator minSize;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxSize")
    @Expose
    private IntValidator maxSize;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("minDate")
    @Expose
    private DateTimeValidator minDate;
    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    @SerializedName("maxDate")
    @Expose
    private DateTimeValidator maxDate;
    /**
     * attribute for lists of values supported - could be int, boolean, string/enumeration
     * 
     */
    @SerializedName("options")
    @Expose
    private List<Property> options = new ArrayList<Property>();

    /**
     * Unique name of resource. Same name as in the serviceDiscovery.
     * (Required)
     * 
     */
    public String getResourceGun() {
        return resourceGun;
    }

    /**
     * Unique name of resource. Same name as in the serviceDiscovery.
     * (Required)
     * 
     */
    public void setResourceGun(String resourceGun) {
        this.resourceGun = resourceGun;
    }

    /**
     * JSON path to the property in the resource
     * (Required)
     * 
     */
    public String getPropertyPointer() {
        return propertyPointer;
    }

    /**
     * JSON path to the property in the resource
     * (Required)
     * 
     */
    public void setPropertyPointer(String propertyPointer) {
        this.propertyPointer = propertyPointer;
    }

    /**
     * When disabled is applied to an individual data property, it means that property may not be modified. When disabled is applied to an object within options, it means that the disabled option may not be selected. Depending on the experience, views may either grey-out or hide a widget with a data property that has a disabled constraint.The default value for disabled is false.
     * 
     */
    public Disabled getDisabled() {
        return disabled;
    }

    /**
     * When disabled is applied to an individual data property, it means that property may not be modified. When disabled is applied to an object within options, it means that the disabled option may not be selected. Depending on the experience, views may either grey-out or hide a widget with a data property that has a disabled constraint.The default value for disabled is false.
     * 
     */
    public void setDisabled(Disabled disabled) {
        this.disabled = disabled;
    }

    /**
     * This field indicates if a property is supported on this device. The default value is true. Provided in capabilities
     * 
     */
    public Supported__1 getSupported() {
        return supported;
    }

    /**
     * This field indicates if a property is supported on this device. The default value is true. Provided in capabilities
     * 
     */
    public void setSupported(Supported__1 supported) {
        this.supported = supported;
    }

    /**
     * A validator of type string that contains a messageId and optional message.
     * 
     */
    public StringValidator getPattern() {
        return pattern;
    }

    /**
     * A validator of type string that contains a messageId and optional message.
     * 
     */
    public void setPattern(StringValidator pattern) {
        this.pattern = pattern;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMinLength() {
        return minLength;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMinLength(IntValidator minLength) {
        this.minLength = minLength;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMaxLength() {
        return maxLength;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMaxLength(IntValidator maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMinLengthInBytes() {
        return minLengthInBytes;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMinLengthInBytes(IntValidator minLengthInBytes) {
        this.minLengthInBytes = minLengthInBytes;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMaxLengthInBytes() {
        return maxLengthInBytes;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMaxLengthInBytes(IntValidator maxLengthInBytes) {
        this.maxLengthInBytes = maxLengthInBytes;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMinLengthInCodePoints() {
        return minLengthInCodePoints;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMinLengthInCodePoints(IntValidator minLengthInCodePoints) {
        this.minLengthInCodePoints = minLengthInCodePoints;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMaxLengthInCodePoints() {
        return maxLengthInCodePoints;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMaxLengthInCodePoints(IntValidator maxLengthInCodePoints) {
        this.maxLengthInCodePoints = maxLengthInCodePoints;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMin() {
        return min;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMin(IntValidator min) {
        this.min = min;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMax() {
        return max;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMax(IntValidator max) {
        this.max = max;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getStep() {
        return step;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setStep(IntValidator step) {
        this.step = step;
    }

    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    public DoubleValidator getMinDouble() {
        return minDouble;
    }

    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    public void setMinDouble(DoubleValidator minDouble) {
        this.minDouble = minDouble;
    }

    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    public DoubleValidator getMaxDouble() {
        return maxDouble;
    }

    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    public void setMaxDouble(DoubleValidator maxDouble) {
        this.maxDouble = maxDouble;
    }

    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    public DoubleValidator getStepDouble() {
        return stepDouble;
    }

    /**
     * A validator of type double that contains a messageId and optional message.
     * 
     */
    public void setStepDouble(DoubleValidator stepDouble) {
        this.stepDouble = stepDouble;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public LargeIntValidator getMinLargeInt() {
        return minLargeInt;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMinLargeInt(LargeIntValidator minLargeInt) {
        this.minLargeInt = minLargeInt;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public LargeIntValidator getMaxLargeInt() {
        return maxLargeInt;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMaxLargeInt(LargeIntValidator maxLargeInt) {
        this.maxLargeInt = maxLargeInt;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public LargeIntValidator getStepLargeInt() {
        return stepLargeInt;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setStepLargeInt(LargeIntValidator stepLargeInt) {
        this.stepLargeInt = stepLargeInt;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMinSize() {
        return minSize;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMinSize(IntValidator minSize) {
        this.minSize = minSize;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public IntValidator getMaxSize() {
        return maxSize;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMaxSize(IntValidator maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public DateTimeValidator getMinDate() {
        return minDate;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMinDate(DateTimeValidator minDate) {
        this.minDate = minDate;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public DateTimeValidator getMaxDate() {
        return maxDate;
    }

    /**
     * A validator of type integer that contains a messageId and optional message.
     * 
     */
    public void setMaxDate(DateTimeValidator maxDate) {
        this.maxDate = maxDate;
    }

    /**
     * attribute for lists of values supported - could be int, boolean, string/enumeration
     * 
     */
    public List<Property> getOptions() {
        return options;
    }

    /**
     * attribute for lists of values supported - could be int, boolean, string/enumeration
     * 
     */
    public void setOptions(List<Property> options) {
        this.options = options;
    }

}
