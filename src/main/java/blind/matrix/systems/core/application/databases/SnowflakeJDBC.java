package blind.matrix.systems.core.application.databases;

//change package as per your requirement.
//it will work without package also.
//package com.util;

//java.sql library for all connection objects

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
public class SnowflakeJDBC {

    private static String jdbcUrl = "jdbc:snowflake://ro54113.ap-south-1.aws.snowflakecomputing.com/";

    public static String selectSQL = "SELECT * FROM  SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.CUSTOMER LIMIT 10000";

    //default constructor
    public SnowflakeJDBC() {
    }

    private static Connection connection = getConnection();

    public Gson gson = new GsonBuilder().create();

    private static Connection getConnection() {
        //properties object
        Properties properties = new Properties();

        //setting properties
        properties.put("user", "RITESHRAJCHAWLA");
        properties.put("password", "1001.117777**Gbu");
        properties.put("account", "ro54113.ap-south-1.aws"); //account-id followed by cloud region.
        properties.put("warehouse", "SNOWFLAKE_LEARNING_WH");
        properties.put("db", "SNOWFLAKE_SAMPLE_DATA");
        properties.put("schema", "TPCH_SF1");
        properties.put("role", "ACCOUNTADMIN");
        try {
            return DriverManager.getConnection(jdbcUrl, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //entry main method
    public DataWrapper getData(String query, String numberOfResults) {
        DataWrapper wrapped = new DataWrapper();
        String executableQuery = "";
        if(StringUtils.isNotBlank(query)) {
            if(!StringUtils.containsIgnoreCase(query, " LIMIT ")) {
                if(query.contains(";")) {
                    executableQuery = query.replace(";", "").concat(" LIMIT ").concat(numberOfResults).concat(";");
                } else {
                    executableQuery = query.concat(" LIMIT ").concat(numberOfResults).concat(";");
                }

            } else {
                executableQuery = query;
            }
            List<Object> resultObj = new ArrayList<>();
            try {
                System.out.println("\tConnection established, connection id : " + connection);
                Statement stmt = connection.createStatement();
                System.out.println("\tGot the statement object, object-id : " + stmt);
                List<Map<String, Object>> dataList = new ArrayList<>();

                ResultSet resultSet = stmt.executeQuery(executableQuery);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                Map<String, String> columnMap = new LinkedHashMap<>();
                for (int zz = 1; zz <= resultSetMetaData.getColumnCount(); zz++) {
                    columnMap.put(resultSetMetaData.getColumnName(zz),
                            resultSetMetaData.getColumnTypeName(zz));
                }
                while (resultSet.next()) {
                    Map<String, Object> resultedColumnMap = new LinkedHashMap<>();
                    for (int aa = 1; aa <= resultSetMetaData.getColumnCount(); aa++) {
                        resultedColumnMap.put(resultSet.getMetaData().getColumnName(aa),
                                resultSet.getObject(aa));
                    }
                    dataList.add(resultedColumnMap);
                }
                wrapped.setColumnMap(columnMap);
                wrapped.setDataList(dataList);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return wrapped;
    }

    public class DataWrapper {

        Map<String, String> columnMap;

        List<Map<String, Object>> dataList;

        public Map<String, String> getColumnMap() {
            return columnMap;
        }

        public void setColumnMap(Map<String, String> columnMap) {
            this.columnMap = columnMap;
        }

        public List<Map<String, Object>> getDataList() {
            return dataList;
        }

        public void setDataList(List<Map<String, Object>> dataList) {
            this.dataList = dataList;
        }
    }
}

