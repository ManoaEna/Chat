import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ChatPartage {
    private ArrayList<String> usersArray = new ArrayList<String>();
    private ArrayList<String> chatArray = new ArrayList<String>();
    private Semaphore cookie = new Semaphore(1);
    private int messCount =0;
    private String msg="";
    private String lastUser="";
    private boolean ServON =true;


    public String[] getUsersArray()
    {
        String[] user = new String[usersArray.size()];
        int i=0;
        for (String mess: usersArray) {
            user[i]=mess;
            i++;
        }
        return user;
    }

    public synchronized void setUsersArray(String newUser)
    {
        try {
            cookie.acquire();
            usersArray.add(newUser);
            this.lastUser=newUser;
            cookie.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void delUsers(String delUser)
    {
        int i = usersArray.indexOf(delUser);
        usersArray.remove(i);
    }

    public String[] getChatArray()
    {
        String[] chat = new String[chatArray.size()];
        int i=0;
        for (String mess: chatArray) {
            chat[i]=mess;
            i++;
        }
        return chat;
    }

    public  String getLastMess()
    {
        return msg;
    }
    public String getLastUser()
    {
        return lastUser;
    }

    public synchronized void setChatArray(String msg)
    {
        try
        {
            cookie.acquire();
            this.chatArray.add(msg);
            this.msg=msg;
            messCount++;
            cookie.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUser(String newUser)
    {
        boolean ok=true;
        if((usersArray.contains(newUser))|(newUser.equals(""))){
            ok=false;
        }
        return ok;
    }

    public int getMessCount()
    {
        return messCount;
    }
    public int getUsersCount()
    {
        return usersArray.size();
    }

    public boolean isServON() {
        return ServON;
    }

    public void setServON(boolean servON) {
        ServON = servON;
    }
}
