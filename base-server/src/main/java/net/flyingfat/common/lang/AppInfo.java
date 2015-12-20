package net.flyingfat.common.lang;

import java.io.IOException;
import java.io.PrintStream;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppInfo
{
  private static final Logger logger = LoggerFactory.getLogger(AppInfo.class);
  private String appVersion;
  private String specificationTitle;
  private String specificationVersion;
  private String specificationVendor;
  private String implementationTitle;
  private String implementationVersion;
  private String implementationVendor;
  
  public void setJarLocation(String location)
  {
    try
    {
      JarFile jar = new JarFile(location);
      Manifest man = jar.getManifest();
      Attributes attrs = man.getMainAttributes();
      this.appVersion = attrs.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
      this.specificationTitle = attrs.getValue(Attributes.Name.SPECIFICATION_TITLE);
      
      this.specificationVersion = attrs.getValue(Attributes.Name.SPECIFICATION_VERSION);
      
      this.specificationVendor = attrs.getValue(Attributes.Name.SPECIFICATION_VENDOR);
      
      this.implementationTitle = attrs.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
      
      this.implementationVersion = attrs.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
      
      this.implementationVendor = attrs.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
      

      logger.info("Specification-Title=[{}]", this.specificationTitle);
      logger.info("Specification-Version=[{}]", this.specificationVersion);
      logger.info("Specification-Vendor=[{}]", this.specificationVendor);
      logger.info("Implementation-Title=[{}]", this.implementationTitle);
      logger.info("Implementation-Version=[{}]", this.implementationVersion);
      
      logger.info("Implementation-Vendor=[{}]", this.implementationVendor);
    }
    catch (IOException e)
    {
      logger.error("setJarLocation:", e);
    }
  }
  
  public String getAppVersion()
  {
    return this.appVersion;
  }
  
  public static Logger getLogger()
  {
    return logger;
  }
  
  public String getSpecificationTitle()
  {
    return this.specificationTitle;
  }
  
  public String getSpecificationVersion()
  {
    return this.specificationVersion;
  }
  
  public String getSpecificationVendor()
  {
    return this.specificationVendor;
  }
  
  public String getImplementationTitle()
  {
    return this.implementationTitle;
  }
  
  public String getImplementationVersion()
  {
    return this.implementationVersion;
  }
  
  public String getImplementationVendor()
  {
    return this.implementationVendor;
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
  
  public static void main(String[] args)
  {
    System.out.println(new AppInfo().getAppVersion());
  }
}
