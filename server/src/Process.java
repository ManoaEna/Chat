import java.io.*;
import java.net.Socket;

public class Process implements Runnable{

    private ChatPartage chatPartage;
    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String mode="",data="",name="", msg="";
    private int longueurChat=0;
    private int nbUsers =0;
    private ObjectOutputStream oos;
    private SRVRecepteur srvRecepteur;
    private Message message;

    public Process(Socket pSock, ChatPartage chat) throws IOException
    {
        message = new Message();
        sock = pSock;
        chatPartage=chat;
        oos= new ObjectOutputStream(sock.getOutputStream());
        srvRecepteur= new SRVRecepteur(chatPartage, sock, oos);
        srvRecepteur.start();
    }

    public void run(){

        boolean closeConnexion = false;
        while(!sock.isClosed())
        {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (srvRecepteur.isConnected())
            {
                if(chatPartage.getMessCount()!=longueurChat)
                {
                    message=null;
                    message=new Message();
                    String[] chat =chatPartage.getChatArray();
                    message.setMode("CHAT");
                    message.setChat(chat);
                    message.setUserList(chatPartage.getUsersArray());
                    try {
                        oos.writeObject(message);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    longueurChat=chatPartage.getMessCount();
                }

                if(chatPartage.getUsersCount()!=nbUsers)
                {
                    String[] user =chatPartage.getUsersArray();
                    message=null;
                    message=new Message();
                    message.setMode("USER");
                    message.setUserList(chatPartage.getUsersArray());
                    message.setChat(chatPartage.getChatArray());
                    try {
                        oos.writeObject(message);
                        oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    nbUsers=chatPartage.getUsersCount();
                }
            }
            if (!srvRecepteur.isAlive())
            {
                closeConnexion=true;
            }

            if(closeConnexion)
            {
                System.err.println("FERMETURE DE LA CONNEXION ! ");
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!chatPartage.isServON())
            {
                message=null;
                message=new Message();
                message.setMode("DECONNEXION");
                try {
                    oos.writeObject(message);
                    oos.flush();

                } catch (IOException e) {
                    System.err.println("Socket client fermé!");
                }

            }
        }

    }
}
