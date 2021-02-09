import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;


public class NoParticipante {
    private static ServerSocket serverSocket;

    public static HashMap hashclt = new HashMap<>();
    public NoParticipante() {
    }

    public static void main(String[] args) {
        try{
            Object obj;
            Socket nosocket = new Socket("localhost",4242);
            ObjectOutputStream out = new ObjectOutputStream(nosocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(nosocket.getInputStream());

            out.writeObject("no");
            Object portano = in.readObject();
            Object numerno = in.readObject();
            System.out.println("Olá, eu sou o nó "+ numerno);
            nosocket.close();
            try {
                serverSocket = new ServerSocket((Integer) portano);
                System.out.println("No conectado ao servidor");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            while (!serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream outno = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inno = new ObjectInputStream(socket.getInputStream());
                    Object comando = inno.readObject();
                    if(comando.equals("R")){
                        Object chave = inno.readObject();
                        if (hashclt.containsKey(chave)) {
                            outno.writeObject("Chave existente.");
                        } else if (!hashclt.containsKey(chave)) {
                            Object valor = inno.readObject();
                            hashclt.put(chave, valor);
                            System.out.println("Chave registada - "+ chave + " " + valor);
                            outno.writeObject("Chave registada com sucesso.");
                        } else {
                            outno.writeObject("Ocorreu um erro.");
                        }
                    }
                    else if (comando.equals("C")){
                        Object chave = inno.readObject();
                        if (hashclt.containsKey(chave)) {
                            String chavinha = (String) hashclt.get(chave);
                            outno.writeObject(chavinha);
                        } else if (!hashclt.containsKey(chave)) {
                            outno.writeObject("Chave inexistente");
                        } else {
                            outno.writeObject("Ocorreu um erro.");
                        }
                    }
                    else if (comando.equals("D")){
                        Object chave = inno.readObject();
                        if (hashclt.containsKey(chave)) {
                            hashclt.remove(chave);
                            outno.writeObject("Item removido com sucesso.");
                        } else if (!hashclt.containsKey(chave)) {
                            outno.writeObject("Chave inexistente.");
                        } else {
                            outno.writeObject("Ocorreu um erro.");
                        }
                    }
                    else if (comando.equals("L")){
                        Set entrySet = hashclt.entrySet();
                        for (Object o : entrySet) {
                            Map.Entry me = (Map.Entry) o;
                            System.out.println("Chave: " + me.getKey() +
                                    " " +
                                    "Valor: " + me.getValue());
                        }
                    }
                } catch (IOException ioe) {
                    if (serverSocket.isClosed()) {
                        System.out.println("Server terminated");
                    } else {
                        ioe.printStackTrace();
                    }
                }
        }
    } catch (Exception e) {
            System.out.println("Numero de nós excedidos.");
        }
    }
}