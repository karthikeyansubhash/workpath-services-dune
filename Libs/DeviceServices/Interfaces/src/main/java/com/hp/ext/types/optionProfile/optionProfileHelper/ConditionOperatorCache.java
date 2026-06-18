package com.hp.ext.types.optionProfile.optionProfileHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionOperatorCache {
    private final Map<Integer, Object> _operatorCache = new HashMap<Integer, Object>();
    Lock lock = new ReentrantLock();

    public void addOperatorToCache(int conditionOperatorKey, Object specializedOperatorInstance) {
        lock.lock();
        _operatorCache.put(conditionOperatorKey, specializedOperatorInstance);
        lock.unlock();
    }

    public Object getOperatorFromCache(int conditionOperatorKey) {
        lock.lock();
        Object specializedOperatorInstance = null;

        if (_operatorCache.containsKey(conditionOperatorKey)) {
            specializedOperatorInstance = _operatorCache.get(conditionOperatorKey);
        }
        lock.unlock();

        return specializedOperatorInstance;

    }

    public void clearOperatorCached() {
        lock.lock();
        _operatorCache.clear();
        lock.unlock();
    }
}
