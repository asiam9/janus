package com.em.janus.dao;

import java.nio.channels.NonWritableChannelException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;

import com.em.janus.model.Entity;

public enum QueryManager {
	
	INSTANCE;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	private Semaphore queryLock = new Semaphore(1,true);
	
	public <T extends Entity> Set<T> query(String dbPath, IDataAccessObject<T> dao, String queryString, Object ... params) {
		
		try {
			this.queryLock.acquire();
		} catch (InterruptedException e) {
			this.logger.error("An error occured acquiring the query lock.",e);
			return Collections.emptySet();
		}

		//if there is a problem (Yo! I'll solve it.) return the empty set
		if(dao == null || queryString == null) return Collections.emptySet();
		
		//build test result set
		Set<T> results = new TreeSet<T>();
		
		//get connection
		Connection connection = ConnectionManager.INSTANCE.getConnection(dbPath);
	
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
				} catch (NonWritableChannelException nwce) {
					//can't query
					this.logger.warn("Could not query database, no retries: {}", nwce.getMessage());
					//release lock
					this.queryLock.release();
					//return empty
					return Collections.emptySet();
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
		
		this.queryLock.release();
		
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
