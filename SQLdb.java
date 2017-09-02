import java.io.*;
import java.sql.*;
import java.io.File;
public class SQLdb{
    private Connection conn;
    public SQLdb(String chan){
        File temp=new File(System.getenv("appdata")+"\\SharqBot\\db\\"+chan+".db");
        boolean newDB;
        if(temp.exists()==false){
            System.out.println("Must create new database");
            newDB=true;
        }
        else{
            newDB=false;
        }
        try{
            this.conn=null;
            connectDB(chan);
            if(this.conn!=null){
                if(newDB){
                    initDB();
                }
                else{
                    ResultSet rs=eQ("SELECT name FROM sqlite_master WHERE type=\"table\"");
                    while(rs.next()){// read the result set
                        System.out.println("name=" + rs.getString("name"));;
                    }
                }
            }
        }
        catch(SQLException e){
            System.out.println("Error initiating database for channel "+chan);
            System.err.println(e);
        }
    }
    public void connectDB(String chan){
        try{
            Class.forName("org.sqlite.JDBC");
            this.conn=DriverManager.getConnection("jdbc:sqlite:"+System.getenv("appdata")+"\\SharqBot\\db\\"+chan+".db");
            System.out.println("Connection to SQLite has been established.");
            //DatabaseMetaData meta=this.conn.getMetaData();
        }
        catch(SQLException e){
            System.out.println("SQLException 1 caught");
            System.err.println(e);
        }
        catch(ClassNotFoundException e){
            System.out.println("ClassNotFoundException caught");
            System.err.println(e);
        }
    }
    public void closeDB(){
        try{
            if(this.conn!=null){
                this.conn.close();
                System.out.println("Connection Closed");
            }
        }
        catch(SQLException e){
            System.out.println("SQLException caught when attempting to close connection");
            System.err.println(e);
        }
    }
    public void initDB(){
        eU("CREATE TABLE config (name string primary key, pokemon integer, league integer, overwatch integer, roomid string, pRate integer, pName string)");
        eU("CREATE TABLE deaths (name string primary key, count integer)");
        eU("CREATE TABLE users (twitchID string primary key, twitchName string, subCount integer, subTime integer, cRolls integer, hRolls integer, balls integer, exp integer, points integer, champ string, hero string, p1 string, p1lvl integer, p2 string, p2lvl integer, p3 string, p3lvl integer, p4 string, p4lvl integer, p5 string, p5lvl integer, p6 string, p6lvl integer)");
    }
    public void pData(){
        try{
            ResultSet rs=eQ("select * from users");
            while(rs.next()){
                System.out.println("Twitch Name=" + rs.getString("twitchName"));
                System.out.println("Twitch ID=" + rs.getInt("twitchID"));
            }
        }
        catch(SQLException e){
            System.out.println("Error trying to send print out the database");
            System.err.println(e);
        }
    }
    public int searchStr(String table, String col, String str){
        int c=0;
        try{
            ResultSet rs=eQ(String.format("SELECT * FROM %s WHERE %s='%s'",table,col,str));
            while(rs.next()){// read the result set
                c++;
            }
        }
        catch(SQLException e){
            System.out.println(String.format("Error searching for %s in table %s column %s",str,table,col));
            System.err.println(e);
        }
        return c;
    }
    public int searchInt(String table, String col, int num){
        int c=0;
        try{
            ResultSet rs=eQ(String.format("SELECT * FROM %s WHERE %s=%d",table,col,num));
            while(rs.next()){// read the result set
                c++;
            }
        }
        catch(SQLException e){
            System.out.println(String.format("Error searching for %d in table %s column %s",num,table,col));
            System.err.println(e);
        }
        return c;
    }
    public ResultSet eQ(String s){
        try{
            Statement st=this.conn.createStatement();
            System.out.println("EXECUTING QUERY: "+s);
            ResultSet rs=st.executeQuery(s);
            return rs;
        }
        catch(SQLException e){
            System.out.println("Error trying to execute a query");
            System.err.println(e);
            return null;
        }
    }
    public void eU(String s){
        try{
            Statement st=this.conn.createStatement();
            System.out.println("EXECUTING UPDATE: "+s);
            System.out.println(st.executeUpdate(s));
        }
        catch(SQLException e){
            System.out.println("Error trying to excute an update");
            System.err.println(e);
        }
    }
}