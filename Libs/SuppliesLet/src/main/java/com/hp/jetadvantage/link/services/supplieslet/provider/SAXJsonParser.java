// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.supplieslet.provider;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXJsonParser extends DefaultHandler {

    static final String TEXTKEY = "_text";
    private static final String TAG = "SAXJsonParser";

    JSONObject result;
    List<JSONObject> stack;

    private boolean isKeySet = false;

    public SAXJsonParser() {
    }

    public JSONObject getJson() {
        return result;
    }

    public String attributeName(String name) {
        return "@" + name;
    }

    public void startDocument() throws SAXException {
        stack = new ArrayList<JSONObject>();
        stack.add(0, new JSONObject());
    }

    public void endDocument() throws SAXException {
        result = stack.remove(0);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        JSONObject work = new JSONObject();

        try {
            isKeySet = true;
            for (int ix = 0; ix < attributes.getLength(); ix++) {
                work.put(attributeName(attributes.getLocalName(ix)), attributes.getValue(ix));
            }
            stack.add(0, work);
        } catch (Throwable throwable) {
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        JSONObject pop = stack.remove(0);       // examine stack
        Object stashable = pop;

        try {
            if (isKeySet) {
                isKeySet = false;
            }

            if (pop.has(TEXTKEY)) {
                String value = pop.getString(TEXTKEY).trim();
                if (pop.length() == 1) stashable = value; // single value
                else if (TextUtils.isEmpty(value)) pop.remove(TEXTKEY);
            }
            JSONObject parent = stack.get(0);

            if (pop != null && pop.length() == 0) {
                stashable = "";
            }
            if (!parent.has(localName)) {   // add new object
                parent.put(localName, stashable);
            } else {                                  // aggregate into arrays
                Object work = parent.get(localName);
                if (work instanceof JSONArray) {
                    ((JSONArray) work).put(stashable);
                } else {
                    parent.put(localName, new JSONArray());
                    parent.getJSONArray(localName).put(work);
                    parent.getJSONArray(localName).put(stashable);
                }
            }
        } catch (Throwable throwable) {
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (!isKeySet)
            return;

        isKeySet = false;

        JSONObject work = stack.get(0);            // aggregate characters

        try {
            String value = (work.has(TEXTKEY) ? work.getString(TEXTKEY) : "");
            work.put(TEXTKEY, value + new String(ch, start, length));
        } catch (Throwable throwable) {
        }
    }

    public void warning(SAXParseException e) throws SAXException {
        Log.e(TAG, "warning  e=" + e.getMessage());
    }

    public void error(SAXParseException e) throws SAXException {
        Log.e(TAG, "error  e=" + e.getMessage());
    }

    public void fatalError(SAXParseException e) throws SAXException {
        Log.e(TAG, "fatalError  e=" + e.getMessage());
        throw e;
    }
}
