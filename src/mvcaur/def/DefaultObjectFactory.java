package mvcaur.def;

import mvcaur.ObjectFactory;

public class DefaultObjectFactory implements ObjectFactory {

	@Override
	public Object create(Class<?> clazz) throws Exception {
		return clazz.newInstance();
	}


}
