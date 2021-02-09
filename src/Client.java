

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.lang.*;

public class Client {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Socket clientsocket = new Socket("localhost", 4242);
            ObjectOutputStream out = new ObjectOutputStream(clientsocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientsocket.getInputStream());
            System.out.println("Ligação estabelecida.");
            System.out.println("Insira um comando:");
            out.writeObject("cliente");

            while (true) {
                String obj = sc.nextLine();
                if(obj.equalsIgnoreCase("c") || obj.equalsIgnoreCase("r") || obj.equalsIgnoreCase("d")){
                    System.out.println("erro");
                }else{
                    String[] keyValue = obj.split(" ", 3);
                    String comando = keyValue[0];
                    out.writeObject(comando);
                    if (comando.equalsIgnoreCase("R")) {
                        String chave = keyValue[1];
                        String valor = keyValue[2];
                        out.writeObject(chave);
                        out.writeObject(valor);
                        Object resposta = in.readObject();
                        System.out.println(resposta);
                    }
                    else if (comando.equalsIgnoreCase("Q")) {
                        System.out.println("A terminar a conexão...");
                        clientsocket.close();
                        break;
                    }
                    else if (comando.equalsIgnoreCase("T")) {
                        System.out.println("A terminar o server...");
                        out.writeObject("T");
                        break;

                    } else if (comando.equalsIgnoreCase("C")) {
                        String chave = keyValue[1];
                        out.writeObject(chave);
                        Object valordachave = in.readObject();
                        System.out.println(valordachave);
                    }
                    else if (comando.equalsIgnoreCase("D")){
                        String chave = keyValue[1];
                        out.writeObject(chave);
                        Object respostadelete = in.readObject();
                        System.out.println(respostadelete);

                    }else if(comando.equalsIgnoreCase("l")){

                    }
                    else{
                        System.out.println("Comando desconhecido, tente novamente.");
                    }
            }}
            } catch (IOException | ClassNotFoundException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Erro");
            }
        }
    }