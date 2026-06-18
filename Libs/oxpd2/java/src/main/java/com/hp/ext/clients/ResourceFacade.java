/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients;

import java.net.URI;

public interface ResourceFacade {
    String getPath();
    URI getServiceUri();
}
