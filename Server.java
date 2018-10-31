import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server {
    public String name;
    private LinkedList<Connection> connections = new LinkedList<>();
    private Scanner sc;
    public Server (int port) throws IOException {
        sc = new Scanner(System.in);
        System.out.println("What would you like to name the server?");
        name = sc.nextLine();
        System.out.println("Your server ip is " + InetAddress.getLocalHost());
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Now listening on port: " + port);
        while(true){
            Socket s = ss.accept();
            System.out.println("New connection from " + s);
            Connection c = new Connection(s, this);
            connections.add(c);
            Thread t = new Thread(c);
            t.start();
            System.out.println("loop");
        }
    }

    void send(String s) {
        System.out.println("Sending in send");
        for(Connection c : connections){
            if(c.isActive()) {
                try {
                    c.out.writeUTF(s);
                    System.out.println("Sent: \"" + s + "\" to " + c.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        try {
            new Server(4004);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class Connection implements Runnable  {
    DataInputStream in;
    DataOutputStream out;
    private Socket s;
    private String name;
    private Boolean active;
    private Server server;

    Connection (Socket s, Server server) throws IOException{
        in = new DataInputStream(s.getInputStream());
        out = new DataOutputStream(s.getOutputStream());
        this.s = s;
        this.server = server;

    }

    public void run(){
        active = true;
        while(true){
            try{
                String str = in.readUTF();
                System.out.println(str);
                if(str.equals("!exit")){
                    active = false;
                    s.close();
                    break;
                }else{
                    System.out.println("Sending in run!");
                    server.send(str);
                }
            }catch(Exception e){
                e.printStackTrace();

            }
        }
    }

    public boolean isActive(){
        return active;
    }
}