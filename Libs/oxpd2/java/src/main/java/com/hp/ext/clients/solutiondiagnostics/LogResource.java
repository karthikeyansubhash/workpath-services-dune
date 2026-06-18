/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutiondiagnostics;

import java.util.AbstractMap.SimpleEntry;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionDiagnostics.Log;

public interface LogResource extends ReadableResource<SimpleEntry<Log, byte[]>> {
}
