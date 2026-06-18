// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.xml;

public class RestXMLEscape {
    /**
     * Represents the XML escape sequence for the less than sign.
     */
    private static final String LESS = "&lt;";

    /**
     * Represents the XML escape sequence for the greater than sign.
     */
    private static final String GREATER = "&gt;";

    /**
     * Represents the XML escape sequence for the double quote.
     */
    private static final String DOUBLE = "&quot;";

    /**
     * Represents the XML escape sequence for the single quote.
     */
    private static final String SINGLE = "&apos;";

    /**
     * Represents the XML escape sequence for the ampersand sign.
     */
    private static final String AND = "&amp;";

    /**
     * This is used to write the specified value to the output with
     * translation to any symbol characters or non text characters.
     * This will translate the symbol characters such as "&amp;",
     * "&gt;", "&lt;", and "&quot;". This also writes any non text
     * and non symbol characters as integer values like "&#123;".
     *
     * @param value the text value to be escaped and written
     */
    public static String escape(String value) {
        StringBuilder escaped = new StringBuilder();

        for(int i = 0; i < value.length(); i++){
            char originalChar = value.charAt(i);
            String escapedChar = symbol(originalChar);
            if (escapedChar != null)
                escaped.append(escapedChar);
            else
                escaped.append(originalChar);
        }
        return escaped.toString();
    }

    /**
     * This is used to convert the specified character to an XML text
     * symbol if the specified character can be converted. If the
     * character cannot be converted to a symbol null is returned.
     *
     * @param ch this is the character that is to be converted
     *
     * @return this is the symbol character that has been resolved
     */
    private static String symbol(char ch) {
        switch(ch) {
            case '<':
                return LESS;
            case '>':
                return GREATER;
            case '"':
                return DOUBLE;
            case '\'':
                return SINGLE;
            case '&':
                return AND;
        }
        return null;
    }
}
