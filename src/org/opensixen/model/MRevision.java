package org.opensixen.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public class MRevision extends X_AD_OMRevision {

	public MRevision(Properties ctx, int AD_OMRevision_ID, String trxName) {
		super(ctx, AD_OMRevision_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MRevision(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	
	public static List<MRevision> getRevisions(Properties ctx)	{
		return POFactory.getList(ctx, MRevision.class);
	}
	
}
