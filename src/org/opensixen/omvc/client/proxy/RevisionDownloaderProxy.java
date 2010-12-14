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

package org.opensixen.omvc.client.proxy;

import java.net.URL;
import java.util.List;

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
	 * Private constructor
	 * use getInstance();
	 */
	private RevisionDownloaderProxy() {
		super();		
	}

	
	
	/* (non-Javadoc)
	 * @see org.opensixen.riena.client.proxy.AbstractProxy#afterRegister()
	 */
	@Override
	protected void afterRegister() {
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
