package blind.matrix.systems.core.application.databases;

//change package as per your requirement.
//it will work without package also.
//package com.util;

//java.sql library for all connection objects

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.*;
import java.util.*;

//class definition
public class SnowflakeJDBC {

    //default constructor
    public SnowflakeJDBC() {
    }

    //entry main method
    public static void main(String[] args) {

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

        //change this below URL as per your snowflake instance
        String jdbcUrl = "jdbc:snowflake://ro54113.ap-south-1.aws.snowflakecomputing.com/";

        //change this select statement, but make sure the logic below is hard coded for now.
        String selectSQL = "SELECT * FROM  SNOWFLAKE_SAMPLE_DATA.TPCH_SF1.CUSTOMER LIMIT 10";

        //try-catch block
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, properties);
            System.out.println("\tConnection established, connection id : " + connection);
            Statement stmt = connection.createStatement();
            System.out.println("\tGot the statement object, object-id : " + stmt);
            ResultSet rs = stmt.executeQuery(selectSQL);
            final Map<String, String> columnNameTypeMap = new LinkedHashMap<>();
            //final Map<String, Object> resultDataMap = new LinkedHashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            String columnName = null;
            for(int z = 0; z < metaData.getColumnCount(); z++) {
                columnNameTypeMap.put(metaData.getColumnName(z + 1), metaData.getColumnTypeName(z + 1));
            }

            ResultSetHandler<Map<String, List<String>>> handler = (ResultSetHandler<List<Map<String, Object>>>)
                    resultSet -> {
                List<Map<String, String>> result = new ArrayList<>();
                int kk = 0;
                columnNameTypeMap.keySet().stream().forEachOrdered(columnNameAsKey -> {
                    final Map<String, Object> resultDataMap = new LinkedHashMap<>();
                    String columnTypeName = columnNameTypeMap.get(columnNameAsKey);
                    Object data = null;
                    try {
                        while (resultSet.next()) {
                            if(columnTypeName.equalsIgnoreCase(ColumnType.Long.name())) {
                                data = resultSet.getLong(kk);
                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.BigDecimal.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Integer.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Int.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Array.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Blob.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Time.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Date.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Time.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Decimal.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Boolean.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.String.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Short.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Object.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Double.name())) {

                            } else if(columnTypeName.equalsIgnoreCase(ColumnType.Float.name())) {

                            }

                            Object empCity = resultSet.getString("empCity");
                            String empName = resultSet.getString("empName");
                            result.computeIfAbsent(empCity, data -> new ArrayList<>()).add(empName);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    }
                });

                return result;
            };


            QueryRunner run = new QueryRunner();
            Map<String, List<String>> valueMap = run.query(connection, "SELECT * FROM employee", handler);
            System.out.println("\tGot the result set object, object-id : " + rs);
            System.out.println("\t----------------------------------------");

            while(rs.next()) {
                //following rs.getXXX should also change as per your select query
                System.out.println(" \tEmployee ID: " + rs.getInt("ID"));
                System.out.println(" \tEmployee Age: " + rs.getInt("AGE"));
                System.out.println(" \tEmployee First: " + rs.getString("FIRST"));
                System.out.println(" \tEmployee Last: " + rs.getString("LAST"));
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }

        System.out.println("\t----------------------------------------");
        System.out.println("\tProgram executed successfully");
    }

    public enum ColumnType {

        Long, Int, Array, Blob, Integer, Byte, Short, Decimal, BigDecimal,
        Float, Date, Timestamp, Time, String, Double, Boolean, Object;



    }
}

