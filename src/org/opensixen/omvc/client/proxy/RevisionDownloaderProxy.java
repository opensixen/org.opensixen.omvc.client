/**
 * 
 */
package org.opensixen.omvc.client.proxy;

import java.util.List;

import org.compiere.model.MSysConfig;
import org.opensixen.dev.omvc.interfaces.IRevisionDownloader;
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
public class RevisionDownloaderProxy extends AbstractProxy<IRevisionDownloader>{
	
	private IRevisionDownloader downloader;

	private static RevisionDownloaderProxy instance;
	
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
