import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SRVRecepteur extends Thread {
    private ChatPartage chatPartage;
    private Message message;
    private Socket sock;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String msg="";
    private boolean isConnected=false;

    public boolean isConnected() {
        return isConnected;
    }

    public SRVRecepteur(ChatPartage chatPartage, Socket sock, ObjectOutputStream oos) {
        this.chatPartage = chatPartage;
        this.sock = sock;
        try {
            ois= new ObjectInputStream(sock.getInputStream());
            this.oos= oos;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
        try {
            message = (Message) ois.readObject();
            switch (message.getMode()) {
                case "CONNECT":
                    boolean nomOK = chatPartage.checkUser(message.getUser());
                    if (nomOK) {
                        chatPartage.setUsersArray(message.getUser());
                        message.setMode("ACCEPTED");
                        message.setUserList(chatPartage.getUsersArray());
                        oos.writeObject(message);
                        oos.flush();
                        isConnected=true;
                        break;
                    } else
                        {
                        message.setMode("DENIED");
                        oos.writeObject(message);
                        oos.flush();
                        break;
                        }


                case "MSG":
                    chatPartage.setChatArray(message.getNewText());
                    break;

                case "DISCONNECT":
                    msg = message.getUser() + " s'est déconnecté! ";
                    chatPartage.setChatArray(msg);
                    chatPartage.delUsers(message.getUser());

                    break;
                default:
                    break;
            }
        } catch (SocketException e) {
            System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    }



}
