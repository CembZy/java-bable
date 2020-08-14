package cap3.config;

import cap3.bean.Monkey;
import org.springframework.beans.factory.FactoryBean;


public class JamesFactoryBean implements FactoryBean<Monkey>{

	public Monkey getObject() throws Exception {
		// TODO Auto-generated method stub
		return new Monkey();
	}

	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return Monkey.class;
	}
	
	public boolean isSingleton() {
		return true;
	}
}
