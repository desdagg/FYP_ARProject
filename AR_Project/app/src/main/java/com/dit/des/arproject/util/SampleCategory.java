package com.dit.des.arproject.util;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class SampleCategory implements Serializable {

    private final List<AppData> samples;
    private final String name;

    public SampleCategory(@NonNull List<AppData> samples, @NonNull String name) {
        this.samples = samples;
        this.name = name;
    }

    public List<AppData> getSamples() {
        return samples;
    }

    public String getName() {
        return name;
    }
}
