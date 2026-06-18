// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.hp.sdd.jabberwocky.utils.Chronicler;
import com.hp.sdd.jabberwocky.utils.FnDebugUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Rest XML Namespace handler. Used to store the namespace and prefix values
 * received from the device. The intent being that they can be retrieved later
 * on to be used when sending XML payloads back to the device. This allows the
 * client application to be flexible and avoid hard-coding specific schemas that
 * may get rejected by the device.
 */


@SuppressWarnings({"unused", "WeakerAccess"})
public final class RestXMLNSHandler {

	/**
	 * Android Log tag
	 */
	private static final String TAG = "XMLNSHandler";

	/**
	 * How closely is the Namespace match?
	 */
	private enum MatchType{
		/** Did not match */
		NO_MATCH,
		/** Exact match */
		EXACT_MATCH,
		/** Close enough match */
		PARTIAL_MATCH,
	} 

	/** Empty version string */
	private static final String EMPTY_VERSION = "";
	/** Bundle key to start namespace map */
    @SuppressWarnings("SpellCheckingInspection")
    private static final String BUNDLE_KEY__NSMAP_KEYS = "ns-map-keys";

	/** Namespace delimiter */
	public static final String XML_SEPARATOR = ",";

	/** HashMap to store discovered/configured namespaces */
	private HashMap<String, ArrayList<String>> xmlNSMap;
	/** Logger */
	private final Chronicler mChronicler;

	/** Debugging flag */
	private boolean mIsDebuggable;

    /**
     * Constructor and initializer for the XML Namespace Handler
     */
	public RestXMLNSHandler() {
		this(null);
	}

    /**
     * Constructor and initializer for the XML Namespace Handler
     * @param chronicler
     *              Logger to use
     */
	public RestXMLNSHandler(Chronicler chronicler) {
		this(chronicler, false);
	}

	/**
	 * Constructor and initializer for the XML Namespace Handler
     * @param chronicler
     *              Logger to use
     * @param debugEnabled
     *              Enable debugging for the namespace handler
	 */
	public RestXMLNSHandler(Chronicler chronicler, boolean debugEnabled) {
		// create the namespace hash map
		xmlNSMap = new HashMap<String, ArrayList<String>>();
        mIsDebuggable = debugEnabled;
		mChronicler = chronicler;
	}

    /**
     * Save the state of the namespace handler to a bundle
     * @return
     *          Bundle containing the current state of the namespace handler
     */
	public synchronized Bundle saveInstanceState() {
		Bundle state = new Bundle();
		Set<String> keySet = xmlNSMap.keySet();
		
		String[] keyArray = new String[keySet.size()];
		keySet.toArray(keyArray);
		state.putStringArray(BUNDLE_KEY__NSMAP_KEYS,keyArray);
		for (String aKeyArray : keyArray) {
			state.putStringArrayList(aKeyArray, xmlNSMap.get(aKeyArray));
		}
		return state;
	}

    /**
     * Restore the state of the namespace handler from the provided bundle
     * @param savedInstanceState
     *              Previously saved state of the namespace handler
     */
	public synchronized void restoreState(Bundle savedInstanceState) {
		if (savedInstanceState == null)
			return;
		String[] keyArray = savedInstanceState.getStringArray(BUNDLE_KEY__NSMAP_KEYS);
		if (keyArray == null)
			return;
		for (String aKeyArray : keyArray) {
			xmlNSMap.put(aKeyArray, savedInstanceState.getStringArrayList(aKeyArray));
		}
	}

