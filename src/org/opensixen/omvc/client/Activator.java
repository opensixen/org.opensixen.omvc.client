package org.opensixen.omvc.client;

import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.eclipse.riena.communication.core.factory.Register;
import org.opensixen.dev.omvc.interfaces.IRevisionDownloader;
import org.opensixen.dev.omvc.interfaces.IRienaService;
import org.opensixen.dev.omvc.model.ServiceRegistrationException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {
	private static CLogger s_log = CLogger.getCLogger(Activator.class);
	private static BundleContext context;
	private static boolean registered;
	private static IRevisionDownloader downloader;

	
	
	public static IRevisionDownloader getDownloader() {
		if (!registered)	{
			register();
		}
		return downloader;
	}

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public static void register() throws ServiceRegistrationException	{
		if (registered)	{
			return;
		}		
		String server = MSysConfig.getValue("DICTIONARY_ID_WEBSITE");
		if (server.endsWith("/"))	{
			server = server.substring(0, server.lastIndexOf('/'));
		}
		String url = server + IRevisionDownloader.path;
		downloader = register(IRevisionDownloader.class, url);
		registered = true;
	}
	
	private static <T extends IRienaService > T register(Class<T> clazz, String url ) throws ServiceRegistrationException	{
		try {
			Register.remoteProxy(clazz).usingUrl(url).withProtocol("hessian").andStart(context);
			ServiceReference ref = Activator.getContext().getServiceReference(clazz.getName());
			T service = (T) context.getService(ref);
			if (service.testService())	{
				s_log.info("Service registered: " + url);
			}
			return service;
		}
		catch (Exception e)	{
			throw new ServiceRegistrationException("No se puede conectar con el servidor en la URL: " + url, e);
		}
	}

}
