package com.safetynet.alertsystem.service.alerts;

public interface Alert<T> {

    T generateAlert(String parameter);

}
