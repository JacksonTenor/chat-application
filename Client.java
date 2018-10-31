import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Socket s;
    static DataInputStream in;
    static DataOutputStream out;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args){
        System.out.println("What ip would you like to connect to?");
        String host = sc.nextLine();
        System.out.println("What port number?");
        int port = sc.nextInt();
        try{
            s = new Socket(host, port);
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }


        sendMessage.start();
        readMessage.start();

    }

    static Thread sendMessage = new Thread(new Runnable(){
        public void run(){
            while (true) {
                String msg = sc.nextLine();
                try {
                    out.writeUTF(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    static Thread readMessage = new Thread(new Runnable(){
        public void run(){
            while (true){
                try{
                    if(in.available() != 0) {
                        String msg = in.readUTF();
                        System.out.println(msg);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    });


}
