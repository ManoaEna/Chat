import java.io.Serializable;

public class Message implements Serializable {
    private String user, mode, newText;
    private String[] chat, userList;

    public Message(String user, String mode, String[] chat, String[] userList) {
        this.user = user;
        this.mode = mode;
        this.chat = chat;
        this.userList = userList;
    }
    public Message() {
        this.user = null;
        this.mode = null;
        this.chat = null;
        this.userList = null;
    }

    public String getUser() {
        return user;
    }

    public String getUserAt(int i) {
        return userList[i];
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String[] getChat() {

        return chat;
    }
    public String getText() {
        String text ="";
        for (int i=0; i<chat.length;i++)
        {
            text +=chat[i]+"\n";
        }
        return text;
    }

    public void setChat(String[] chat) {
        this.chat = chat;
    }

    public String[] getUserList() {
        return userList;
    }

    public int getUserNb() {
        return userList.length;
    }

    public void setUserList(String[] userList) {
        this.userList = userList;
    }

    public void setNewText(String text){
        this.newText=text;
    }

    public String getNewText() {
        return newText;
    }
}
