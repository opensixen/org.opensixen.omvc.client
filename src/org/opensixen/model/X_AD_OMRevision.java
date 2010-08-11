/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.opensixen.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_OMRevision
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_AD_OMRevision extends PO implements I_AD_OMRevision, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20100810L;

    /** Standard Constructor */
    public X_AD_OMRevision (Properties ctx, int AD_OMRevision_ID, String trxName)
    {
      super (ctx, AD_OMRevision_ID, trxName);
      /** if (AD_OMRevision_ID == 0)
        {
			setAD_OMRevision_ID (0);
			setName (null);
			setProject_ID (0);
			setRevision (0);
        } */
    }

    /** Load Constructor */
    public X_AD_OMRevision (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_AD_OMRevision[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AD_OMRevision.
		@param AD_OMRevision_ID AD_OMRevision	  */
	public void setAD_OMRevision_ID (int AD_OMRevision_ID)
	{
		if (AD_OMRevision_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_OMRevision_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_OMRevision_ID, Integer.valueOf(AD_OMRevision_ID));
	}

	/** Get AD_OMRevision.
		@return AD_OMRevision	  */
	public int getAD_OMRevision_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OMRevision_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Activate Audit.
		@param IsAudited 
		Activate Audit Trail of what numbers are generated
	  */
	public void setIsAudited (boolean IsAudited)
	{
		set_Value (COLUMNNAME_IsAudited, Boolean.valueOf(IsAudited));
	}

	/** Get Activate Audit.
		@return Activate Audit Trail of what numbers are generated
	  */
	public boolean isAudited () 
	{
		Object oo = get_Value(COLUMNNAME_IsAudited);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Project_ID.
		@param Project_ID Project_ID	  */
	public void setProject_ID (int Project_ID)
	{
		if (Project_ID < 1) 
			set_Value (COLUMNNAME_Project_ID, null);
		else 
			set_Value (COLUMNNAME_Project_ID, Integer.valueOf(Project_ID));
	}

	/** Get Project_ID.
		@return Project_ID	  */
	public int getProject_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Revision.
		@param Revision Revision	  */
	public void setRevision (int Revision)
	{
		set_Value (COLUMNNAME_Revision, Integer.valueOf(Revision));
	}

	/** Get Revision.
		@return Revision	  */
	public int getRevision () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Revision);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}