package com.project.visionguardiancommander;


import java.sql.*;
import java.util.ArrayList;



class Database {
    public int code;

    public String firstName;
    public String lastName;
    public int registrationId;
    public int age;
    public String sex;

    public final String databaseName = "VisionGuardianCommander";
    public final String databaseUserName = "root";
    public final String databasePassword = "";

    public Connection con;

    public boolean init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try {
                this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, databaseUserName,
                        databasePassword);
            } catch (SQLException e) {

                System.out.println("Error: Database Connection Failed!!! Please check the connection Setting...");

                return false;

            }

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

            return false;
        }

        return true;
    }

    public void insert() {
        String sql = "INSERT INTO face_bio (code, first_name, last_name, registration_id, age , sex) VALUES (?, ?, ?, ?,?,?)";

        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(sql);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {

            statement.setInt(1, this.code);
            statement.setString(2, this.firstName);

            statement.setString(3, this.lastName);
            statement.setInt(4, this.registrationId);
            statement.setInt(5, this.age);
            statement.setString(6, this.sex);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new face data was inserted successfully!");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ArrayList<String> getUser(int inCode){

        ArrayList<String> user = new ArrayList<String>();

        try {

            Database app = new Database();

            String sql = "select * from face_bio where code=" + inCode + " limit 1";

            Statement s = con.createStatement();

            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {

                /*
                 * app.setCode(rs.getInt(2)); app.setFname(rs.getString(3));
                 * app.setLname(rs.getString(4)); app.setReg(rs.getInt(5));
                 * app.setAge(rs.getInt(6)); app.setSec(rs.getString(7));
                 */

                user.add(0, Integer.toString(rs.getInt(2)));
                user.add(1, rs.getString(3));
                user.add(2, rs.getString(4));
                user.add(3, Integer.toString(rs.getInt(5)));
                user.add(4, Integer.toString(rs.getInt(6)));
                user.add(5, rs.getString(7));

                /*
                 * System.out.println(app.getCode());
                 * System.out.println(app.getFname());
                 * System.out.println(app.getLname());
                 * System.out.println(app.getReg());
                 * System.out.println(app.getAge());
                 * System.out.println(app.getSec());
                 */

                // nString="Name:" + rs.getString(3)+" "+rs.getString(4) +
                // "\nReg:" + app.getReg() +"\nAge:"+app.getAge() +"\nSection:"
                // +app.getSec() ;

                // System.out.println(nString);
            }

            con.close(); // closing connection
        } catch (Exception e) {
            e.getStackTrace();
        }
        return user;
    }

    public void db_close() throws SQLException {
        try {
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getReg() {
        return registrationId;
    }

    public void setReg(int registrationId) {
        this.registrationId = registrationId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}

