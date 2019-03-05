import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
 // Server class
public class ServerHandler {


// Vector to store active clients
    static Vector<ClientsHandler> ar = new Vector<>();
     
    // counter for clients
    static int i = 0;
    public static void main(String[] args) throws IOException 
    {
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);
        Socket s;
        // running infinite loop for getting
        // client request
        while (true) 
        {
            // Accept the incoming request
            s = ss.accept();
            System.out.println("New client request received : " + s);
            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            System.out.println("Creating a new handler for this client...");
            // Create a new handler object for handling this request.
            ClientsHandler mtch = new ClientsHandler(s,"client " + i, dis, dos);
            // Create a new Thread with this object.
            Thread t = new Thread(mtch);
            System.out.println("Adding this client to active client list");
            // add this client to active clients list
            ar.add(mtch);
            // start the thread.
            t.start();
            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;
        }
    }
}
 
// ClientHandler class
class ClientsHandler implements Runnable 
{
     
    private String username="";
    private String address="";
    private String email="";
    private String phone="";
    private String gender="";
    private String password="";
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    int ok = 0;
   // Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
     
    // constructor
    public ClientsHandler(Socket s, String name,DataInputStream dis, DataOutputStream dos) {
        System.out.println("Name :");
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }
 
    @Override
    public void run() {
 
        String received,msg;
        int flag =0 ;
        while (true) 
        {
            try
            {
                // receive the string
                received = dis.readUTF();
                System.out.println("Name got:"+received);
                if(flag==0)
                {
                   flag = 1;System.out.println("here i am :"+flag);
                }
                else if(flag == 1)
                { //register this user
                 if(username == "")
                     username = received;// System.out.println("Name :"+username);}
                 else if(address == "")
                     address = received;
                 else if(email == "")
                     email = received;
                 else if(phone == "")
                     phone = received;
                 else if(password == "")
                     password = received;
                 else if(gender == "")
                     gender = received;
                 else if(received.compareTo("logout") != 0){
                     System.out.println("Address :"+address);
                     try{
                        Class.forName("com.mysql.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudokudetails","root","");
                        pst = con.prepareStatement("SELECT * from userdetails where Email=?");
                        //System.out.println("Name :"+username);
                        pst.setString(1, email); //search for column
                        rs = pst.executeQuery();
                        if(rs.next()) //if you get a column means it exists.
                        {
                            ok=-1;
                        }
                        else
                        {
                            ok = 1;                  
                        }
                        if(ok == 1){
                        String registerUser = "insert into userdetails(Name,Email,Phone,Gender,Address,Password) VALUES(?,?,?,?,?,?)";
                        pst = con.prepareStatement(registerUser);
                        pst.setString(1, username);
                        pst.setString(2, email);
                        pst.setString(3, phone);
                        pst.setString(4, gender);
                        pst.setString(5, address);
                        pst.setString(6, password);
                        pst.executeUpdate();
                        }
                     }catch(Exception e){
                         System.out.println("Error : "+e);
                     }
                     if(ok == 1)
                     {
                      msg = "ok";
                      dos.writeUTF(msg);
                     }
                     else if(ok == -1)
                     {
                      msg = "already registed";
                      dos.writeUTF(msg);   
                     }
                   //  System.out.println("Okay :"+ok);
                 }
                 else{//System.out.println("Name :"+username);
                 //logout
                 this.isloggedin=false;
                 this.s.close();
                 break;
                 }
               }
                else//this is for login part
                {
                   if(email == "")
                     email = received;
                   else if(password == "")
                     email = received;
                   else if(received.compareTo("DONE") == 0){
                     System.out.println("Address :"+address);
                   try{
                     Class.forName("com.mysql.jdbc.Driver");
                     con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudokudetails","root","");                     pst = con.prepareStatement("select * from sudokudetails where Email=? and Password=?");
                     pst.setString(1, email);
                     pst.setString(2, password);
                     rs = pst.executeQuery();
                     if(rs.next())
                      ok=1;
                     else
                      ok=-1;
                     if(ok == 1)
                     {
                      msg = "ok";
                      dos.writeUTF(msg);
                     }
                     else if(ok == -1)
                     {
                      msg = "not found";
                      dos.writeUTF(msg);   
                     }
                     
                     }catch (Exception e) {
                        //Logger.getLogger(ClientsHandler.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Exception "+e);}
                    }
                   else if(received.equals("logout")){
                    this.isloggedin=false;
                    this.s.close();
                    break;}
                }
               // msg=scn.nextLine();
               // dos.writeUTF(msg);
               
               }
              catch (IOException e) {
                 
                e.printStackTrace();
            }
             
        }
        try
        {// closing resources
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }
   
}
