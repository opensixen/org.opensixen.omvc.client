/**
 * 
 */
package org.opensixen.omvc.client.proxy;

import java.util.ArrayList;
import java.util.List;

import org.opensixen.dev.omvc.interfaces.IPO;
import org.opensixen.dev.omvc.interfaces.IRemoteConsole;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.dev.omvc.model.Script;
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
	
	
	
	public synchronized static RemoteConsoleProxy getInstance()	{
		if (instance == null)	{
			instance = new RemoteConsoleProxy();
		}
		
		return instance;
	}
	
	/**
	 * @param clazz
	 */
	protected RemoteConsoleProxy() {
		super();
		console = getService();
		
	}
	
	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#getServicePath()
	 */
	@Override
	public String getServicePath() {
		return IRemoteConsole.path;
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