	/**
	 * Method to extract version information out of an XML Namespace entry
	 * @param entry
	 * 				String value to parse for a possible version value
     * @param chronicler
     *              Logger to use
	 * @return String containing the version information we found in the entry
	 */
	private static String ns_get_version(String entry, Chronicler chronicler) {
		String version = EMPTY_VERSION;
		try {
			URI uri = new URI(entry);
			version = uri.getPath();
		} catch (URISyntaxException ignored) {
		}

		if (!TextUtils.isEmpty(version)) {
			// grab indices to the start and end of the string
			int start = 0;
			int end = version.length();

			// move past leading slashes
			while (version.charAt(start) == '/') {
				start++;
			}
			// don't include trailing slashes
			while (version.charAt(end - 1) == '/') {
				end--;
			}
			// split the reduced path into its various parts
			String[] parts = version.substring(start, end).split("/");

			// invalidate the indices
			start = end = -1;
			// check each part to see if it's a number sequence
			for (int partsIndex = 0; partsIndex < parts.length; partsIndex++) {
				boolean allNumbers = true;
				for (int index = 0; (allNumbers && (index < parts[partsIndex]
						.length())); index++) {
					char data = parts[partsIndex].charAt(index);
					// treat a '.' as a number since the version may be in the
					// format of 1.0
					// check if it's a number
					if ((data != '.') && !Character.isDigit(data)) {
						allNumbers = false;
					}
				}
				// not all numbers, move on
				if (!allNumbers) {
					continue;
				}
				// check if this is the first number sequence
				if (start == -1) {
					// first sequence, save indices
					start = partsIndex;
					end = partsIndex;
				}
				// is this a subsequent number sequence
				else if (partsIndex == (end + 1)) {
					// update end index
					end++;
				} else if (chronicler != null) {
					// there are multiple items that look like versions
					// need to figure out what to do, but first we need to find
					// an entry that falls in this category
					if (FnDebugUtils.mDebugEnabled) chronicler.log(Log.ERROR, TAG, "found multiple possible versions");
				}
			}

			// did we find anything?
			if (start >= 0) {
				// we found a sequence that looks like a version, so build it up
				StringBuilder builder = new StringBuilder();
				for (int index = start; index <= end; index++) {
					// add a '/' between the entries
					if (index != start) {
						builder.append('/');
					}
					// add the version sequence
					builder.append(parts[index]);
				}
				// produce the complete version string
				version = builder.toString();
			} else {
				// didn't find a version sequence
				version = EMPTY_VERSION;
			}

		}
		// return the version we found
		return version;
	}

	/**
	 * Determine if the specified entry match the provided version number
	 * @param version
	 * 				Version information we want to match against
	 * @param entry
	 * 				Entry being compared to the specified version
	 * @return true if the entry is of the specified version, false otherwise
	 */
	private MatchType ns_version_match(String version, String entry) {
		// sanity check
		if ((version == null) || (entry == null)) {
			return MatchType.NO_MATCH;
		}

		// get the version
		String entryVersion = ns_get_version(entry, this.mChronicler);

		// check that we got a version
		if (TextUtils.isEmpty(entryVersion)) {
			return MatchType.NO_MATCH;
		}

		// compare the values
		if (version.equalsIgnoreCase(entryVersion)) {
			return MatchType.EXACT_MATCH;
		} else if (entryVersion.startsWith(version)) {
			return MatchType.PARTIAL_MATCH;
		} else {
			return MatchType.NO_MATCH;
		}
	}

    /**
     * Check if the provided namespaces match
     * @param nsA First namespace to check
     * @param nsB Second namespace to check
     * @return
     *          true if namespaces match
     *          false otherwise
     */
	@SuppressWarnings("SpellCheckingInspection")
	public static boolean ns_unversioned_match(String nsA, String nsB)
	{
		// assume failure
		boolean result = false;
		String[] temp;
		
		do
		{
			// make sure the namespaces aren't empty
			if (TextUtils.isEmpty(nsA) || TextUtils.isEmpty(nsB))
				continue;

			// split into the various parts, get the namesapce
			temp = nsA.split(XML_SEPARATOR);
			if (temp.length < 2)
				continue;
			nsA = temp[1];
			
			// split into the various parts, get the namesapce
			temp = nsB.split(XML_SEPARATOR);
			if (temp.length < 2)
				continue;
			nsB = temp[1];
			
			// compare the namespaces
			result = nsA.equals(nsB);
		
		} while(false);
		
		// return the result
		return result;
	}

