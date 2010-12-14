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
