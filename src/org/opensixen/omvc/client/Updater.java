package org.opensixen.omvc.client;

import java.util.ArrayList;
import java.util.List;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Trx;
import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.model.MRevision;
import org.opensixen.model.POFactory;
import org.opensixen.omvc.client.model.SQLEngine;
import org.opensixen.omvc.client.model.ScriptException;
import org.opensixen.omvc.client.proxy.RevisionDownloaderProxy;

public class Updater {

	private CLogger log = CLogger.getCLogger(getClass());
	private RevisionDownloaderProxy downloader;

	
	public Updater()	{
		downloader = RevisionDownloaderProxy.getInstance();		
	}
	
	
	public boolean update() throws ScriptException {	
		return update(getPendingUpdates());
	}
	
	public boolean update(List<ProjectUpdates> updates)	throws ScriptException {
		
		// Guardamos el estado de migrationScriptState y lo cambiamos a false
		// para que no guarde las actualizaciones en el migration script.
		
		boolean migrationScriptState = Ini.isPropertyBool(Ini.P_LOGMIGRATIONSCRIPT);
		if (migrationScriptState)	{
			Ini.setProperty(Ini.P_LOGMIGRATIONSCRIPT, false);
		}
		
		SQLEngine engine = new SQLEngine();
		
		
		
		for (ProjectUpdates update : updates)	{		
			// If update is not selected, continue
			if (!update.isSelected())	{
				continue;
			}
			
			List<Revision> revisions =  update.getRevisions();
			MRevision project = update.getProject();
			
			for (Revision rev:revisions)	{				
				log.info("Running revision_ID " + rev.getRevision_ID() + ": " + rev.getDescription());
				String trxName = Trx.createTrxName();
				// Get scripts from server
				List<Script> scripts = downloader.getScripts(rev, engine.getAvailableEngines());
				for (Script script: scripts)	{
					
					// Try to run engine
					if (!engine.run(script, trxName))	{
						return false;
					}					
				}
				
				// Actualizamos el proyecto a la revision descargada.
				project.setRevision(rev.getRevision_ID());
				project.save(trxName);
				
				try {
					DB.commit(true, trxName);
				} catch (Exception e) {			
					throw new ScriptException(e);	
				} 
				
			}
		}
		
		// volvemos a poner logmigrationscript a su valor original;
		Ini.setProperty(Ini.P_LOGMIGRATIONSCRIPT, migrationScriptState);
		return true;
	}
	
	/**
	 * Return pending revisions from all projects
	 * @return
	 */
	public List<ProjectUpdates> getPendingUpdates()	{
		ArrayList<ProjectUpdates> revisions = new ArrayList<ProjectUpdates>();
		
		List<MRevision> projects = POFactory.getList(Env.getCtx(), MRevision.class);
		for (MRevision r:projects)	{						
			List<Revision> projectRevisions =  downloader.getRevisions(r.getProject_ID(),r.getRevision());
			
			if (projectRevisions.size() > 0)	{
				ProjectUpdates updates = new ProjectUpdates();
				updates.setProject(r);
				updates.setRevisions(projectRevisions);
				revisions.add(updates);
			}
		}
		
		return revisions;
	}
	
	public String getServerDescription()	{
		return downloader.getServiceConnectionHandler().getServiceConnection().getUrl();
	}
	
}
