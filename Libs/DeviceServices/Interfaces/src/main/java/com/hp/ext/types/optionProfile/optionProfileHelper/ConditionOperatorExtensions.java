package com.hp.ext.types.optionProfile.optionProfileHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.types.optionProfile.AndOperator;
import com.hp.ext.types.optionProfile.ConditionOperator;
import com.hp.ext.types.optionProfile.EqualsOperator;
import com.hp.ext.types.optionProfile.GreaterThanOperator;
import com.hp.ext.types.optionProfile.LessThanOperator;
import com.hp.ext.types.optionProfile.NotEqualsOperator;
import com.hp.ext.types.optionProfile.NotOperator;
import com.hp.ext.types.optionProfile.OrOperator;

public class ConditionOperatorExtensions {
    // Construct static instances of each specialized operator type so that we can
    // convienently access their specific TypeGUN during string compares.
    private static final EqualsOperator _equals = new EqualsOperator();
    private static final NotEqualsOperator _notEquals = new NotEqualsOperator();
    private static final GreaterThanOperator _greaterThan = new GreaterThanOperator();
    private static final LessThanOperator _lessThan = new LessThanOperator();
    private static final OrOperator _or = new OrOperator();
    private static final AndOperator _and = new AndOperator();
    private static final NotOperator _not = new NotOperator();

    public static Boolean isEqualsOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_equals.getTypeGUN());
    }

    public static EqualsOperator asEqualsOperator(ConditionOperator conditionOperator, ConditionOperatorCache cache) {
        EqualsOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_equals.getTypeGUN())) {
            try {
                op = (EqualsOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), EqualsOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }

    public static Boolean isNotEqualsOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_notEquals.getTypeGUN());
    }

    public static NotEqualsOperator asNotEqualsOperator(ConditionOperator conditionOperator,
            ConditionOperatorCache cache) {
        NotEqualsOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_notEquals.getTypeGUN())) {
            try {
                op = (NotEqualsOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), NotEqualsOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }

    public static Boolean isGreaterThanOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_greaterThan.getTypeGUN());
    }

    public static GreaterThanOperator asGreaterThanOperator(ConditionOperator conditionOperator,
            ConditionOperatorCache cache) {
        GreaterThanOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_greaterThan.getTypeGUN())) {
            try {
                op = (GreaterThanOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), GreaterThanOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }

    public static Boolean isLessThanOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_lessThan.getTypeGUN());
    }

    public static LessThanOperator asLessThanOperator(ConditionOperator conditionOperator,
            ConditionOperatorCache cache) {
        LessThanOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_lessThan.getTypeGUN())) {
            try {
                op = (LessThanOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), LessThanOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }

    public static Boolean isOrOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_or.getTypeGUN());
    }

    public static OrOperator asOrOperator(ConditionOperator conditionOperator, ConditionOperatorCache cache) {
        OrOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_or.getTypeGUN())) {
            try {
                op = (OrOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), OrOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }

    public static Boolean isAndOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_and.getTypeGUN());
    }

    public static AndOperator asAndOperator(ConditionOperator conditionOperator, ConditionOperatorCache cache) {
        AndOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_and.getTypeGUN())) {
            try {
                op = (AndOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), AndOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }

    public static Boolean isNotOperator(ConditionOperator conditionOperator) {
        return conditionOperator.getTypeGUN().equals(_not.getTypeGUN());
    }

    public static NotOperator asNotOperator(ConditionOperator conditionOperator, ConditionOperatorCache cache) {
        NotOperator op = null;

        if (conditionOperator.getTypeGUN().equals(_not.getTypeGUN())) {
            try {
                op = (NotOperator) cache.getOperatorFromCache(conditionOperator.hashCode());

                if (null == op) {
                    ObjectMapper mapper = new ObjectMapper();
                    op = mapper.convertValue(conditionOperator.getValue(), NotOperator.class);
                    cache.addOperatorToCache(conditionOperator.hashCode(), op);
                }
            } catch (Exception e) {
                // Eat...
            }
        }

        return op;
    }
    
}
