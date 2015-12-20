package net.flyingfat.common.lang;

import java.io.Serializable;

public class Paginator
  implements Serializable, Cloneable
{
  private static final long serialVersionUID = -7029079889853156238L;
  public static final int DEFAULT_ITEMS_PER_PAGE = 20;
  public static final int DEFAULT_SLIDER_SIZE = 10;
  public static final int UNKNOWN_ITEMS = 0;
  private int page;
  private int items;
  private int pageCount;
  private int itemsPerPage;
  
  public int getPageCount()
  {
    return this.pageCount;
  }
  
  public void setPageCount(int pageCount)
  {
    this.pageCount = pageCount;
  }
  
  public Paginator()
  {
    this(20);
  }
  
  public Paginator(int itemsPerPage)
  {
    this(itemsPerPage, 0);
  }
  
  public Paginator(int itemsPerPage, int items)
  {
    this.items = (items >= 0 ? items : 0);
    this.itemsPerPage = (itemsPerPage > 0 ? itemsPerPage : 20);
    
    this.page = calcPage(0);
  }
  
  public int getPages()
  {
    return (int)Math.ceil(this.items / this.itemsPerPage);
  }
  
  public int getPage()
  {
    return this.page;
  }
  
  public int setPage(int page)
  {
    return this.page = calcPage(page);
  }
  
  public int getItems()
  {
    return this.items;
  }
  
  public int setItems(int items)
  {
    this.items = (items >= 0 ? items : 0);
    setPage(this.page);
    setPageCount(getPages());
    return this.items;
  }
  
  public int getItemsPerPage()
  {
    return this.itemsPerPage;
  }
  
  public int setItemsPerPage(int itemsPerPage)
  {
    int tmp = this.itemsPerPage;
    
    this.itemsPerPage = (itemsPerPage > 0 ? itemsPerPage : 20);
    if (this.page > 0) {
      setPage((int)((this.page - 1) * tmp / this.itemsPerPage) + 1);
    }
    return this.itemsPerPage;
  }
  
  public int getBeginIndex()
  {
    if (this.page >= 0) {
      return this.itemsPerPage * this.page;
    }
    return 0;
  }
  
  public int getEndIndex()
  {
    if (this.page >= 0) {
      return Math.min(this.itemsPerPage * (this.page + 1) - 1, this.items);
    }
    return 0;
  }
  
  public int getFirstPage()
  {
    return calcPage(0);
  }
  
  public int getLastPage()
  {
    return calcPage(getPages() - 1);
  }
  
  public int getPreviousPage()
  {
    return calcPage(this.page - 1);
  }
  
  public int getPreviousPage(int n)
  {
    return calcPage(this.page - n);
  }
  
  public int getNextPage()
  {
    return calcPage(this.page + 1);
  }
  
  public int getNextPage(int n)
  {
    return calcPage(this.page + n);
  }
  
  protected int calcPage(int page)
  {
    int pages = getPages();
    if (pages > 0) {
      return page >= pages ? pages - 1 : page < 0 ? 0 : page;
    }
    return 0;
  }
  
  public int[] getSlider()
  {
    return getSlider(10);
  }
  
  public int[] getSlider(int width)
  {
    int pages = getPages();
    if ((pages < 1) || (width < 1)) {
      return new int[0];
    }
    if (width > pages) {
      width = pages;
    }
    int[] slider = new int[width];
    int first = this.page - (width - 1) / 2;
    if (first < 1) {
      first = 1;
    }
    if (first + width - 1 > pages) {
      first = pages - width + 1;
    }
    for (int i = 0; i < width; i++) {
      slider[i] = (first + i);
    }
    return slider;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer("Paginator: page ");
    if (getPages() < 1)
    {
      sb.append(getPage());
    }
    else
    {
      int[] slider = getSlider();
      for (int i = 0; i < slider.length; i++)
      {
        sb.append(slider[i]);
        if (i < slider.length - 1) {
          sb.append('\t');
        }
      }
    }
    sb.append(" of ").append(getPages()).append(",\n");
    sb.append("    Showing items ").append(getBeginIndex()).append(" to ").append(getEndIndex()).append(" (total ").append(getItems()).append(" items), ");
    


    return sb.toString();
  }
}
