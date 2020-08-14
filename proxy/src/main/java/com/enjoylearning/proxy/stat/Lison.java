package com.enjoylearning.proxy.stat;

public class Lison implements ManToolsCompany {
	
	private ManToolsCompany mts;
	
	
	@Override
	public void saleManTool(String size) {
		doSomeThingBefore();
		mts.saleManTool(size);
		doSomeThingEnd();
	}
	
	
	
	private void doSomeThingBefore(){
		System.out.println("根据你的需求，我们帮你挑选一个符合你口味的娃娃！");
	}

	
	private void doSomeThingEnd(){
		System.out.println("精美包装，快递一条龙服务！");
	}
	

	public Lison(ManToolsCompany mts) {
		super();
		this.mts = mts;
	}




	public ManToolsCompany getMts() {
		return mts;
	}




	public void setMts(ManToolsCompany mts) {
		this.mts = mts;
	}





}
