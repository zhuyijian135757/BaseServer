package net.flyingfat.common.lang;

public class IpPortPair
  implements Comparable<IpPortPair>
{
  private String ip = null;
  private int port = 0;
  
  public IpPortPair() {}
  
  public IpPortPair(String ip, int port)
  {
    this.ip = ip;
    this.port = port;
  }
  
  public String getIp()
  {
    return this.ip;
  }
  
  public void setIp(String ip)
  {
    this.ip = ip;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public void setPort(int port)
  {
    this.port = port;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (this.ip == null ? 0 : this.ip.hashCode());
    result = 31 * result + this.port;
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    IpPortPair other = (IpPortPair)obj;
    if (this.ip == null)
    {
      if (other.ip != null) {
        return false;
      }
    }
    else if (!this.ip.equals(other.ip)) {
      return false;
    }
    if (this.port != other.port) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return this.ip + ":" + this.port;
  }
  
  public int compareTo(IpPortPair o)
  {
    int rslt = this.ip.compareTo(o.ip);
    if (0 == rslt) {
      return this.port - o.port;
    }
    return rslt;
  }
}
