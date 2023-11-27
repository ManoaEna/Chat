import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server extends javax.swing.JFrame{

        ChatPartage chat = new ChatPartage();
        private int port;
        private String host;
        ArrayList clients = new ArrayList();
        private ServerSocket server = null;
        private boolean isRunning = true;

    public static void main(String[] args) {
        String host = "localhost";
        int port = 600;
        Server ts = new Server(host, port);
        ts.setVisible(true);
        ts.setTitle("Server");
        ts.open();

        System.out.println("Serveur initialisé.");
    }
        public Server(String pHost, int pPort){
            host = pHost;
            port = pPort;
            try {
                server = new ServerSocket(port, 100, InetAddress.getByName(host));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            initComponents();
        }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToggleButton1.setText("Server ON");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Clients:");

        jLabel2.setText("XXXX");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jToggleButton1)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel2)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        if(chat.isServON())
        {
            jToggleButton1.setText("Server OFF");
            chat.setServON(false);
        }else
        {
            jToggleButton1.setText("Server ON");
            chat.setServON(true);
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt)
    {
        closing();
    }

    private void closing(){
        chat.setServON(false);
    }

    public void open(){

            Thread t = new Thread(new Runnable(){
                public void run(){

                    while(isRunning == true){
                        jLabel2.setText(String.valueOf(chat.getUsersCount()));
                        try {
                            server.setSoTimeout(1000);
                            Socket socketClient = server.accept();
                            System.out.println("Connexion client reçue.");
                            Thread t = new Thread(new Process(socketClient,chat));
                            t.start();
                            clients.add(socketClient);
                        } catch (IOException e) {

                        }
                    }

                    try {
                        closing();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        server.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                        server = null;
                    }
                }
            });

            t.start();
        }
        public void close(){
            isRunning = false;
        }

        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JToggleButton jToggleButton1;
    }


