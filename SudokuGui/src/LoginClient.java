
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 *
 * @author shubhra
 */
class LoginClient {
    String email;
    String password;
    int ok;
    LoginClient(String email,String password){
        this.email = email;
        this.password = password;
    }
    final static int ServerPort = 1234;
    void connectToServer() throws UnknownHostException, IOException{
         InetAddress ip = InetAddress.getByName("localhost");
         
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
         
        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run(){
                while(true){
                    try{
                        dos.writeUTF("LOGIN");
                        dos.writeUTF(email);
                        dos.writeUTF(password);
                        dos.writeUTF("done");
                        String msg = dis.readUTF();
                        if(msg.equals("ok"))
                         {ok = 1;}//System.out.println("okay mil gya");}
                        else
                          ok = -1;
                        if(ok !=0)
                        {
                        System.out.println("okay mil gya"+ok);  
                        //take the sudoku and solution matrix as input from the server
                        
                        
                        //dos.writeUTF("logout");
                        }
                    }
                    catch(IOException e){
                         e.printStackTrace();
                        break; 
                    }
                    
                }
            }
            
        });
          
        sendMessage.start();
        //readMessage.start();
    
    }
    public static void main(String args[]) throws UnknownHostException, IOException 
    {
        
        
    }  
}
