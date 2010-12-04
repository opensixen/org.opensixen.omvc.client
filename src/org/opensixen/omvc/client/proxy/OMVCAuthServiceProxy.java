/**
 * 
 */
package org.opensixen.omvc.client.proxy;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.opensixen.riena.Activator;
import org.opensixen.riena.client.proxy.AbstractProxy;
import org.opensixen.riena.interfaces.IConnectionChangeListener;
import org.opensixen.riena.interfaces.IServiceConnectionHandler;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class OMVCAuthServiceProxy implements IConnectionChangeListener {
	
	private boolean registered = false;
	private IServiceConnectionHandler serviceConnectionHandler;
	private IRemoteServiceRegistration serviceRegistration;
	
	private static OMVCAuthServiceProxy instance;
	
	public static OMVCAuthServiceProxy getInstance()	{
		if (instance == null)	{
			instance = new OMVCAuthServiceProxy();
		}
		return instance;
	}
	
	
	/**
	 * Private construnctor
	 * see getUInstance
	 */
	private OMVCAuthServiceProxy()	{
		
	}
	
	/**
	 * @return the serviceConnectionHandler
	 */
	public IServiceConnectionHandler getServiceConnectionHandler() {
		return serviceConnectionHandler;
	}

	/**
	 * @param serviceConnectionHandler the serviceConnectionHandler to set
	 */
	public void setServiceConnectionHandler(
			IServiceConnectionHandler aServiceConnectionHandler) {
		serviceConnectionHandler = aServiceConnectionHandler;
		serviceConnectionHandler.addConnectionChangeListener(this);
	}
	
	public boolean register()	{
		if (registered)	{
			return true;
		}
		
		// Registramos el Authentication Service		
		String url = AbstractProxy.getURL(serviceConnectionHandler.getServiceConnection().getUrl(), IAuthenticationService.WS_ID) ;			
		serviceRegistration = Register.remoteProxy(IAuthenticationService.class).usingUrl(url).withProtocol("hessian").andStart(Activator.getContext());
		registered = true;
		
		return true;
		
	}
	
	public void unregister()	{
		if (registered == false || serviceRegistration == null)	{
			return;
		}
		
		serviceRegistration.unregister();
	}


	/* (non-Javadoc)
	 * @see org.opensixen.riena.interfaces.IConnectionChangeListener#fireConnectionChange()
	 */
	@Override
	public boolean fireConnectionChange() {
		unregister();
		return register();
	}
	

}
