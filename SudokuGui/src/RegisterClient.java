
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 *
 * @author shubhrA
 */
class RegisterClient {
static String name,address,password,email,phone,gender;  
int ok;
final static int ServerPort = 1234;
RegisterClient(String name,String address,String password,String email,String phone,String gender) {
    this.name = name;
    this.address = address;
    this.gender = gender;
    this.phone = phone;
    this.password = password;
    this.email = email;
    ok = 0;
    
}    

void  connectToServer() throws UnknownHostException, IOException
        {
            // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");
         
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
         
        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
 
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() 
        {
            @Override
            public void run() {
                while (true) {
                   
                    try {
                        // write on the output stream
                        dos.writeUTF("REGISTER");
                        dos.writeUTF(name);
                        dos.writeUTF(address);
                        dos.writeUTF(email);
                        dos.writeUTF(phone);
                        dos.writeUTF(password);
                        dos.writeUTF(gender);
                        dos.writeUTF("done");
                        String msg = dis.readUTF();
                        if(msg.equals("ok"))
                        {ok = 1;}//System.out.println("okay mil gya");}
                        else
                          ok = -1;
                        if(ok !=0)
                        {
                        System.out.println("okay mil gya"+ok);  
                        dos.writeUTF("logout");
                        
                        }
                    } catch (IOException e) {
                        
                         //System.out.println("waiting for ok");
                         
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
