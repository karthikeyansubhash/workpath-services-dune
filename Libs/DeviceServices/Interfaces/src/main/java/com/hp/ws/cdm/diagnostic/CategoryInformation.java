
package com.hp.ws.cdm.diagnostic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Describes a category
 * 
 */
public class CategoryInformation {

    /**
     * Unsigned integer to sort the values of the categories
     * 
     */
    @SerializedName("categoryIndex")
    @Expose
    private Long categoryIndex;
    /**
     * Enumeration to identify the category of the test
     * (Required)
     * 
     */
    @SerializedName("category")
    @Expose
    private com.hp.ws.cdm.diagnostic.TestInformation.Category category;
    /**
     * Optional textual information in English
     * 
     */
    @SerializedName("categoryDescription")
    @Expose
    private String categoryDescription;

    /**
     * Unsigned integer to sort the values of the categories
     * 
     */
    public Long getCategoryIndex() {
        return categoryIndex;
    }

    /**
     * Unsigned integer to sort the values of the categories
     * 
     */
    public void setCategoryIndex(Long categoryIndex) {
        this.categoryIndex = categoryIndex;
    }

    /**
     * Enumeration to identify the category of the test
     * (Required)
     * 
     */
    public com.hp.ws.cdm.diagnostic.TestInformation.Category getCategory() {
        return category;
    }

    /**
     * Enumeration to identify the category of the test
     * (Required)
     * 
     */
    public void setCategory(com.hp.ws.cdm.diagnostic.TestInformation.Category category) {
        this.category = category;
    }

    /**
     * Optional textual information in English
     * 
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Optional textual information in English
     * 
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

}
