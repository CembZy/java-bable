package com.enjoylearning.proxy;

import com.enjoylearning.proxy.dynamic.BbCompany;
import com.enjoylearning.proxy.dynamic.LisonCompany;
import com.enjoylearning.proxy.dynamic.WomanToolsCompany;
import com.enjoylearning.proxy.stat.AaCompany;
import com.enjoylearning.proxy.stat.Lison;
import com.enjoylearning.proxy.stat.ManToolsCompany;
import com.enjoylearning.proxy.utils.ProxyUtils;

public class Client {
	
	public static void main(String[] args) {
//		ManToolsCompany mts = new AaCompany();
//		Lison lison = new Lison(mts);
//		lison.saleManTool("c");
		
		WomanToolsCompany bbcomp = new BbCompany();
		ManToolsCompany aacomp = new AaCompany();
		
		LisonCompany lisonCompany = new LisonCompany();
		
		lisonCompany.setCompy(aacomp);
		ManToolsCompany lison1 = (ManToolsCompany) lisonCompany.getProxyInstance();
		lison1.saleManTool("C");
		
		lisonCompany.setCompy(bbcomp);
		WomanToolsCompany lison2 = (WomanToolsCompany) lisonCompany.getProxyInstance();
		lison2.saleWomanTool(1.8f);
		
		
		ProxyUtils.generateClassFile(ManToolsCompany.class, lison1.getClass().getName());
		ProxyUtils.generateClassFile(WomanToolsCompany.class, lison2.getClass().getName());
		
		
		
	}

}
