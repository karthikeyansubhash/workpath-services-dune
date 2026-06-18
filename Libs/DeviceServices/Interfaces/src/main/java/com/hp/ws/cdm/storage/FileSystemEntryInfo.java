
package com.hp.ws.cdm.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class FileSystemEntryInfo {

    /**
     * The type of entry, file or directory.
     * 
     */
    @SerializedName("type")
    @Expose
    private FileSystemEntryInfo.Type type;
    /**
     * The file/directory name
     * 
     */
    @SerializedName("name")
    @Expose
    private String name;
    /**
     * The size of the file (not present for directories)
     * 
     */
    @SerializedName("size")
    @Expose
    private String size;
    /**
     * The last time the file was modified
     * 
     */
    @SerializedName("dateModified")
    @Expose
    private String dateModified;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("hidden")
    @Expose
    private Property.FeatureEnabled hidden;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * The type of entry, file or directory.
     * 
     */
    public FileSystemEntryInfo.Type getType() {
        return type;
    }

    /**
     * The type of entry, file or directory.
     * 
     */
    public void setType(FileSystemEntryInfo.Type type) {
        this.type = type;
    }

    /**
     * The file/directory name
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The file/directory name
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The size of the file (not present for directories)
     * 
     */
    public String getSize() {
        return size;
    }

    /**
     * The size of the file (not present for directories)
     * 
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * The last time the file was modified
     * 
     */
    public String getDateModified() {
        return dateModified;
    }

    /**
     * The last time the file was modified
     * 
     */
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHidden() {
        return hidden;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHidden(Property.FeatureEnabled hidden) {
        this.hidden = hidden;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * The type of entry, file or directory.
     * 
     */
    public enum Type {

        @SerializedName("file")
        FILE("file"),
        @SerializedName("directory")
        DIRECTORY("directory");
        private final String value;
        private final static Map<String, FileSystemEntryInfo.Type> CONSTANTS = new HashMap<String, FileSystemEntryInfo.Type>();

        static {
            for (FileSystemEntryInfo.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FileSystemEntryInfo.Type fromValue(String value) {
            FileSystemEntryInfo.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
