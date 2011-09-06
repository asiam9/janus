package com.em.janus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;

import com.em.janus.model.Entity;

public enum QueryManager {
	
	INSTANCE;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	public <T extends Entity> Set<T> query(IDataAccessObject<T> dao, String queryString, Object ... params) {
		
		Set<T> results = new TreeSet<T>();
		
		//if there is a problem (Yo! I'll solve it.) return the empty set
		if(dao == null || queryString == null) return results;
		
		//get connection
		Connection connection = ConnectionManager.INSTANCE.getConnection();
	
		try {
			
			int tries = 0;
			int max_tries = 20;
			
			ResultSet rs = null;
			
			while(tries < max_tries) {
				try { 
					PreparedStatement query = connection.prepareStatement(queryString);
								
					//build the query from the params passed
					this.buildQuery(queryString,query,params);

					//do the query
					rs = query.executeQuery();
					
					//query complete
					break;
				} catch (Exception ex) {
					this.logger.warn("Retrying database query: {}",ex.getMessage());
				}
				//increment tries
				tries++;
				
				if(tries >= max_tries) {
					this.logger.error("Exceeded max tries ({}) for query.  Query failed.",""+tries);
				}
			}
		 
			while(rs.next()) {
				
				T entity = null;
				try {
					entity = dao.build(rs);
				} catch (SQLException e) {
					this.logger.error("Could not build entity.",e);
				}
				
				if(entity != null) {
					results.add(entity);
				}
				
			}
			
			//close the result set when done
			rs.close();
		
		} catch (SQLException e) {
			this.logger.error("Could not create entity set.",e);
		}		
		
		try {
			connection.close();
		} catch (SQLException e) {
			this.logger.error("Could not close connection.",e);
		}	
		
		return results;		
	}

	private void buildQuery(String queryString, PreparedStatement query, Object[] params) {
		
		int index = 1;

		try {
			for(Object o : params) {
				
				try {
				
					if(o == null) {
						query.setNull(index,java.sql.Types.VARCHAR);
					}
					
					//implement instance of types for query.set...()
					if(o instanceof String) {
						query.setString(index, (String)o);
					} else if(o instanceof Integer) {
						query.setInt(index, (Integer)o);
					} else if(o instanceof Long) { 
						query.setLong(index, (Long)o);
					} else if(o instanceof Double) {
						query.setDouble(index, (Double)o);
					} else if(o instanceof Float) {
						query.setFloat(index, (Float)o);
					} else if(o instanceof Date || o instanceof java.sql.Date) {
						query.setDate(index, (java.sql.Date)o);
					}
	
					//implement index
					index++;
					
				} catch (SQLException e) {
					this.logger.error("Could not set the {} param.",index,e);
				}
			}
			
		} catch (Exception e) {
			this.logger.error("Error in query \"{}\" at param " + index,queryString,e);
			throw new RuntimeException(e);
		}
		
	}

}
