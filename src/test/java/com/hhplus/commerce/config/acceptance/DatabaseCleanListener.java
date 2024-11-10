package com.hhplus.commerce.config.acceptance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@TestComponent
public class DatabaseCleanListener extends AbstractTestExecutionListener {
    private final List<String> tableNames = new ArrayList<>();

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private void findDatabaseTableNames() {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                final String tableName = resultSet.getString(1);
                tableNames.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void truncateMySQL() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", tableName)).executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    private void truncateH2() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", tableName)).executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void clearH2() {
        if (tableNames.isEmpty()) {
            findDatabaseTableNames();
        }
        entityManager.clear();
        truncateH2();
    }

    @Transactional
    public void clearMySQL() {
        if (tableNames.isEmpty()) {
            findDatabaseTableNames();
        }
        entityManager.clear();
        truncateMySQL();
    }
}
