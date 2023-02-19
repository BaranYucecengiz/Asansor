package com.by.myapplication;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.sql.*;

public class DB {
    private final Connection connect;

    public DB() throws SQLException, ClassNotFoundException {
        String CON_URL = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12598266?zeroDateTimeBehavior=convertToNull";
        String CON_USER = "sql12598266";
        String CON_PASSWORD = "vDZ9DK9rxI";
        this.connect = DriverManager.getConnection(CON_URL, CON_USER, CON_PASSWORD);
        Class.forName("com.mysql.jdbc.Driver");
    }

    public List<String> getResponsibleElevatorIds(String userId) throws SQLException {
        Statement statement = this.connect.createStatement();
        String query = "SELECT responsible_elevators from User WHERE id = %s";
        ResultSet resultSet = statement.executeQuery(String.format(query, userId));

        while (resultSet.next()) {
            String Elevators = resultSet.getString("responsible_elevators");

            if (Elevators == null)
                return this.getAllElevatorIds();
            else
                return Arrays.asList(Elevators.split("-"));
        }
        return null;
    }

    private List<String> getAllElevatorIds() throws SQLException {
        List<String> elevatorIds = new ArrayList<>();

        Statement statement = this.connect.createStatement();
        String query = "SELECT id from Elevator";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            elevatorIds.add(id);
        }
        return elevatorIds;
    }

    public Map<String, Elevator> getAllResponsibleElevators(List<String> idList, boolean onlyMetaData) throws SQLException {
        Map<String, Elevator> elevators = new HashMap<>();

        String idListString = "";
        for (String id : idList) {
            idListString += String.format("'%s', ", id);
        }
        idListString = idListString.substring(0, idListString.length() - 2);

        Statement statement = this.connect.createStatement();
        String query = "SELECT * from Elevator WHERE id IN ( " + idListString + ") ";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            Elevator elevator = new Elevator("1","A");

            elevator.setId(resultSet.getString("id"));
            elevator.setAddress(resultSet.getString("address"));
            elevator.setOwner(resultSet.getString("owner"));
            elevator.setCommunicationNumber(resultSet.getString("communication_number"));
            elevator.setSetupDate(resultSet.getString("setup_date"));
            elevator.setLastConfigurationDate(resultSet.getString("last_configuration_date"));

            if (!onlyMetaData) {
                elevator.setP(this.getElevatorPropertyMap(elevator.getId(), "P"));
                elevator.setD(this.getElevatorPropertyMap(elevator.getId(), "D"));
                elevator.setN(this.getElevatorPropertyMap(elevator.getId(), "N"));
            }

            elevators.put(elevator.getId(), elevator);
        }

        return elevators;
    }

    private List<String> getColumnNamesOfSpecificTable(ResultSet resultSet) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount(); //number of column

        for (int i = 1; i <= count; i++)
            columnNames.add(metaData.getColumnLabel(i));

        return columnNames;
    }

    public Elevator getElevatorWithId(String elevatorId) throws SQLException {
        Elevator elevator = new Elevator(elevatorId);
        elevator.setP(this.getElevatorPropertyMap(elevatorId, "P"));
        elevator.setD(this.getElevatorPropertyMap(elevatorId, "D"));
        elevator.setN(this.getElevatorPropertyMap(elevatorId, "N"));

        Statement statement = this.connect.createStatement();
        String query = "SELECT * from Elevator WHERE id = " + elevatorId;
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            elevator.setAddress(resultSet.getString("address"));
            elevator.setOwner(resultSet.getString("owner"));
            elevator.setCommunicationNumber(resultSet.getString("communication_number"));
            elevator.setSetupDate(resultSet.getString("setup_date"));
            elevator.setLastConfigurationDate(resultSet.getString("last_configuration_date"));
        }

        return elevator;
    }

    private Map<String, Integer> getElevatorPropertyMap(String elevatorId, String PDN) throws SQLException {
        Map<String, Integer> propertyMap = new HashMap<>();

        Statement statement = this.connect.createStatement();
        String query = "SELECT * from Elevator_%s WHERE id = %s";
        ResultSet resultSet = statement.executeQuery(String.format(query, PDN, elevatorId));

        List<String> columnNames = this.getColumnNamesOfSpecificTable(resultSet);
        columnNames.remove("id");

        while (resultSet.next()) {
            for (String columName : columnNames) {
                if (resultSet.getString(columName) == null)
                    propertyMap.put(columName, null);
                else
                    propertyMap.put(columName, resultSet.getInt(columName));
            }
        }

        return propertyMap;
    }

    public void updateElevatorWithPropertyMap(String elevatorId, Map<String, Integer> propertyMap, String PDN) throws SQLException {
        String setKeyValString = "";
        for (Map.Entry<String, Integer> entry : propertyMap.entrySet()) {
            setKeyValString += String.format("%s = %d , ", entry.getKey(), entry.getValue());
        }
        setKeyValString = setKeyValString.substring(0, setKeyValString.length() - 2);

        String query = String.format("UPDATE Elevator_%s SET %s WHERE id = %s", PDN, setKeyValString, elevatorId);
        System.out.println(query);
        PreparedStatement preparedStatement = this.connect.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

}
