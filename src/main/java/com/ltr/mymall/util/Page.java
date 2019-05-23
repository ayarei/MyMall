package com.ltr.mymall.util;

public class Page {
	
	private int start; //开始位置
	private int count; //每页显示数量
	private int total; //数据总数
	private String param; //往前台url传送特定字符
	
	private static final int defaultCount = 8;//默认每页数据数量
	
	public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
 
    public Page (){
        count = defaultCount;
    }
    
    public Page(int start, int count) {
        this();
        this.start = start;
        this.count = count;
    }
    
    //第一页不能继续点击上一页
    public boolean isHasPrevious() {
    	if(start == 0)
    		return false;
    	return true;
    }
    
	//最后一页不能继续点击下一页
    public boolean isHasNext() {
    	if(start == getLast())
    		return false;
    	return true;
    }
    
    //最后一页start的位置
	public int getLast(){
		int last;
		if(0 == total % count)
			last = total - count;
		else
			last = total - total % count;
		last = last<0 ? 0 : last;
		return last;
	}
	
	//获取总页数
	public int getTotalPage() {
		int totalPage;
		if(0 == total % count)
			totalPage = total / count;
		else
			totalPage = total / count + 1;
		
		/*当没有数据时默认页数总数为1*/
		if(0 == totalPage)
			totalPage = 1;
		return totalPage;
	}
	public String toString() {
        return "Page [start=" + start + ", count=" + count + ", total=" + total + ", getStart()=" + getStart()
                + ", getCount()=" + getCount() + ", isHasPreviouse()=" + isHasPrevious() + ", isHasNext()="
                + isHasNext() + ", getTotalPage()=" + getTotalPage() + ", getLast()=" + getLast() + "]";
    }
	
	public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public String getParam() {
        return param;
    }
    public void setParam(String param) {
        this.param = param;
    }
}





















