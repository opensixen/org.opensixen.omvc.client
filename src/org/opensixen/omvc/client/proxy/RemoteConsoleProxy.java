/**
 * 
 */
package org.opensixen.omvc.client.proxy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.opensixen.dev.omvc.interfaces.IPO;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.client.Activator;
import org.opensixen.riena.client.proxy.AbstractProxy;

/**
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class RemoteConsoleProxy extends AbstractProxy<IRemoteConsole> {
	
	private static IRemoteConsole console;
	
	private static RemoteConsoleProxy instance;
	
	/* stuff for the login configuration */
	private static final String JAAS_CONFIG_FILE = "data/jaas_config.txt"; //$NON-NLS-1$
	private static final String CONFIG_PREF = "loginConfiguration"; //$NON-NLS-1$
	private static final String CONFIG_DEFAULT = "omvc"; //$NON-NLS-1$
	
	
	
	public synchronized static RemoteConsoleProxy getInstance()	{
		if (instance == null)	{
			instance = new RemoteConsoleProxy();
		}
		
		return instance;
	}
	
	/**
	 * Private constructor
	 * use getInstance();
	 */
	private RemoteConsoleProxy() {
		super();
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#afterRegister()
	 */
	@Override
	protected void afterRegister() {
		console = getService();
	}

	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#getServicePath()
	 */
	@Override
	public String getServicePath() {
		return IRemoteConsole.path;
	}	
		
	

	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#needAuth()
	 */
	@Override
	public boolean needAuth() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#getJAASConfigurationName()
	 */
	@Override
	public String getJAASConfigurationName() {
		// TODO Auto-generated method stub
		return new DefaultScope().getNode(Activator.PLUGIN_ID).get(CONFIG_PREF, CONFIG_DEFAULT);
	}

	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#getJAASConfigFile()
	 */
	@Override
	public URL getJAASConfigFile() {
		// TODO Auto-generated method stub
		return Activator.getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
	}

	/**
	 * @return
	 * @see org.opensixen.riena.interfaces.IRienaService#testService()
	 */
	public boolean testService() {
		return console.testService();
	}

	/**
	 * @param po
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#save(org.opensixen.dev.omvc.interfaces.IPO)
	 */
	public boolean save(IPO po) {
		return console.save(po);
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#load(java.lang.Class, int)
	 */
	public <T extends IPO> T load(Class<T> clazz, int id) {
		return console.load(clazz, id);
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#getAll(java.lang.Class)
	 */
	public <T extends IPO> ArrayList<T> getAll(Class<T> clazz) {
		return console.getAll(clazz);
	}

	/**
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#getRevisions()
	 */
	public List<Revision> getRevisions() {
		return console.getRevisions();
	}

	/**
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#getProjects()
	 */
	public List<Project> getProjects() {
		return console.getProjects();
	}

	/**
	 * @param revision
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#getScripts(org.opensixen.dev.omvc.model.Revision)
	 */
	public List<Script> getScripts(Revision revision) {
		return console.getScripts(revision);
	}

	/**
	 * @param revision
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRemoteConsole#uploadRevison(org.opensixen.dev.omvc.model.Revision)
	 */
	public int uploadRevison(Revision revision) {
		return console.uploadRevison(revision);
	}

}
