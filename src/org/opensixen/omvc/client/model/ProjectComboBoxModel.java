package org.opensixen.omvc.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

import org.compiere.util.Env;
import org.opensixen.dev.omvc.model.Project;
import org.opensixen.model.MRevision;
import org.opensixen.omvc.client.proxy.RemoteConsoleProxy;

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
