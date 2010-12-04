package org.opensixen.omvc.client.interfaces;

import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.client.model.ScriptException;
import org.opensixen.osgi.interfaces.IService;

public interface IEngine extends IService {

	public String[] getAvailableEngines();
	
	public boolean run(Script script, String trxName) throws ScriptException;
	
	public Object getResult();
}
