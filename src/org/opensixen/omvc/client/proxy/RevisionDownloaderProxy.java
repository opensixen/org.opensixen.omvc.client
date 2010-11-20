/**
 * 
 */
package org.opensixen.omvc.client.proxy;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.opensixen.dev.omvc.interfaces.IRevisionDownloader;
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
public class RevisionDownloaderProxy extends AbstractProxy<IRevisionDownloader>{
	
	private IRevisionDownloader downloader;
	private CLogger log = CLogger.getCLogger(getClass());
	private static CLogger s_log = CLogger.getCLogger(RevisionDownloaderProxy.class);
	private static RevisionDownloaderProxy instance;
	
	/* stuff for the login configuration */
	private static final String JAAS_CONFIG_FILE = "data/jaas_config.txt"; //$NON-NLS-1$
	private static final String CONFIG_PREF = "loginConfiguration"; //$NON-NLS-1$
	private static final String CONFIG_DEFAULT = "omvc"; //$NON-NLS-1$
	
	
	
	
	public synchronized static RevisionDownloaderProxy getInstance()	{
		if (instance == null)	{
			instance = new RevisionDownloaderProxy();			
		}
		
		return instance;
	}
	
	
	/**
	 * @param clazz
	 */
	protected RevisionDownloaderProxy() {
		super();
		downloader = getService();
	}

	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#getServicePath()
	 */
	@Override
	public String getServicePath() {
		return IRevisionDownloader.path;
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
		return downloader.testService();
	}

	/**
	 * @param project_ID
	 * @param from
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRevisionDownloader#getRevisions(int, int)
	 */
	public List<Revision> getRevisions(int project_ID, int from) {
		return downloader.getRevisions(project_ID, from);
	}

	/**
	 * @param revision
	 * @param engines
	 * @return
	 * @see org.opensixen.dev.omvc.interfaces.IRevisionDownloader#getScripts(org.opensixen.dev.omvc.model.Revision, java.lang.String[])
	 */
	public List<Script> getScripts(Revision revision, String[] engines) {
		return downloader.getScripts(revision, engines);
	}


	
}
