// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.options.And;
import com.hp.oxpdlib.options.Condition;
import com.hp.oxpdlib.options.ConditionOperator;
import com.hp.oxpdlib.options.Disable;
import com.hp.oxpdlib.options.Equals;
import com.hp.oxpdlib.options.ForceSet;
import com.hp.oxpdlib.options.InvalidValues;
import com.hp.oxpdlib.options.GreaterThan;
import com.hp.oxpdlib.options.LessThan;
import com.hp.oxpdlib.options.NotEquals;
import com.hp.oxpdlib.options.OptionDefinition;
import com.hp.oxpdlib.options.OptionName;
import com.hp.oxpdlib.options.OptionRule;
import com.hp.oxpdlib.options.Or;
import com.hp.oxpdlib.options.Range;
import com.hp.oxpdlib.options.ValidValues;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import org.xml.sax.Attributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Device Copy Options profile
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CopyOptionsProfile implements Parcelable {
    public final List<OptionDefinition> optionDefinition;

    /**
     * Constructor used by the library to construct CopyOptionsProfile objects.
     *
     * @param tagHandler XML handler to extract data from
     */
    @SuppressWarnings("unchecked")
    private CopyOptionsProfile(RestXMLTagHandler tagHandler) throws Error {
        OXPdCopy.faultExceptionCheck(tagHandler);

        this.optionDefinition = Collections.unmodifiableList((List<OptionDefinition>) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITIONS, Collections.EMPTY_LIST));

    }

    /**
     * Builds a CopyOptionsProfile instance from the provided HTTP request/response
     * @param device
     *              OXPd device instance
     * @param requestResponse
     *              HTTP request/response pair
     * @param tagHandler
     *              XML tag handler
     * @return
     *              CopyOptionsProfile instance
     * @throws Error
     *              When errors are detected
     */
    @SuppressWarnings("unchecked")
    static CopyOptionsProfile parseRequestResult(OXPdDevice device, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        RestXMLTagHandler.XMLStartTagHandler listCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITIONS, localName)) {
                    handler.setTagData(localName, new ArrayList<OptionDefinition>());
                    handler.setGenericXMLHandler(
                            new RestXMLTagHandler.XMLStartTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                                    if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITION, localName)) {
                                        handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES, new ArrayList<OptionRule>());
                                    }
                                }
                            },
                            new RestXMLTagHandler.XMLEndTagHandler() {
                                @Override
                                public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                                    if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITIONS, localName)) {
                                        handler.setGenericXMLHandler(null, null);
                                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITION, localName)) {
                                        Stack<String> optionNames = (Stack<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);

                                        OptionName optionName = OptionName.fromAttributeValue(optionNames.pop());
                                        Boolean isAvailable = (Boolean) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__IS_AVAILABLE);
                                        List<OptionRule> optionRules = (List<OptionRule>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES);

                                        OptionDefinition optionDefinition = new OptionDefinition(optionName, isAvailable, optionRules.isEmpty() ? null : optionRules);
                                        ((List<OptionDefinition>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITIONS)).add(optionDefinition);

                                        handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);
                                        handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__IS_AVAILABLE);
                                        handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES);
                                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME, localName)) {
                                        Stack<String> optionNames = (Stack<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);
                                        if (optionNames == null) {
                                            optionNames = new Stack<>();
                                            handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME, optionNames);
                                        }
                                        optionNames.push(data);
                                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE, localName)) {
                                        handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE, data);
                                    } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__IS_AVAILABLE, localName)) {
                                        handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__IS_AVAILABLE, Boolean.valueOf(data));
                                    }
                                }
                            }
                    );
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler equalsCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Stack<String> optionNames = (Stack<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);
                String value = (String) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE);

                Equals equals = new Equals(optionNames.pop(), value);
                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_EQUALS, equals);
                if (handler.containsTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)) {
                    ((List<ConditionOperator>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)).add(
                            equals);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler notEqualsCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Stack<String> optionNames = (Stack<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);
                String value = (String) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE);

                NotEquals notEquals = new NotEquals(optionNames.pop(), value);
                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_NOT_EQUALS, notEquals);
                if (handler.containsTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)) {
                    ((List<ConditionOperator>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)).add(
                            notEquals);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler lessThanCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Stack<String> optionNames = (Stack<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);
                BigDecimal value =  new BigDecimal((String) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE));

                LessThan lessThan = new LessThan(optionNames.pop(), value);
                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_LESS_THAN, lessThan);
                if (handler.containsTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)) {
                    ((List<ConditionOperator>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)).add(lessThan);
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler greaterThanCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Stack<String> optionNames = (Stack<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_NAME);
                BigDecimal value =  new BigDecimal((String) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE));

                GreaterThan greaterThan = new GreaterThan(optionNames.pop(), value);
                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_GREATER_THAN, greaterThan);
                if (handler.containsTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)) {
                    ((List<ConditionOperator>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS)).add(greaterThan);
                }
            }
        };

        RestXMLTagHandler.XMLStartTagHandler rangeStartCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__RANGE_LOWER_BOUND, null, new RestXMLTagHandler.XMLEndTagHandler() {
                    @Override
                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                        handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__RANGE_LOWER_BOUND, new BigDecimal(data));
                    }
                });

                handler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__RANGE_UPPER_BOUND, null, new RestXMLTagHandler.XMLEndTagHandler() {
                    @Override
                    public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                        handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__RANGE_UPPER_BOUND, new BigDecimal(data));
                    }
                });
            }
        };

        RestXMLTagHandler.XMLEndTagHandler rangeEndCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Condition condition = (Condition) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION);
                BigDecimal lowerBound = (BigDecimal) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__RANGE_LOWER_BOUND);
                BigDecimal upperBound = (BigDecimal) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__RANGE_UPPER_BOUND);

                ((List<OptionRule>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES)).add(new Range(condition, lowerBound, upperBound));

            }
        };

        RestXMLTagHandler.XMLStartTagHandler orAndStartCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS, new ArrayList<ConditionOperator>());
            }
        };

        RestXMLTagHandler.XMLEndTagHandler orAndEndCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                List<ConditionOperator> conditionOperators = (List<ConditionOperator>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS);

                if (OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_OR.equals(localName)) {
                    handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_OR, new Or(conditionOperators));
                } else if (OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_AND.equals(localName)) {
                    handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_AND, new And(conditionOperators));
                }

                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__CONDITION_OPERATORS);
            }
        };

        RestXMLTagHandler.XMLStartTagHandler validValuesStartCreator = new RestXMLTagHandler.XMLStartTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, Attributes attributes) {
                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUES, new ArrayList<String>());
                handler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUES, null, new RestXMLTagHandler.XMLEndTagHandler() {
                        @Override
                        public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                            if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUES, localName)) {
                                ((List<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUES)).add(data);
                            }
                        }
                    }
                );
            }
        };

        RestXMLTagHandler.XMLEndTagHandler conditionEndCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                And and = (And) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_AND);
                Or or = (Or) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_OR);
                Equals equals = (Equals) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_EQUALS);
                NotEquals notEquals = (NotEquals) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_NOT_EQUALS);
                LessThan lessThan = (LessThan) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_LESS_THAN);
                GreaterThan greaterThan = (GreaterThan) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_GREATER_THAN);

                Condition condition = (and != null || or != null || equals != null || notEquals != null || lessThan != null || greaterThan != null) ?
                        new Condition(and, or, equals, notEquals, lessThan, greaterThan) : null;

                handler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION, condition);

                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_OR);
                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_AND);
                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_LESS_THAN);
                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_GREATER_THAN);
                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_EQUALS);
                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_NOT_EQUALS);
            }
        };

        RestXMLTagHandler.XMLEndTagHandler optionRuleCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                Condition condition = (Condition) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION);
                List<String> optionValues = (List<String>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUES);

                if (OXPdCopy.Constants.XML_TAG__COPY__VALID_VALUES.equals(localName)) {
                    ((List<OptionRule>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES)).add(new ValidValues(condition, optionValues));
                } else if (OXPdCopy.Constants.XML_TAG__COPY__INVALID_VALUES.equals(localName)) {
                    ((List<OptionRule>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES)).add(new InvalidValues(condition, optionValues));
                } else if (OXPdCopy.Constants.XML_TAG__COPY__DISABLE.equals(localName)) {
                    ((List<OptionRule>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES)).add(new Disable(condition));
                } else if (OXPdCopy.Constants.XML_TAG__COPY__FORCESET.equals(localName)) {
                    String value = (String) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUE);
                    ((List<OptionRule>) handler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTIONS_RULES)).add(new ForceSet(condition, value));
                }

                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION);
                handler.clearTagData(OXPdCopy.Constants.XML_TAG__COPY__OPTION_VALUES);
            }
        };

        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_DEFINITIONS, listCreator, null);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_EQUALS, null, equalsCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_NOT_EQUALS, null, notEqualsCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_LESS_THAN, null, lessThanCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_GREATER_THAN, null, greaterThanCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION, null, conditionEndCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__RANGE, rangeStartCreator, rangeEndCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_OR, orAndStartCreator, orAndEndCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OPTION_CONDITION_AND, orAndStartCreator, orAndEndCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__VALID_VALUES, validValuesStartCreator, optionRuleCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__INVALID_VALUES, validValuesStartCreator, optionRuleCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__DISABLE, null, optionRuleCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__FORCESET, null, optionRuleCreator);

        device.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);

        return new CopyOptionsProfile(tagHandler);
    }

    protected CopyOptionsProfile(Parcel in) {
        optionDefinition = in.createTypedArrayList(OptionDefinition.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(optionDefinition);
    }

    public static final Creator<CopyOptionsProfile> CREATOR = new Creator<CopyOptionsProfile>() {
        @Override
        public CopyOptionsProfile createFromParcel(Parcel in) {
            return new CopyOptionsProfile(in);
        }

        @Override
        public CopyOptionsProfile[] newArray(int size) {
            return new CopyOptionsProfile[size];
        }
    };
}