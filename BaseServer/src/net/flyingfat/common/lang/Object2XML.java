package net.flyingfat.common.lang;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Object2XML
{
  public static String object2XML(Object obj, String outFileName)
    throws FileNotFoundException
  {
    File outFile = new File(outFileName);
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
    


    XMLEncoder xmlEncoder = new XMLEncoder(bos);
    
    xmlEncoder.writeObject(obj);
    
    xmlEncoder.close();
    
    return outFile.getAbsolutePath();
  }
  
  public static Object xml2Object(String inFileName)
    throws FileNotFoundException
  {
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFileName));
    

    XMLDecoder xmlDecoder = new XMLDecoder(bis);
    
    Object obj = xmlDecoder.readObject();
    
    xmlDecoder.close();
    
    return obj;
  }
}
