package com.car.appstore.core.updateengine;

public enum UpdateEvent {
    ENQUEUE,
    START,
    DOWNLOAD_COMPLETE,
    VERIFY_PASS,
    VERIFY_FAIL,
    INSTALL_SUCCESS,
    INSTALL_FAIL,
    PAUSE,
    RESUME,
    CANCEL,
    RETRY
}
