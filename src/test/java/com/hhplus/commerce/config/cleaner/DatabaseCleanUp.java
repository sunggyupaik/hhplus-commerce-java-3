package com.hhplus.commerce.config.cleaner;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseCleanUp implements InitializingBean {
    private static final String SYS_CONFIG_TABLE_NAME = "sys_config";
    private final List<String> tableNames = new ArrayList<>();

    private final EntityManager entityManager;
    private final DataSource dataSource;

    @Override
    public void afterPropertiesSet() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet rs = databaseMetaData.getTables(
                    null, null, "%", new String[]{"TABLE"}
            );
            while (rs.next()) {
                addExceptSysConfigTable(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addExceptSysConfigTable(ResultSet rs) throws SQLException {
        String tableName = rs.getString("TABLE_NAME");
        if (!tableName.equals(SYS_CONFIG_TABLE_NAME)) {
            tableNames.add(tableName);
        }
    }

    @Transactional
    public void truncateAllEntityH2() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery(
                    "ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void truncateAllEntityMySQL() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
