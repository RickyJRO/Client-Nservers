
import java.io.*;
import java.net.*;
import java.util.*;


public class Server extends Thread {
    private final static int PORT = 4242;
    private static ServerSocket serverSocket;
    private final Socket socket;
    private static int contagem= -1;
    public Server(Socket socket) {
        this.socket = socket;
    }

    private static int portano;
    public int getPortano() {
        return portano;
    }
    public static Object setPortano() {
        return null;
    }

    public static HashMap hashclt = new HashMap<>();

    public static HashMap hashno = new HashMap<>();

    private static int Hash(int numChar, int numNodes){
        return (numChar % numNodes);
    }

    static ArrayList<Integer> nos = new ArrayList<Integer>();

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object obj;
            Object noousocket = in.readObject();
            if (noousocket.equals("no")) {
                Object portadono = portano;
                out.writeObject(portadono);
                System.out.println("Novo Nó - [" + socket.getInetAddress().getHostName() + "@" +  " " + portadono + ":" + socket.getPort() + "]");
                  if (contagem < 9) {
                    contagem++;
                    nos.add(contagem);
                    out.writeObject(contagem);
                    String ipdono = socket.getInetAddress().getHostAddress();//To get the IP that is connected to this Principal node
                    String portadonoo = ipdono + " " + portadono;//ip + port

                    hashno.put(contagem, portadonoo);
                    portano++;
                } else {
                    System.out.println("Numero de nós excedido");
                }
            } else if (noousocket.equals("cliente")) {
                System.out.println("Novo Cliente - [" + socket.getInetAddress().getHostName() + "@" + " " + socket.getLocalPort() + ":" + socket.getPort() + "]");
                while (true) {
                    obj = in.readObject();
                    if (obj instanceof String) {

                        Socket nosSocket;
                        if (((String) obj).equalsIgnoreCase("Q")) {
                            System.out.println("Conexão Terminada - [" + socket.getInetAddress().getHostName() + "@" + socket.getInetAddress().getHostAddress() + " " + socket.getLocalPort() + ":" + socket.getPort() + "]");
                            break;

                        } else if (((String) obj).equalsIgnoreCase("T")) {
                            serverSocket.close();
                            break;

                        } else if (((String) obj).equalsIgnoreCase("R")) {
                            String chave = ((String) in.readObject());
                            int numeroletras = 0;
                            for (int i = 0; i < chave.length(); i++) {
                                int asciiValue = chave.charAt(i);
                                numeroletras = numeroletras + asciiValue;
                            }
                            int numeronoo = nos.size();
                            if (Hash(numeroletras, numeronoo) == 0) {
                                if (hashclt.containsKey(chave)) {
                                    out.writeObject("Chave existente.");
                                } else if (!hashclt.containsKey(chave)) {
                                    String valor = ((String) in.readObject());
                                    hashclt.put(chave, valor);
                                    System.out.println("Chave registada - "+ chave + " " + valor);
                                    out.writeObject("Chave registada com sucesso.");
                                } else {
                                    out.writeObject("Ocorreu um erro.");
                                }
                            }else {
                                int numerono = nos.size();
                                String IP = (String) hashno.get(Hash(numeroletras, numerono));
                                String[] IPPort = IP.split(" ");
                                String IpNode = IPPort[0];
                                int portNo = Integer.parseInt(IPPort[1]);


                                nosSocket = new Socket(IpNode, portNo);
                                ObjectOutputStream outnos = new ObjectOutputStream(nosSocket.getOutputStream());
                                ObjectInputStream innos = new ObjectInputStream(nosSocket.getInputStream());
                                String valor = ((String) in.readObject());
                                outnos.writeObject("R");
                                outnos.writeObject(chave);
                                outnos.writeObject(valor);
                                Object resposta = innos.readObject();
                                out.writeObject(resposta);
                            }
                        } else if (((String) obj).equalsIgnoreCase("C")) {
                            String chave = ((String) in.readObject());
                            int numeroletras = 0;
                            for (int i = 0; i < chave.length(); i++) {
                                int asciiValue = chave.charAt(i);
                                numeroletras = numeroletras + asciiValue;
                            }
                            int numeronoo = nos.size();
                            if (Hash(numeroletras, numeronoo) == 0) {
                                if (hashclt.containsKey(chave)) {
                                    String chavinha = (String) hashclt.get(chave);
                                    out.writeObject(chavinha);
                                } else if (!hashclt.containsKey(chave)) {
                                    out.writeObject("Chave inexistente");
                                } else {
                                    out.writeObject("Ocorreu um erro.");
                                }
                            }
                            else {
                                int numerono = nos.size();
                                String IP = (String) hashno.get(Hash(numeroletras, numerono));
                                String[] IPPort = IP.split(" ");
                                String IpNode = IPPort[0];
                                int portNo = Integer.parseInt(IPPort[1]);


                                nosSocket = new Socket(IpNode, portNo);
                                ObjectOutputStream outnos = new ObjectOutputStream(nosSocket.getOutputStream());
                                ObjectInputStream innos = new ObjectInputStream(nosSocket.getInputStream());
                                outnos.writeObject("C");
                                outnos.writeObject(chave);
                                Object resposta = innos.readObject();
                                out.writeObject(resposta);
                            }
                        } else if (((String) obj).equalsIgnoreCase("D")) {
                            String chave = ((String) in.readObject());
                            int numeroletras = 0;
                            for (int i = 0; i < chave.length(); i++) {
                                int asciiValue = chave.charAt(i);
                                numeroletras = numeroletras + asciiValue;
                            }
                            int numeronoo = nos.size();
                            if (Hash(numeroletras, numeronoo) == 0) {
                                if (hashclt.containsKey(chave)) {
                                    hashclt.remove(chave);
                                    out.writeObject("Item removido com sucesso.");
                                } else if (!hashclt.containsKey(chave)) {
                                    out.writeObject("Chave inexistente.");
                                } else {
                                    out.writeObject("Ocorreu um erro.");
                                }
                            }else {
                                int numerono = nos.size();
                                String IP = (String) hashno.get(Hash(numeroletras, numerono));
                                String[] IPPort = IP.split(" ");
                                String IpNode = IPPort[0];
                                int portNo = Integer.parseInt(IPPort[1]);

                                nosSocket = new Socket(IpNode, portNo);
                                ObjectOutputStream outnos = new ObjectOutputStream(nosSocket.getOutputStream());
                                ObjectInputStream innos = new ObjectInputStream(nosSocket.getInputStream());
                                outnos.writeObject("D");
                                outnos.writeObject(chave);
                                Object resposta = innos.readObject();
                                out.writeObject(resposta);
                            }
                        } else if (((String) obj).equalsIgnoreCase("L")) {
                            int porta = 4243;
                            for (int i = 0; i < nos.size() - 1 ; i++) {
                                nosSocket = new Socket("localhost",porta);
                                ObjectOutputStream outnos = new ObjectOutputStream(nosSocket.getOutputStream());
                                outnos.writeObject("L");
                                porta++;
                            }
                            Set entrySet = hashclt.entrySet();
                            for (Object o : entrySet) {
                                Map.Entry me = (Map.Entry) o;
                                System.out.println("Chave: " + me.getKey() +
                                        " " +
                                        " Valor: " + me.getValue());
                            }
                            }
                        } else {
                            System.out.println("Comando inesperado.");
                        }
                    }
                }

            socket.close();
            out.close();
            in.close();

            } catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

    public static void main(String[] args) {
        hashno.put(0,4242);
        nos.add(0);
        contagem++;
        try {
            portano = 4243;
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor Iniciado");
            System.out.println("Olá, sou o nó 0");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                new Server(socket).start();
            } catch (IOException ioe) {
                if (serverSocket.isClosed()) {
                    System.out.println("Servidor Terminado");
                } else {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