	/**
	 * Determine if the version appears in a date format
	 * @param version namespace version
	 * @return true if the version appears to be a date
	 */
	private boolean ns_is_dated_version(String version) {
		boolean dated = false;
		int numSlashes = 0;
		boolean allNumbers = true;
		do {
			// sanity check
			if (version == null) {
				continue;
			}

			// go through the version string
			for (int offset = 0; offset < version.length(); offset++) {
				// found a slash, add it to the count
				if (version.charAt(offset) == '/') {
					numSlashes++;
				}
				// check the character is a number
				else if (!Character.isDigit(version.charAt(offset))) {
					allNumbers = false;
				}
			}

			// if we found something that isn't a number then bail
			if (!allNumbers) {
				continue;
			}

			// did we find slashes between any of those numbers??
			if (numSlashes == 0) {
				continue;
			}

			// this must be a dated version string
			dated = true;

		} while (false);

		// return the result
		return dated;
	}

	/**
	 * Determine if an XML namespace is a newer 
	 * @param versionA current XML namespace version
	 * @param versionB XML namespace version to compare to
	 * @return true if versionB is newer than versionA, false otherwise
	 */
	private boolean ns_version_newer(String versionA, String versionB) {
		boolean newer = false;
		String verA, verB;
		boolean datedA, datedB;

		do {
			// sanity checks
			if ((versionA == null) && (versionB == null)) {
				continue;
			}

			// version A is null but B isn't, so B HAS to be newer
			if (versionA == null) {
				newer = true;
				continue;
			}

			// version B is null, but A isn't, so A is newer
			if (versionB == null) {
				continue;
			}

			// get the version substring from A and B
			verA = ns_get_version(versionA, this.mChronicler);
			verB = ns_get_version(versionB, this.mChronicler);

			// check if the versions are dated
			datedA = ns_is_dated_version(verA);
			datedB = ns_is_dated_version(verB);

			if (datedA == datedB) //noinspection SpellCheckingInspection
			{
				// sanity check
				newer = !((verA == null) || (verB == null)) && (verA.compareToIgnoreCase(verB) < 0);
				// both A and B are dated/versioned, we'll assume they are
				// in the same format and use strcmp to "sort" them
			} else if (datedB) {
				// since all new schemas use dated versions and B is dated but A
				// isn't, then B is newer
				newer = true;
			}
		} while (false);

		return newer;
	}

	/**
	 * Return the specified namespace from a namespace that has been returned by the device
	 * Value should be in "prefix,namespace,version" format
	 * @param nsRequest
	 *            Namespace to lookup
	 * @return Matching specified namespace as used by the printer
	 */
	synchronized String getXMLns(String nsRequest) {
		String ns, version;

		// assume we won't find anything
		String xmlns = "";
		do {
			// sanity check
			if (nsRequest == null) {
				continue;
			} else {
				// split up the request into its prefix, namespace, version
				// parts
				String[] temp = nsRequest.split(XML_SEPARATOR);

				// make sure we got at least the prefix and namespace
				if (temp.length < 2) {
					continue;
				}
				// grab the namespace
				ns = temp[1];
				// try to grab the version requested, otherwise we'll use null
				// which
				// implies we want to grab the latest version we've found so far
				if (temp.length > 2) {
					// if there appear to be a version grab it, or use null if
					// its empty
					version = (TextUtils.isEmpty(temp[2]) ? null : temp[2]);
				} else {
					// no version information, use use
					version = null;
				}
			}
			// lookup the requested namespace
			ArrayList<String> nsVersions = xmlNSMap.get(ns);
			// did we find anything for that namespace?
			if (nsVersions == null) {
				continue;
			}

			// go through the entries for the requested namespace, look for a
			// match
			for (String nsVersion : nsVersions) {
				String entry[] = nsVersion.split(XML_SEPARATOR);
				// no specific version was requested
				if (version == null) {
					// is this entry newer than what we've found so far?
					if (ns_version_newer(xmlns, entry[1])) {
						// yes it's newer, update our result
						xmlns = entry[1];
					}
				}
				// a specific version was requested, check if this entry matches
				else {
					MatchType matchType = ns_version_match(version, entry[1]);
					if (matchType == MatchType.EXACT_MATCH) {
						// found a version match to what was requested
						// update our result and bail
						xmlns = entry[1];
						break;
					} else if (matchType == MatchType.PARTIAL_MATCH) {
						if (ns_version_newer(xmlns, entry[1])) {
							// yes it's newer, update our result
							xmlns = entry[1];
						}
					}
				}
			}

		} while (false);
		// return whatever we've found
		return xmlns;
	}

