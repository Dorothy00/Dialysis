package com.reader.dialysis;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Connection;
        import java.sql.Statement;

public class Test {
    static Connection con; // 声明Connection对象
    static Statement sql; // 声明Statement对象
    static ResultSet res;
    public static StringBuilder sb = new StringBuilder();

    public Connection getConnection() { // 连接数据库方法
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/test", "root", "0000");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return con; // 返回Connection对象
    }

    // ///////////////

    public static void selectData() {

        Test c = new Test();
        con = c.getConnection();

        try {
            sql =con.createStatement();

            res = sql.executeQuery("select  *  from  stu;");
            while (res.next()) {
                sb.append(res.getString("id") + "   " + res.getString("name")
                        + "\n");

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    // ////////

}// /Test