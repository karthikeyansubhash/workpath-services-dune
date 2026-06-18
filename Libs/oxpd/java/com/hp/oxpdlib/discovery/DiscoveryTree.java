// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.discovery;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * DiscoveryTree objects.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DiscoveryTree implements Parcelable {

    /**
     * XML tag containing values for {@link DiscoveredFeature}
     */
    private static final String XML_TAG__LEDM__SUPPORTED_FEATURE = "SupportedFeature";
    /**
     * XML tag containing values for {@link DiscoveredInterface}
     */
    private static final String XML_TAG__LEDM__SUPPORTED_IFC = "SupportedIfc";
    /**
     * XML tag containing values for {@link DiscoveredTree}
     */
    private static final String XML_TAG__LEDM__SUPPORTED_TREE = "SupportedTree";
    /**
     * XML tag containing value for
     * {@link DiscoveredFeature#minorRevision DiscoveredFeature.minorRevision}
     * {@link DiscoveredTree#minorRevision DiscoveredTree.minorRevision}
     * {@link DiscoveredInterface#minorRevision DiscoveredInterface.minorRevision}
     */
    private static final String XML_TAG__LEDM__MINOR_REVISION = "MinorRevision";
    /**
     * XML tag containing value for
     * {@link DiscoveredFeature#resourceURI DiscoveredFeature.resourceURI}
     * {@link DiscoveredTree#resourceURI DiscoveredTree.resourceURI}
     */
    private static final String XML_TAG__DD__RESOURCE_URI = "ResourceURI";
    /**
     * XML tag containing value for
     * {@link DiscoveredInterface#manifestUri DiscoveredInterface.manifestUri}
     */
    private static final String XML_TAG__DD__MANIFEST_URI = "ManifestURI";
    /**
     * XML tag containing value for
     * {@link DiscoveredFeature#resourceType DiscoveredFeature.resourceType},
     * {@link DiscoveredTree#resourceType DiscoveredTree.resourceType}, or
     * {@link DiscoveredInterface#resourceType DiscoveredInterface.resourceType}
     */
    private static final String XML_TAG__DD__RESOURCE_TYPE = "ResourceType";
    /**
     * XML tag containing value for
     * {@link DiscoveredFeature#revision DiscoveredFeature.revision},
     * {@link DiscoveredTree#revision DiscoveredTree.revision}, or
     * {@link DiscoveredInterface#revision DiscoveredInterface.revision}
     */
    private static final String XML_TAG__DD__REVISION = "Revision";

    /** Default revision value to use */
    private static final String XML_VALUE__DD__MINOR_REVISION_DEFAULT = "1.0";

    /** Dummy xml tag to use for collecting data */
    private static final String XML_TAG__DUMMY_COLLECTOR = "#DummyCollector#";

    /**
     * A list of DiscoveredFeature (may be null).
     */
    private final List<DiscoveredFeature> discoveredFeatures;
    /**
     * A list of DiscoveredInterface (may be null).
     */
    private final List<DiscoveredInterface> discoveredInterfaces;
    /**
     * A list of DiscoveredTree (may be null).
     */
    private final List<DiscoveredTree> discoveredTrees;

    /**
     * Constructor used by the library to construct DiscoveryTree objects.
     * @param tagHandler
     *              Tag handler
     * @hide
     */
    private DiscoveryTree(RestXMLTagHandler tagHandler) throws Error

    {
        // extract the data
        if (tagHandler.getTagData(OXPdDevice.Constants.XML_TAG__XML_PARSE_EXCEPTION) != null) {
            throw new Error(ErrorName.AjaxError, "Parse failed");
        }
        //noinspection unchecked
        this.discoveredFeatures=(List<DiscoveredFeature>) tagHandler.getTagData(XML_TAG__LEDM__SUPPORTED_FEATURE);
        //noinspection unchecked
        this.discoveredInterfaces=(List<DiscoveredInterface>) tagHandler.getTagData(XML_TAG__LEDM__SUPPORTED_IFC);
        //noinspection unchecked
        this.discoveredTrees=(List<DiscoveredTree>) tagHandler.getTagData(XML_TAG__LEDM__SUPPORTED_TREE);
    }

    /**
     * Parse the discovery tree response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              request response
     * @return
     *              DiscoveryTree instance
     * @hide
     */
    public static DiscoveryTree parseDiscoveryTree(OXPdDevice device, HttpRequestResponseContainer requestResponse) throws Error {
        if (requestResponse.response != null) {
            switch(requestResponse.response.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:

                    RestXMLTagHandler tagHandler = new RestXMLTagHandler();

                    RestXMLTagHandler.XMLEndTagHandler ledm_supported_subfield__end =
                            new RestXMLTagHandler.XMLEndTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {

                                    //noinspection ConstantConditions
                                    Bundle collectedInfo = ((Bundle)handler.getTagData(XML_TAG__DUMMY_COLLECTOR));
                                    if (collectedInfo == null) return;

                                    if (TextUtils.equals(localName, XML_TAG__LEDM__SUPPORTED_FEATURE)) {
                                        //noinspection unchecked
                                        List<DiscoveredFeature> features = (List<DiscoveredFeature>) handler.getTagData(localName);
                                        if (features == null) {
                                            features = new ArrayList<DiscoveredFeature>();
                                            handler.setTagData(localName, features);
                                        }
                                        features.add(new DiscoveredFeature(
                                                collectedInfo.getString(XML_TAG__DD__RESOURCE_URI),
                                                collectedInfo.getString(XML_TAG__DD__RESOURCE_TYPE),
                                                collectedInfo.getString(XML_TAG__DD__REVISION),
                                                collectedInfo.getString(XML_TAG__LEDM__MINOR_REVISION, XML_VALUE__DD__MINOR_REVISION_DEFAULT)
                                                ));
                                    }
                                    else if (TextUtils.equals(localName, XML_TAG__LEDM__SUPPORTED_IFC)) {
                                        //noinspection unchecked
                                        List<DiscoveredInterface> interfaces = (List<DiscoveredInterface>) handler.getTagData(localName);
                                        if (interfaces == null) {
                                            interfaces = new ArrayList<DiscoveredInterface>();
                                            handler.setTagData(localName, interfaces);
                                        }
                                        interfaces.add(new DiscoveredInterface(
                                                collectedInfo.getString(XML_TAG__DD__MANIFEST_URI),
                                                collectedInfo.getString(XML_TAG__DD__RESOURCE_TYPE),
                                                collectedInfo.getString(XML_TAG__DD__REVISION),
                                                collectedInfo.getString(XML_TAG__LEDM__MINOR_REVISION, XML_VALUE__DD__MINOR_REVISION_DEFAULT)
                                        ));
                                    }
                                    else if (TextUtils.equals(localName, XML_TAG__LEDM__SUPPORTED_FEATURE)) {
                                        //noinspection unchecked
                                        List<DiscoveredTree> trees = (List<DiscoveredTree>) handler.getTagData(localName);
                                        if (trees == null) {
                                            trees = new ArrayList<DiscoveredTree>();
                                            handler.setTagData(localName, trees);
                                        }
                                        trees.add(new DiscoveredTree(
                                                collectedInfo.getString(XML_TAG__DD__RESOURCE_URI),
                                                collectedInfo.getString(XML_TAG__DD__RESOURCE_TYPE),
                                                collectedInfo.getString(XML_TAG__DD__REVISION),
                                                collectedInfo.getString(XML_TAG__LEDM__MINOR_REVISION, XML_VALUE__DD__MINOR_REVISION_DEFAULT)
                                        ));
                                    }

                                    collectedInfo.clear();
                                }
                            };

                    RestXMLTagHandler.XMLEndTagHandler dd_supported_info__end =
                            new RestXMLTagHandler.XMLEndTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                                    Bundle collectedInfo = ((Bundle)handler.getTagData(XML_TAG__DUMMY_COLLECTOR));
                                    if (collectedInfo != null)
                                        collectedInfo.putString(localName, data);

                                }
                            };

                    // add handlers
                    tagHandler.setXMLHandler(XML_TAG__LEDM__SUPPORTED_FEATURE, null, ledm_supported_subfield__end);
                    tagHandler.setXMLHandler(XML_TAG__LEDM__SUPPORTED_IFC, null, ledm_supported_subfield__end);
                    tagHandler.setXMLHandler(XML_TAG__LEDM__SUPPORTED_TREE, null, ledm_supported_subfield__end);
                    tagHandler.setXMLHandler(XML_TAG__DD__RESOURCE_URI, null, dd_supported_info__end);
                    tagHandler.setXMLHandler(XML_TAG__DD__MANIFEST_URI, null, dd_supported_info__end);
                    tagHandler.setXMLHandler(XML_TAG__DD__RESOURCE_TYPE, null, dd_supported_info__end);
                    tagHandler.setXMLHandler(XML_TAG__DD__REVISION, null, dd_supported_info__end);
                    tagHandler.setXMLHandler(XML_TAG__LEDM__MINOR_REVISION, null, dd_supported_info__end);

                    // initialize collection bucket
                    tagHandler.setTagData(XML_TAG__DUMMY_COLLECTOR, new Bundle());

                    // parse response
                    device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

                    // extract the data
                    return new DiscoveryTree(tagHandler);

                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new Error(ErrorName.NotFound, "404 Requested resource not found");
                case OXPdDevice.Constants.HTTP_SERVER_ERROR:
                    throw new Error(ErrorName.ServerError, "500 Internal server error");
                default:
                    throw new Error(ErrorName.Unknown, "Unknown Error: " + requestResponse.response.getResponseCode());            }

        } else {
            throw new Error(ErrorName.AjaxError, "Connection failed");
        }
    }

    /**
     * Helper method used by service libraries to search the OXPd Discovery Tree for a specific feature.
     * @param resourceType
     *              The resource type string to search for.
     * @param revision
     *              The revision string to search for.
     * @return
     *              Matching {@link DiscoveredFeature} or null if no match found
     */
    public DiscoveredFeature GetDiscoveredFeatureByResourceTypeAndRevision(String resourceType, String revision) {
        if (this.discoveredFeatures == null) return null;
        for(DiscoveredFeature feature : this.discoveredFeatures) {
            if (TextUtils.equals(resourceType, feature.resourceType) && TextUtils.equals(revision, feature.revision)) {
                return feature;
            }
        }
        return null;
    }

    /**
     * Helper method used by service libraries to search the OXPd Discovery Tree for a specific interface.
     * @param resourceType
     *              The resource type string to search for.
     * @param revision
     *              The revision string to search for.
     * @return
     *              Matching {@link DiscoveredInterface} or null if no match found
     */
    public DiscoveredInterface GetDiscoveredInterfaceByResourceTypeAndRevision(String resourceType, String revision) {
        if (this.discoveredInterfaces == null) return null;
        for(DiscoveredInterface ifc : this.discoveredInterfaces) {
            if (TextUtils.equals(resourceType, ifc.resourceType) && TextUtils.equals(revision, ifc.revision)) {
                return ifc;
            }
        }
        return null;
    }

    /**
     * Helper method used by service libraries to search the OXPd Discovery Tree for a specific tree.
     * @param resourceType
     *              The resource type string to search for.
     * @param revision
     *              The revision string to search for.
     * @return
     *              Matching {@link DiscoveredTree} or null if no match is found
     */
    public DiscoveredTree GetDiscoveredTreeByResourceTypeAndRevision(String resourceType, String revision) {
        if (this.discoveredTrees == null) return null;
        for(DiscoveredTree tree : this.discoveredTrees) {
            if (TextUtils.equals(resourceType, tree.resourceType) && TextUtils.equals(revision, tree.revision)) {
                return tree;
            }
        }
        return null;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.discoveredFeatures);
        dest.writeTypedList(this.discoveredInterfaces);
        dest.writeTypedList(this.discoveredTrees);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private DiscoveryTree(Parcel in) {
        this.discoveredFeatures = in.createTypedArrayList(DiscoveredFeature.CREATOR);
        this.discoveredInterfaces = in.createTypedArrayList(DiscoveredInterface.CREATOR);
        this.discoveredTrees = in.createTypedArrayList(DiscoveredTree.CREATOR);
    }

    /**
     * DiscoveryTree creator
     */
    public static final Parcelable.Creator<DiscoveryTree> CREATOR =
            /**
             * Creator
             */
            new Parcelable.Creator<DiscoveryTree>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link DiscoveryTree#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public DiscoveryTree createFromParcel(Parcel in) {
                    return new DiscoveryTree(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public DiscoveryTree[] newArray(int size) {
                    return new DiscoveryTree[size];
                }
            };
}
