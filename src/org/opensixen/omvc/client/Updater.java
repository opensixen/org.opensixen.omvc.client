 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

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

/**
 * 
 * Updater 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
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
