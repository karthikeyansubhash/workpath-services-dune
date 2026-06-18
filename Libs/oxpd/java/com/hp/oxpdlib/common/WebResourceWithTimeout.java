package com.hp.oxpdlib.common;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.OXPdDevice.Constants;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;
import com.hp.sdd.jabberwocky.xml.RestXMLWriter;

public class WebResourceWithTimeout {
    public String mUri;
    public Binding mBinding;
    public int mConnectionTimeout;
    public int mResponseTimeout;
    public NetworkCredentials mNetworkCredentials;

    public WebResourceWithTimeout(String mUri, Binding mBinding, int mConnectionTimeout, int mResponseTimeout,
            NetworkCredentials mNetworkCredentials) {
        this.mUri = mUri;
        this.mBinding = mBinding;
        this.mConnectionTimeout = mConnectionTimeout;
        this.mResponseTimeout = mResponseTimeout;
        this.mNetworkCredentials = mNetworkCredentials;
    }

    public WebResourceWithTimeout(RestXMLTagHandler tagHandler) throws Error {
        //noinspection unchecked
        mUri = (String)tagHandler.getTagData(Constants.XML_TAG__COMMON__URI);
        mBinding = Binding.fromAttributeValue((String) tagHandler.getTagData(Constants.XML_TAG__COMMON__BINDING));
        mConnectionTimeout = Integer.parseInt((String) tagHandler.getTagData(Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT));
        mResponseTimeout = Integer.parseInt((String) tagHandler.getTagData(Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT));
        mNetworkCredentials = (NetworkCredentials)(tagHandler.getTagData(OXPdDevice.Constants.XML_TAG__COMMON__NETWORK_CREDENTIALS));
    }

    /**
     * Configure the handler to process XML payload
     * @param tagHandler
     *              XML handler to extract data from
     */
    public static void setupXMLTagHandler(final RestXMLTagHandler tagHandler) {
        RestXMLTagHandler.XMLEndTagHandler stringFieldsHandler = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(Constants.XML_TAG__COMMON__URI, null, stringFieldsHandler);
        tagHandler.setXMLHandler(Constants.XML_TAG__COMMON__BINDING, null, stringFieldsHandler);
        tagHandler.setXMLHandler(Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT, null, stringFieldsHandler);
        tagHandler.setXMLHandler(Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT, null, stringFieldsHandler);

        NetworkCredentials.setupXMLTagHandler(tagHandler);
    }

    public static void writeToXML(WebResourceWithTimeout webResourceWithTimeout, RestXMLWriter xmlWriter) {
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__URI, null, "%s", webResourceWithTimeout.mUri);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__BINDING, null, "%s", webResourceWithTimeout.mBinding);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__CONNECTION_TIMEOUT, null, "%d", webResourceWithTimeout.mConnectionTimeout);
        xmlWriter.writeTag(OXPdDevice.Constants.XML_SCHEMA__OXPD_COMMON, OXPdDevice.Constants.XML_TAG__COMMON__RESPONSE_TIMEOUT, null, "%d", webResourceWithTimeout.mResponseTimeout);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[").append("uri=").append(mUri).append(", ").append("binding=").append(mBinding).append(", ").append("connectionTimeout=").append(mConnectionTimeout).append(", ").append("responseTimeout=").append(mResponseTimeout).append(", ").append("networkCredentials=").append(mNetworkCredentials).append("]").toString();
    }
}
