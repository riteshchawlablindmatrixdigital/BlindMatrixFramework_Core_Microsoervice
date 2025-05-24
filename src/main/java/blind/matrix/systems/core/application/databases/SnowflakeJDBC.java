package blind.matrix.systems.core.application.databases;

//change package as per your requirement.
//it will work without package also.
//package com.util;

//java.sql library for all connection objects

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

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
            SortedSet<Map<String, Object>> dataList = new TreeSet<>();

            ResultSet resultSet = stmt.executeQuery(selectSQL);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            SortedSet<String> columnSet = new TreeSet<>(String::compareTo);
            for(int zz=1;zz <= resultSetMetaData.getColumnCount();zz++) {
                columnSet.add(resultSetMetaData.getColumnName(zz));
            }


        } catch(Exception exception) {

        }
    }

    /*public enum ColumnType {

        Long, Int, Array, Blob, Integer, Byte, Short, Decimal, BigDecimal,
        Float, Date, Timestamp, Time, String, Double, Boolean, Object;



    }*/
}

