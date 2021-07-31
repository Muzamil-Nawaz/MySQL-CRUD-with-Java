package gbsproject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class GBSProject {
    static Connection con = null;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        menu();
    }
    public static  Connection makeConnection(){
        
        if(con==null){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gbs_demo", "root","");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return con;
    }
    public static void insertStd(){
        System.out.println("Enter Name:");
        
        String name = sc.next();
        System.out.println("Enter Father Name:");
        String fname = sc.next();
        System.out.println("Enter Contact:");
        String contact = sc.next();
        Connection con = makeConnection();
        try{
            PreparedStatement pst = con.prepareStatement("insert into crud(name,f_name,contact)values(?,?,?)");
            pst.setString(1, name);
            pst.setString(2, fname);
            pst.setString(3, contact);
            pst.execute();
            System.out.println( "Record Inserted Successfully.");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Some problem occured while inserting record.");
            e.printStackTrace();
        }
    }
    public static void printData(){
        Connection con=makeConnection();
        System.out.println("All Available Records:");
        String query = "Select * from crud";
        try{
            Statement st = con.createStatement();
            ResultSet rst =st.executeQuery(query);
            while(rst.next()){
                System.out.println(rst.getObject("id")+"\t"+rst.getObject("name")+"\t"+rst.getObject("f_name")+"\t"+rst.getObject("contact"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void deleteRecord(){
        Connection  con = makeConnection();
        try{
            System.out.println("Enter id to delete record:");
            int id = sc.nextInt();
            String query = "delete from crud where id =?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            pst.execute();
            System.out.println( "Record Deleted Successfully.");
                
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Some problem occured while deleting record.");
            e.printStackTrace();
        }
    }
    public static void updateRecord(){
        System.out.println("Enter id to update record:");
        int id = sc.nextInt();
        Connection con = makeConnection();
        try{
            PreparedStatement pst =con.prepareStatement("select * from crud where id =?");
            pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            if(rst.next()){
                System.out.println("Enter new name for record :");
                String name = sc.next();
                System.out.println("Enter new father name for record:");
                String fname = sc.next();
                System.out.println("Enter new contact for record:");
                String contact = sc.next();
                pst = con.prepareStatement("update crud set name =?,f_name=?,contact =? where id =?");
                pst.setString(1, name);
                pst.setString(2, fname);
                pst.setString(3, contact);
                pst.setInt(4, id);
                pst.executeUpdate();
                System.out.println( "Record Updated Successfully.");
            }
            else
                System.out.println("Id does not exist in record.");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Some problem occured while updating record.");
            e.printStackTrace();
        }
        
    }
    public static void menu(){
        printData();
        System.out.println("\nEnter 1 for inserting record\nEnter 2 for deleting record.\nEnter 3 for updating record\nEnter 4 for exiting");
        System.out.print("\nYour Choice:");
        int a = sc.nextInt();
        if(a==1){
            insertStd();
            menu();
        }
        else if(a==2){
            deleteRecord();
            menu();
        }
        else if(a==3){
            updateRecord();
            menu();
        }
        else if(a==4){
            System.out.println("Thank you for using our Crud System.");
            System.exit(0);
        }
        else{
            System.out.println("Invalid input. Try again.");
            menu();
        }
    }
}
