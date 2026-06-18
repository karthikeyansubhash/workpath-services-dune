package com.hp.jetadvantage.link.services.printlet.model.ipp.util;

import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.ErrorName;

public class IppErrorGenerator {
    public static void attributeNotFoundError(String attributeName) throws Error {
        throw new Error(ErrorName.Unknown, "Attribute " + attributeName + " not found");
    }
}
