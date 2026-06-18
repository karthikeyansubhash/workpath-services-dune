// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.chat;

import android.text.TextUtils;
import android.util.Pair;

import java.security.InvalidParameterException;
import java.util.Locale;

/**
 * HTTP Header object representation
 */
public final class HttpHeader extends Pair<String, String> {
    /**
     * Create a devcom http header
     * @param name
     *              Header name
     * @param value
     *              Header value
     * @throws InvalidParameterException
     *              if name or value are null or empty
     */
    private HttpHeader(String name, String value) throws InvalidParameterException {
        super(name, value);
        if (TextUtils.isEmpty(name)) throw new InvalidParameterException("invalid name");
        if (value == null) throw new InvalidParameterException("invalid value");
    }

    /**
     * Get the header name
     * @return
     *              header name
     */
    public final String getName() {
        return first;
    }

    /**
     * Get the header value
     * @return
     *              header value
     */
    public final String getValue() {
        return second;
    }

    /**
     * Returns a human-readable representation of the http header
     * @return
     *              a printable representation of this object
     */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s=%s", getName(), getValue());
    }

    /**
     * Returns an integer hash code for this object
     * @return
     *              this object's hash code.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getName().hashCode();
        result = prime * result + getValue().hashCode();
        return result;
    }

    /**
     * Compares this instance with the specified object and indicates if they are equal.
     * @param other
     *              Other object to compare against
     * @return
     *              true if the specified object is equal to this Object;
     *              false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof HttpHeader)) return false;
        if (this == other) return true;
        HttpHeader otherHeader = (HttpHeader)other;
        return (TextUtils.equals(this.getName(), otherHeader.getName())
                    && TextUtils.equals(this.getValue(), otherHeader.getValue()));
    }

    /**
     * Create an HttpHeader
     * @param name
     *              header name
     * @param value
     *              header value
     * @return
     *              HttpHeader object
     */
    public static HttpHeader create(String name, String value) {
        return new HttpHeader(name, value);
    }
}