    /**
     * Return the "unversioned" representation of the provided namespace
     * @param uri String revision for a namespace
     * @return
     *          String representation of provided namespace with version/dates replaced as '*'
     */
	@SuppressWarnings("SpellCheckingInspection")
	static String getUnversionedNamespace(String uri)
	{
		if (TextUtils.isEmpty(uri))
			return "";
		
		// if the uri ends in a slash remove it
		if (uri.charAt(uri.length() - 1) == '/') {
			uri = uri.substring(0, uri.length() - 1);
		}
		// get the version information from the uri
		String version = ns_get_version(uri, null);
		
		// Replace the version information with a '*' so that we can use
		// the version-less portion of the URI as a key into the hash map

		return (TextUtils.isEmpty(version) ? uri : uri.replaceAll(
				version, "*"));
	}

	/**
	 * Method used to store an XML Namespace, duplicates values are ignored
	 * @param prefix
	 *            XML Namespace prefix received from the device
	 * @param uri
	 *            XML Namespace uri received from the device
	 */
	@SuppressWarnings("SpellCheckingInspection")
	synchronized public void addXMLNS(String prefix, String uri) {
		// create the value we will store, this includes the namespace URI and
		// prefix used by the device
		// the entry will be stored as "prefix,URI" so that for debugging
		// purposes we can see all of the prefix-URI combinations we have
		// received from the device
		String namespaceEntry = String.format(Locale.US, "%s%s%s", prefix, XML_SEPARATOR,
				uri);

		// get the unversioned namespace
		String namespace = getUnversionedNamespace(uri);

		// check to see if we already have an entry in the hash map for the base
		// URI
		if (!xmlNSMap.containsKey(namespace)) {
			// the hash map didn't have an entry, let's go ahead and create one
			xmlNSMap.put(namespace, new ArrayList<String>());
		}
		// pull out a reference to the linked list so we can modify it
		ArrayList<String> list = xmlNSMap.get(namespace);

		// Store the new entry if it's not already on the list
		if ((list != null) && !list.contains(namespaceEntry)) {
			list.add(namespaceEntry);
		}
	}

	/**
	 * Method to print out debugging information
	 */
	public synchronized void debug() {
		if (xmlNSMap.size() == 0) {
			// hash map is empty
			if (mIsDebuggable) mChronicler.log(Log.DEBUG, TAG, "XMLNS map is empty");
		} else if (mChronicler != null) {
			mChronicler.lock();
			// Get all the entries in the hash map
			Set<Map.Entry<String, ArrayList<String>>> set = xmlNSMap
					.entrySet();
			
			if (mIsDebuggable) mChronicler.log(Log.DEBUG, TAG, String.format(Locale.US, "XMLNS map has %d entries",
					set.size()));

			// Go through each entry in the hash map
			for (Map.Entry<String, ArrayList<String>> entry : set) {
				// print out the namespace base URI information
				String namespace = entry.getKey();
				if (mIsDebuggable) mChronicler.log(Log.DEBUG, TAG, String.format(Locale.US,
						"\tReceived URIs for namespace: %s\n", namespace));

				// print out the all the prefix and URIs received by the printer
				ArrayList<String> list = entry.getValue();
				for (String aList : list) {
					if (mIsDebuggable)
						mChronicler.log(Log.DEBUG, TAG, String.format(Locale.US, "\t\t%s", aList));
				}
			}
			mChronicler.unlock();
		}
	}
}
