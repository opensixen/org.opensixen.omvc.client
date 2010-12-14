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

package org.opensixen.omvc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import org.compiere.util.Env;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.model.MRevision;
import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;
/**
 * ProjectComboBoxModel 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class ProjectComboBoxModel implements ComboBoxModel {

	private ArrayList<Project> projects;
	private Project selected;
		
	public ProjectComboBoxModel() {
			loadProjects();
	}

	/**
	 * Load projects from server and active only registered.
	 */
	private void loadProjects()	{
		projects = new ArrayList<Project>();
		List<Project> serverProjects;
		try	{
			RemoteConsoleProxy console = RemoteConsoleProxy.getInstance();
			serverProjects = console.getProjects();
		}
		catch (Exception e)	{
			throw new RuntimeException("No se pueden cargar los proyectos", e);			
		}	
		
		List<MRevision> channels = MRevision.getRevisions(Env.getCtx());
		for (MRevision channel:channels)	{
			for (Project project:serverProjects)	{
				if (project.getProject_ID() == channel.getProject_ID())	{
					projects.add(project);
				}
			}
		}
		
	}
	
	@Override
	public int getSize() {
		return projects.size();
	}

	@Override
	public Object getElementAt(int index) {
		return projects.get(index).getName();
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSelectedItem(Object anItem) {
		for (Project project:projects)	{
			if (project.getName().equals(anItem))	{
				selected = project;
				return;
			}
		}
		
		selected = null;
	}

	@Override
	public Object getSelectedItem() {
		if (selected != null)	{
			return selected.getName();
		}
		else {
			return null;
		}
	}
	
	public Project getSelectedProject()	{
		return selected;
	}

}
