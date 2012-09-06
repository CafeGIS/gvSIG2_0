package es.idr.crs.crsGT;

import java.util.Arrays;
import java.util.Iterator;

import org.geotools.factory.FactoryRegistry;
import org.geotools.referencing.operation.MathTransformProvider;

public class ProvidersTest {
	

	public static void main(String[] args) {
		final Class[] categories = {org.geotools.referencing.operation.MathTransformProvider.class};
		FactoryRegistry registry = new FactoryRegistry(Arrays.asList(categories));
		Iterator providers = registry.getServiceProviders(MathTransformProvider.class);
		Iterator providers2 = null;
		MathTransformProvider provider = null;
		MathTransformProvider provider2 = null;
		while (providers.hasNext()){
			provider = (MathTransformProvider) providers.next(); 
			if(provider.nameMatches("IDR")){
				providers2 = registry.getServiceProviders(MathTransformProvider.class);
				while (providers2.hasNext()){
					provider2 = (MathTransformProvider) providers2.next();
					if(provider2.nameMatches(provider.getName().toString()) && !provider2.nameMatches("IDR"))
						registry.deregisterServiceProvider(provider2, categories[0]);
				}
			}
		}
		providers = registry.getServiceProviders(MathTransformProvider.class);
		while (providers.hasNext()) {
            provider = (MathTransformProvider) providers.next();
            if (provider.nameMatches("IDR"))
            	System.out.println("-IDR-"+provider.toString());
            else
            	System.out.println(provider.toString());
		}
	}
}

