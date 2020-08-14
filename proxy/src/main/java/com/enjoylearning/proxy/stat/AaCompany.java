package com.enjoylearning.proxy.stat;

public class AaCompany implements ManToolsCompany {

	@Override
	public void saleManTool(String size) {
		System.out.println("A情趣用品公司为您提供了一个size为"+size+"的女模特！");
	}

}
