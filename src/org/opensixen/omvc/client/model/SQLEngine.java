package org.opensixen.omvc.client.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.client.interfaces.IEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SQLEngine implements IEngine{
	
	private CLogger log = CLogger.getCLogger(getClass());

	@Override
	public boolean run(Script script) throws ScriptException {
		ArrayList<String> querys = getQueryes(script);
		String trxName = Trx.createTrxName();
		log.info("Running script_ID: " + script.getScript_ID());
		for (String sql:querys)	{
			if (DB.executeUpdate(sql, trxName) == -1)	{
				try { 
					DB.rollback(true, trxName);
				} catch (SQLException e) {
					throw new ScriptException(e);					
				}
				throw new ScriptException("No se puede ejecutar la consulta: " + sql);
			}
		}
			
		try {
			DB.commit(true, trxName);
		} catch (Exception e) {			
			throw new ScriptException(e);	
		} 
		return true;
		
		
	}

	@Override
	public Object getResult() {
		return null;
	}

	@Override
	public String[] getAvailableEngines() {
		String engine; 
		
		// TODO: Comprobar si la base de datos es postgres u oracle
		if (true)	{
			engine= Script.ENGINE_POSTGRESQL;
		}
		String [] engines = {Script.ENGINE_POSTGRESQL};
		return engines;
	}

	private ArrayList<String> getQueryes(Script script)	{
		ArrayList<String> queryes = new ArrayList<String>();
		
		String xml = getScriptXML(script.getScript());
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		
		
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			
			NodeList childrens = doc.getElementsByTagName("script");
			for (int i=0; i < childrens.getLength(); i++) {
				StringBuffer buff = new StringBuffer();
				Node child = childrens.item(i);
				NodeList nodes = child.getChildNodes();
				
				for (int x=0; x < nodes.getLength(); x++)	{
					Node node = nodes.item(x);
					String value = node.getNodeValue().trim(); //node.getNextSibling().getNodeValue().trim();
					if (value != null && value.length() != 0)	{
						buff.append(value);
					}
				}
				String query = buff.toString().trim();
				if (query.endsWith(";"))	{
					query = query.substring(0, query.lastIndexOf(";")).trim();
				}
				
				queryes.add(query);
							
			}

		} catch (SAXException e) {
			log.severe("No se puede procesar el script.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return queryes;
	}
	
	private String getScriptXML(String script)	{
		StringBuffer buff = new StringBuffer("<scriptCollection>");		
		buff.append(script.replaceAll("--##", ""));
		buff.append("</scriptCollection>");						
		return buff.toString();
	}
	
}
