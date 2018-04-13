package com.dit.des.arproject.util;

import com.dit.des.arproject.BaseArActivity;

import static com.wikitude.common.camera.CameraSettings.*;

public class DefaultConfigs {

    private DefaultConfigs() {}

    // Defaults configuration for samples
    public static final Class DEFAULT_ACTIVITY = BaseArActivity.class;
    public static final CameraPosition DEFAULT_CAMERA_POSITION = CameraPosition.DEFAULT;
    public static final CameraResolution DEFAULT_CAMERA_RESOLUTION = CameraResolution.SD_640x480;
    public static final CameraFocusMode DEFAULT_CAMERA_FOCUS_MODE = CameraFocusMode.CONTINUOUS;
    public static final boolean DEFAULT_CAMERA_2_ENABLED = false;
    public static final int DEFAULT_AR_FEATURES = 15;
    public static final String DEFAULT_DISPLAY_TYPE = "User_Place";

}
