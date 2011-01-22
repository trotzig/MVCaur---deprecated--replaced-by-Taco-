package mvcaur;

public class DefaultObjectFactory implements ObjectFactory {

	@Override
	public Object create(Class<?> clazz) throws Exception {
		return clazz.newInstance();
	}


}
