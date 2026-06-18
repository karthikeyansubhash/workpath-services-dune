package com.hp.ext.service.copy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hp.ext.types.optionProfile.OptionDefinition;
import com.hp.ext.types.optionProfile.OptionProfile;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelperException;

public class CopyTicketHelper {

    private OptionProfileHelper<CopyOptions> baseCopyOptionsProfileHelper;

    public OptionProfileHelper<CopyOptions> getBaseCopyOptionsProfileHelper() {
        return baseCopyOptionsProfileHelper;
    }

    private CopyOptions baseCopyOptions = new CopyOptions();

    public CopyOptions getBaseCopyOptions() {
        return baseCopyOptions;
    }

    public void setBaseCopyOptions(CopyOptions copyOptions) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, OptionProfileHelperException {
        this.baseCopyOptions = copyOptions;
        getBaseCopyOptionsProfileHelper().setOptionsInstance(copyOptions);
    }

    private OptionProfileHelper<CopyOptions> storedCopyOptionsProfileHelper;

    public OptionProfileHelper<CopyOptions> getStoredCopyOptionsProfileHelper() {
        return storedCopyOptionsProfileHelper;
    }

    private CopyOptions storedCopyOptions = new CopyOptions();

    public CopyOptions getStoredCopyOptions() {
        return storedCopyOptions;
    }

    public void setStoredCopyOptions(CopyOptions copyOptions) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, OptionProfileHelperException {
        this.storedCopyOptions = copyOptions;
        getStoredCopyOptionsProfileHelper().setOptionsInstance(copyOptions);
    }

    /**
     * Create a CopyTicketHelper for use with copy jobs
     *
     * @param baseCopyOptionProfile The base copy-options OptionProfile
     */
    public CopyTicketHelper(OptionProfile baseCopyOptionProfile)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        initialize(baseCopyOptionProfile, null);
    }

    /**
     * Create a CopyTicketHelper for use with stored copy jobs
     *
     * @param baseCopyOptionProfile The base copy-options OptionProfile
     * @param storedCopyOptionProfile The stored copy-options OptionProfile
     */
    public CopyTicketHelper(OptionProfile baseCopyOptionProfile, OptionProfile storedCopyOptionProfile)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        initialize(baseCopyOptionProfile, storedCopyOptionProfile);
    }

    private void initialize(OptionProfile baseCopyOptionProfile, OptionProfile storedCopyOptionProfile)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        var baseCopyDefinitions = (null == baseCopyOptionProfile ? null : baseCopyOptionProfile.getDefinitions());
        baseCopyDefinitions = (baseCopyDefinitions != null) ? baseCopyDefinitions : new ArrayList<OptionDefinition>();
        var storedCopyDefinitions = (null == storedCopyOptionProfile ? null : storedCopyOptionProfile.getDefinitions());

        Map<String, OptionDefinition> mergedMap = baseCopyDefinitions.stream()
                .collect(Collectors.toMap(entry -> entry.getOptionName().toLowerCase(), entry -> entry));

        baseCopyOptionsProfileHelper = new OptionProfileHelper<CopyOptions>(baseCopyOptions,
                mergedMap, CopyOptions.class);

        var mergedStoredCopyDefinitions = MergeOptionDefinitions(baseCopyDefinitions, storedCopyDefinitions);
        storedCopyOptionsProfileHelper = new OptionProfileHelper<CopyOptions>(storedCopyOptions,
                mergedStoredCopyDefinitions, CopyOptions.class);
    }

    private Map<String, OptionDefinition> MergeOptionDefinitions(List<OptionDefinition> baseDefinitions,
            List<OptionDefinition> overrideDefinitions) {
        baseDefinitions = (baseDefinitions != null) ? baseDefinitions : new ArrayList<OptionDefinition>();
        overrideDefinitions = (overrideDefinitions != null) ? overrideDefinitions : new ArrayList<OptionDefinition>();

        Map<String, OptionDefinition> mergedMap = baseDefinitions.stream()
                .collect(Collectors.toMap(entry -> entry.getOptionName().toLowerCase(), entry -> entry));

        for (OptionDefinition definition : overrideDefinitions) {
            String normalizedOptionName = definition.getOptionName().toLowerCase();

            if (mergedMap.containsKey(normalizedOptionName)) {
                mergedMap.put(normalizedOptionName, definition);
            } else {
                mergedMap.putIfAbsent(normalizedOptionName, definition);
            }
        }

        return mergedMap;
    }

    /**
     * Checks if a copy ticket is valid
     *
     * @param ticket The copy ticket to validate
     */
    public Boolean isTicketValid(CopyJobTicket ticket) throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Boolean result = true;

        if (null == ticket || null == ticket.getCopyOptions()) {
            return false;
        } else {
            // Evaluate the options-instance
            result = baseCopyOptionsProfileHelper.isValid(ticket.getCopyOptions());
        }

        return result;
    }

    /**
     * Checks if a copy ticket is valid
     *
     * @param ticket The copy ticket to validate
     */
    public Boolean isValidStoredJob(CopyJobTicket ticket) throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        Boolean result = true;

        if (null == ticket || null == ticket.getCopyOptions()) {
            return false;
        } else {
            // Evaluate the storedCopy options-instance
            result = storedCopyOptionsProfileHelper.isValid(ticket.getCopyOptions());
        }

        return result;
    }
}
