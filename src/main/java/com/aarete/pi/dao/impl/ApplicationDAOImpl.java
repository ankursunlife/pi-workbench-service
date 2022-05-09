package com.aarete.pi.dao.impl;

import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.dao.ApplicationDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

import static com.aarete.pi.util.TableUtil.getColumns;
import static com.aarete.pi.util.TableUtil.getColumnsStr;
import static com.aarete.pi.util.TableUtil.getColumnsStrExceptId;

@Repository
public class ApplicationDAOImpl implements ApplicationDAO {

	public static final String DB_QUERY_SUCCESSFULLY_EXECUTED = "db query successfully executed.";
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	final
	JdbcTemplate jdbcTemplate;

	public ApplicationDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<IdNameBean> findAll(String name) {
		String sqlQuery = String.format("SELECT * FROM %s", name);
		log.debug(sqlQuery);
		List<String> columns = getColumns(name);
		List<IdNameBean> beanList = jdbcTemplate.query(sqlQuery,
				(rs, rowNum) -> new IdNameBean(rs.getString(columns.get(0)), rs.getString(columns.get(1)),
						rs.getString(columns.get(2)), false));
		log.info(DB_QUERY_SUCCESSFULLY_EXECUTED);
		return beanList;
	}

	@Override
	public IdNameBean findById(String name, int id) {
		List<String> colNames = getColumns(name);
		String sqlQuery = String.format("SELECT * FROM %s WHERE %s=?", name, colNames.get(0));
		log.debug(sqlQuery);
		IdNameBean bean = jdbcTemplate.queryForObject(sqlQuery, new Object[]{id}, new int[]{Types.INTEGER},
				(rs, rowNum) -> new IdNameBean(rs.getString(colNames.get(0)), rs.getString(colNames.get(1)),
						rs.getString(colNames.get(2)), false));
		log.info(DB_QUERY_SUCCESSFULLY_EXECUTED);
		return bean;
	}

	@Override
	public int deleteById(String name, String id) {
		List<String> colNames = getColumns(name);
		String sqlQuery = String.format("DELETE FROM %s WHERE %s=?", name, colNames.get(0));
		log.debug(sqlQuery);
		int count = jdbcTemplate.update(sqlQuery, id);
		log.info(DB_QUERY_SUCCESSFULLY_EXECUTED);
		return count;
	}

	@Override
	public List<IdNameBean> save(String name, List<IdNameBean> idNameBeans) {
		for (IdNameBean idNameBean : idNameBeans) {
			String sqlQuery;
			if (idNameBean.getId() != null && !idNameBean.getId().isBlank()) {
				sqlQuery = String.format("INSERT INTO %s (%s) VALUES (?, ?, ?)", name, getColumnsStr(name));
				log.debug(sqlQuery);
				jdbcTemplate.update(sqlQuery, idNameBean.getId(), idNameBean.getName(), idNameBean.getDesc());
			} else {
				sqlQuery = String.format("INSERT INTO %s (%s) VALUES (?, ?)", name, getColumnsStrExceptId(name));
				log.debug(sqlQuery);
				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(connection -> {
					PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{getColumns(name).get(0)});
					ps.setString(1, idNameBean.getName());
					ps.setString(2, idNameBean.getDesc());
					return ps;
				}, keyHolder);
				Number key = keyHolder.getKey();
				if(key != null) {
					idNameBean.setId(String.valueOf(key.intValue()));
				}
			}
			log.info(DB_QUERY_SUCCESSFULLY_EXECUTED);
		}
		// TODO return count
		return idNameBeans;
	}

	@Override
	public int update(String name, IdNameBean masterTableRequest) {
		List<String> columns = getColumns(name);
		String sqlQuery = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", name, columns.get(1), columns.get(2), columns.get(0));
		log.debug(sqlQuery);
		int count = jdbcTemplate.update(sqlQuery, masterTableRequest.getName(), masterTableRequest.getDesc(), masterTableRequest.getId());
		log.info(DB_QUERY_SUCCESSFULLY_EXECUTED);
		return count;
	}
		
}