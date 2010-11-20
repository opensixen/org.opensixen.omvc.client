/**
 * 
 */
package org.opensixen.omvc.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opensixen.dev.omvc.model.Revision;
import org.opensixen.model.MRevision;

/**
 * Clase contenedora de las actualizaciones por proyecto. 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class ProjectUpdates {
	
	private MRevision project;
	
	private String projectName;
	
	private List<Revision> revisions = new ArrayList<Revision>();

	private boolean selected = true;
	
	
	/**
	 * @return the project
	 */
	public MRevision getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(MRevision project) {
		this.project = project;
		setProjectName(project.getName());
	}

	/**
	 * @return the revisions
	 */
	public List<Revision> getRevisions() {
		return revisions;
	}
	

	/**
	 * @param revisions the revisions to set
	 */
	public void setRevisions(List<Revision> revisions) {
		this.revisions = revisions;
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return revisions.size();
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Revision e) {
		return revisions.add(e);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return revisions.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends Revision> c) {
		return revisions.addAll(c);
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}	
			
}
