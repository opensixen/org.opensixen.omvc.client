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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.compiere.db.PreparedStatementProxy;
import org.compiere.util.CLogger;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.CStatementVO;
import org.compiere.util.DB;
import org.opensixen.dev.omvc.model.Script;
import org.opensixen.omvc.client.interfaces.IEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * SQLEngine 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class SQLEngine implements IEngine{
	
	private CLogger log = CLogger.getCLogger(getClass());

	/**
	 * 
	 * Run the updates
	 * @Override
	 */	
	public boolean run(Script script, String trxName) throws ScriptException {
		ArrayList<String> querys = getQueries(script);
		log.info("Running script_ID: " + script.getScript_ID() + " type: " + script.getType());
		for (String sql:querys)	{
			
			try {
				executeUpdate(sql, trxName);
			}
			catch (SQLException e)	{
				try { 
					DB.rollback(true, trxName);
				} catch (SQLException ex) {
					throw new ScriptException(ex);					
				}
				throw new ScriptException("No se puede ejecutar la consulta del script: + " + script.getName() + "[" + script.getScript_ID()+"].", e);
			}			
		}
		
		return true;	
	}
	
	private int executeUpdate(String sql, String trxName) throws SQLException	{		
		CPreparedStatement psmt = newCPreparedStatement(sql, trxName);
		int num = psmt.executeUpdate();
		psmt.close();
		psmt = null;
		return num;

	}
	
	
	/**
	 * Nos proporciona un CPrepareStatement sin pasar por el conversor
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param sql
	 * @param trxName
	 * @return CPreparedStatement proxy
	 */
	public static CPreparedStatement newCPreparedStatement(String sql, String trxName) {
		
		CStatementVO statement = new CStatementVO(ResultSet.TYPE_FORWARD_ONLY,	ResultSet.CONCUR_UPDATABLE, sql);
		statement.setTrxName(trxName);
		return (CPreparedStatement)Proxy.newProxyInstance(CPreparedStatement.class.getClassLoader(), 
				new Class[]{CPreparedStatement.class},	new PreparedStatementProxy(statement));
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

	/**
	 * Return the SQL queries
	 * @param script
	 * @return
	 */
	private ArrayList<String> getQueries(Script script)	{
		if (Script.TYPE_OSX.equals(script.getType()))	{
			return getOSXQueries(script);
		}
		else if (Script.TYPE_SQL.equals(script.getType()))	{
			return getSQLQueries(script);
		}
		throw new RuntimeException("Unknown type: " + script.getType());
	}
	
	/**
	 * Return raw sql as script
	 * @param script
	 * @return
	 */
	private ArrayList<String> getSQLQueries(Script script)	{
		ArrayList<String> queries = new ArrayList<String>();
		
		String sql = script.getScript();
		
		if (sql == null || sql.length() == 0)	{
			// Return empty array
			return queries;
		}
		//QueryExtractor.getQueries(script.getScript());
		
		queries.add(script.getScript());
		return queries;
	}
	
	/**
	 * Process xml script and return each query
	 * @param script
	 * @return
	 */
	private ArrayList<String> getOSXQueries(Script script)	{
		ArrayList<String> queries = new ArrayList<String>();
		
		String xml = formatScriptXML(script.getScript());
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
					String value = node.getNodeValue().trim();
					if (value != null && value.length() != 0)	{
						buff.append(value);
					}
				}
				String query = buff.toString().trim();
				if (query.endsWith(";"))	{
					query = query.substring(0, query.lastIndexOf(";")).trim();
				}
				// Limpiamos caracteres extraños
				query = query.replaceAll("\n", "");
				queries.add(query);
							
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

		return queries;
	}
	
	
	private String formatScriptXML(String script)	{
		StringBuffer buff = new StringBuffer("<scriptCollection>");		
		
		script = script.replaceAll("<script>", "<script><![CDATA[");
		script = script.replaceAll("</script>", "]]></script>");
		
		buff.append(script.replaceAll("--##", ""));
		buff.append("</scriptCollection>");						
		return buff.toString();
	}
	
}
