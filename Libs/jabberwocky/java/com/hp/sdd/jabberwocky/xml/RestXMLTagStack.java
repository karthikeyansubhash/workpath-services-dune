// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import android.os.Build;
import android.util.Pair;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * XML Tag stack that is used during XML writing
 */
@SuppressWarnings("unused")
public final class RestXMLTagStack {
    /**
     * Current tag stack
     */
    private final ArrayList<Pair<String,String>> tagStack;
    /**
     * Is the stack used for parsing or writing?
     */
    private final boolean forWriting;

    /**
     * Default constructor
     */
    private RestXMLTagStack(boolean forWriting)
    {
        // create the tag list
        this.forWriting = forWriting;
        this.tagStack = new ArrayList<Pair<String,String>>();
    }

    /**
     * Create RestXMLTagStack instance to use during XML writing
     * @return
     *              RestXMLTagStack instance
     */
    static RestXMLTagStack createStackForWriting() {
        return new RestXMLTagStack(true);
    }

    /**
     * Create RestXMLTagStack instance to use during XML parsing
     * @return
     *              RestXMLTagStack instance
     */
    static RestXMLTagStack createStackForParsing() {
        return new RestXMLTagStack(false);
    }

    /**
     * Add an entry into the XML  tag stack
     * @param prefix
     * 				XML tag prefix
     * @param tag
     * 				XML tag
     */
    void push(String prefix, String tag)
    {
        if (forWriting) {
            // push an entry to the beginning of hte list
            tagStack.add(0, Pair.create(prefix, tag));
        } else {
            // create a namespace entry in the prefix,ns,version format
            String nsEntry = String.format(Locale.US, ",%s,*", RestXMLNSHandler.getUnversionedNamespace(prefix));
            // add the entry
            tagStack.add(0, Pair.create(nsEntry, tag));
        }
    }

    /**
     * Remove an entry out of the XML tag stack, verified the entry matches the specified values
     * @param prefix
     * 				XML tag prefix
     * @param tag
     * 				XML tag
     * @throws IllegalStateException
     *              if stack is empty or the wrong tag is being popped
     */
    void pop(String prefix, String tag) throws IllegalStateException
    {
        // create an entry we will use to compare
        Pair<String, String> entry = Pair.create(prefix, tag);
        // check the stack isn't empty
        if (tagStack.isEmpty())
            throw new IllegalStateException("XML Tag Stack is already empty");
        // remove the top entry
        Pair<String, String> currentEntry = tagStack.remove(0);

        // check that they match note ava/android problem related to accessing pair members in < 4.2 when a pair member is null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!currentEntry.equals(entry))
                throw new IllegalStateException("Closing incorrect XML Tag");
        } else if (!((TextUtils.equals(currentEntry.first, entry.first)) &&
                    (TextUtils.equals(currentEntry.second, entry.second))))
        {
            throw new IllegalStateException("Closing incorrect XML Tag");
        }
    }

    /**
     * Get the depth of the XML tag stack
     * @return
     *              the XML tag stack depth
     */
    int getDepth()
    {
        // return the depth
        return tagStack.size();
    }

    /**
     * Check if the stack is empty
     * @return
     *              true if the stack is empty
     *              false otherwise
     */
    boolean isEmpty() {
        return tagStack.isEmpty();
    }

    /**
     * Pop an entry out of the XML Tag Stack
     * @throws IllegalStateException
     *              if used on instance used for XML writing
     */
    void pop() throws IllegalStateException {
        if (forWriting) {
            throw new IllegalStateException("parameter-less pop() cannot be used during XML writing");
        }
        // if the list is not empty, remove the first entry
        if (!tagStack.isEmpty())
            tagStack.remove(0);
    }

    /**
     * Clear the XML Tag Stack of all entries
     * @throws IllegalStateException
     *              if used on instance used for XML writing
     */
    void clear() throws IllegalStateException
    {
        if (forWriting) {
            throw new IllegalStateException("clear() cannot be used during XML writing");
        }
        // clear out the xml tag stack
        tagStack.clear();
    }

    /**
     * Check to see whether the specified tag is in the stack
     * @param ns
     * 			    Namespace of the XML element tag to search on, may be null if
     *			    you only want to check the tag names and not their namespaces
     * @param tag
     * 			    XML element tag to search for
     * @return
     *              true if tag is found
     *              false otherwise
     */
    public boolean isTagInStack(String ns, String tag)
    {
        // assume failure
        boolean result = false;
        // sanity check
        if (TextUtils.isEmpty(tag))
            return false;
        // caller provided a namespace, pull out the un-versioned string
        if (!TextUtils.isEmpty(ns))
        {
            // split prefix,namespace,version string into its various parts
            String[] temp = ns.split(RestXMLNSHandler.XML_SEPARATOR);
            // was the namespace in the correct format
            if (temp.length < 2)
                // wrong format, return false since we'll never find a match
                return false;
            else
                // found the un-versioned namespace
                ns = temp[1];
        }
        else
        {
            // nothing to check
            ns = null;
        }

        // go through the tag stack
        for (Pair<String, String> entry : tagStack) {
            // get the entry
            // compare the tags
            result = tag.equals(entry.second);
            // did the caller want to compare namespace as well
            if (result && (ns != null)) {
                // pull out the un-versioned namespace from the stack entry
                String[] temp = entry.first.split(RestXMLNSHandler.XML_SEPARATOR);
                // compare them
                result = ns.equals(temp[1]);
            }
            if (result) break;
        }
        // return the result
        return result;
    }
}
