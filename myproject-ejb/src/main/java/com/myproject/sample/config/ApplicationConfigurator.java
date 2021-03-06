package com.myproject.sample.config;

public interface ApplicationConfigurator {
    String getProperty(AppProperty property);

    ScalerType getScalerBeanQualifier();

    GeneratedSourcesType getCanvasBeanQualifier();

    boolean useIM();

    boolean generatePdf();

    void invalidate();
}
