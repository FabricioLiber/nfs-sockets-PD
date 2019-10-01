package br.edu.ifpb.filesystem.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("== Cliente ==");

        // configurando o socket
        Socket socket = new Socket("localhost", 8000);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        DataInputStream dis = new DataInputStream(socket.getInputStream());


        while (true) {
            Scanner teclado = new Scanner(System.in);

            dos.writeUTF(teclado.nextLine());

            String mensagem = dis.readUTF();
            System.out.println("Servidor informa que " + mensagem);
        }
    }

}
