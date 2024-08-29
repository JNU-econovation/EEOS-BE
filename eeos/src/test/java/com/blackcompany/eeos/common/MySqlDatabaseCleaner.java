package com.blackcompany.eeos.common;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MySqlDatabaseCleaner implements DatabaseCleaner {
	private static final String FOREIGN_KEY_CHECK_FORMAT = "SET FOREIGN_KEY_CHECKS = %d";
	private static final String TRUNCATE_FORMAT = "TRUNCATE TABLE %s";
	private static final String AUTO_INCREMENT_FORMAT = "ALTER TABLE %s AUTO_INCREMENT = 1";

	@PersistenceContext private EntityManager entityManager;

	@Override
	@Transactional
	public void clear() {
		disableForeignKeyChecks();
		List<String> tableNames = getTableNames();
		truncate(tableNames);
		enableForeignKeyChecks();
	}

	private void disableForeignKeyChecks() {
		entityManager.createNativeQuery(String.format(FOREIGN_KEY_CHECK_FORMAT, 0)).executeUpdate();
	}

	private List<String> getTableNames() {
		String query =
				"SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()";
		return entityManager.createNativeQuery(query).getResultList();
	}

	private void enableForeignKeyChecks() {
		entityManager.createNativeQuery(String.format(FOREIGN_KEY_CHECK_FORMAT, 1)).executeUpdate();
	}

	private void truncate(List<String> tableNames) {
		for (String tableName : tableNames) {
			entityManager.createNativeQuery(String.format(TRUNCATE_FORMAT, tableName)).executeUpdate();
			entityManager
					.createNativeQuery(String.format(AUTO_INCREMENT_FORMAT, tableName))
					.executeUpdate();
		}
	}
}
