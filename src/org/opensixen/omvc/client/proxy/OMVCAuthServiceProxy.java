/**
 * 
 */
package org.opensixen.omvc.client.proxy;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.opensixen.riena.Activator;
import org.opensixen.riena.client.proxy.AbstractProxy;
import org.opensixen.riena.interfaces.IServiceConnectionHandler;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class OMVCAuthServiceProxy {
	
	private static boolean registered = false;
	private static IServiceConnectionHandler serviceConnectionHandler;
	private static IRemoteServiceRegistration serviceRegistration;
	
	
	/**
	 * @return the serviceConnectionHandler
	 */
	public static IServiceConnectionHandler getServiceConnectionHandler() {
		return serviceConnectionHandler;
	}

	/**
	 * @param serviceConnectionHandler the serviceConnectionHandler to set
	 */
	public static void setServiceConnectionHandler(
			IServiceConnectionHandler aServiceConnectionHandler) {
		serviceConnectionHandler = aServiceConnectionHandler;
	}
	
	public static boolean register()	{
		if (registered)	{
			return true;
		}
		
		// Registramos el Authentication Service		
		String url = AbstractProxy.getURL(serviceConnectionHandler.getServiceConnection().getUrl(), IAuthenticationService.WS_ID) ;			
		serviceRegistration = Register.remoteProxy(IAuthenticationService.class).usingUrl(url).withProtocol("hessian").andStart(Activator.getContext());
		registered = true;
		
		return true;
		
	}
	
	public static void unregister()	{
		if (registered == false || serviceRegistration == null)	{
			return;
		}
		
		serviceRegistration.unregister();
	}
	

}
