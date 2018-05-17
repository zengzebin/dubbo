package com.dubbo.common.utils;

import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesContainer extends PropertyPlaceholderConfigurer
{
  static Properties mergedProperties;

  public Properties mergeProperties()
    throws IOException
  {
    if (mergedProperties == null) {
      mergedProperties = super.mergeProperties();
    }
    return mergedProperties;
  }

  public static String getPropVal(String propKey) {
    return mergedProperties.getProperty(propKey);
  }

  public static Properties getMergedProperties() {
    return mergedProperties;
  }

  public static void setMergedProperties(Properties mergedProperties) {
    mergedProperties = mergedProperties;
  }
}
