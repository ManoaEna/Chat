import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Client extends javax.swing.JFrame
{
    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String name="";
    private Message messageClient;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean nomOk=false;
    private DefaultListModel<String> listModel;
    private String chat="Bienvenue";

    public static void main(String[] args)
    {
        String host = "localhost";
        int port = 600;
        Client client= new  Client(host, port);
        client.setVisible(false);
        client.run();
    }

    public Client(String host, int port)
    {
        messageClient = new Message();
        initComponents();
        try {
            connexion = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            oos = new ObjectOutputStream(connexion.getOutputStream());
            ois = new ObjectInputStream(connexion.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents()
    {
        jScrollPane1 = new javax.swing.JScrollPane();
        textChat = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        textEntry = new javax.swing.JTextField();
        btnEnter = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        textChat.setEditable(false);
        textChat.setColumns(20);
        textChat.setLineWrap(true);
        textChat.setRows(5);
        textChat.setAutoscrolls(true);
        jScrollPane1.setViewportView(textChat);
        textChat.setText(chat);


        jScrollPane2.setViewportView(jList1);

        textEntry.setToolTipText("message");
        textEntry.setName(""); // NOI18N
        textEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textEnterActionPerformed(evt);
            }
        });

        btnEnter.setText("SEND");
        btnEnter.setToolTipText("");
        btnEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                        .addComponent(textEntry))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                                        .addComponent(btnEnter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(textEntry))
                                .addContainerGap())
        );

        pack();
    }

    public void run()
    {
        while (!nomOk)
        {
            try {
                    name = JOptionPane.showInputDialog("Username");
                    if(name==null){System.exit(0);}
                    messageClient.setMode("CONNECT");
                    messageClient.setUser(name);
                    oos.writeObject(messageClient);
                    oos.flush();
                    messageClient = (Message) ois.readObject();
                    switch (messageClient.getMode())
                    {
                        case "ACCEPTED":
                            setTitle(name);
                            this.setVisible(true);
                            nomOk = true;
                            majListUser();
                            break;

                        case "DENIED":
                            JOptionPane.showMessageDialog(this, "Username Invalide!");
                            break;

                        default:
                            break;
                    }
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        while(true)
        {
            try {
                messageClient = new Message();
                messageClient=(Message)ois.readObject();
                switch (messageClient.getMode())
                {
                    case "CHAT":
                        messageClient.setChat(messageClient.getChat());
                        majChat();

                        break;
                    case "DECONNEXION":
                        deco();
                        this.setVisible(false);
                        System.exit(0);

                        break;
                    case "USER":
                        majListUser();
                        break;
                    default:
                        break;
                }
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt)
    {
        deco();
    }

    private void deco(){
        try {
            messageClient.setMode("DISCONNECT");
            messageClient.setUser(name);
            oos.writeObject(messageClient);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void btnEnterActionPerformed(java.awt.event.ActionEvent evt) {
        envoiText();
    }

    private void textEnterActionPerformed(java.awt.event.ActionEvent evt) {
        envoiText();
    }

    private void envoiText()
    {
        try {
            messageClient.setMode("MSG");
            messageClient.setUser(name);
            messageClient.setNewText(name+" : "+textEntry.getText());
            oos.writeObject(messageClient);
            oos.flush();
            textEntry.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void majChat()
    {
        textChat.setText(messageClient.getText());
        messageClient=null;
    }

    private void majListUser()
    {
        listModel = new DefaultListModel<>();
        for (int i=0;i<messageClient.getUserNb();i++)
        {
            listModel.addElement(messageClient.getUserAt(i));
        }
        jList1.setModel(listModel);
    }

    private javax.swing.JButton btnEnter;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea textChat;
    private javax.swing.JTextField textEntry;
}