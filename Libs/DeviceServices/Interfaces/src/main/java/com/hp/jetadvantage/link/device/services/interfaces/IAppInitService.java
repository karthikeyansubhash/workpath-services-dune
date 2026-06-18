package com.hp.jetadvantage.link.device.services.interfaces;

import androidx.lifecycle.Observer;

public interface IAppInitService {
    public void emitAppInitEvent(String packageName);
    public void registerObserver(Observer<String> observer);
}
