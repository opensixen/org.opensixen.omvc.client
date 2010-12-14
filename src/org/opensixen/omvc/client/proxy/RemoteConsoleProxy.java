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
