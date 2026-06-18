package com.hp.ext.clients.application;

import com.hp.ext.clients.ModifiableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.application.Homescreen;
import com.hp.ext.service.application.Homescreen_Modify;

public interface HomescreenResource extends ReadableResource<Homescreen>, ModifiableResource<Homescreen, Homescreen_Modify> {

}
