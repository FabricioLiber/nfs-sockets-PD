package br.edu.ifpb.filesystem.sockets;

import com.sun.org.apache.xml.internal.security.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class Server {

    private static String dir = System.getProperty("user.dir") + "/files/";

    public static void main(String[] args) {
        System.out.println("== Servidor ==");
        try {

            ServerSocket serverSocket = new ServerSocket(8000);
            Socket socket = serverSocket.accept();

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            while (true) {
                String mensagem = dis.readUTF();
                System.out.println("Cliente " + socket.getInetAddress() + " solicita " + mensagem);
                List<String> data = Arrays.asList(mensagem.split(" "));
                if (data.get(1) == null || data.get(1).equals("")) {
                    dos.writeUTF("Informe o caminho do arquivo correto!");
                } else {
                    if (data.get(0).equals("readdir")) {

                        Path file = Paths.get(dir + data.get(1));
                        System.out.println("Arquivos no diretório " + file);
                        dos.writeUTF("Arquivos no diretório " + file);
                        Files.list(file).forEach(item -> {
                            System.out.println(item.getFileName());
                            try {
                                dos.writeUTF(item.getFileName().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else if (data.get(0).equals("rename")) {
                        Path file1 = Paths.get(dir + data.get(1));
                        Path file2 = Paths.get(dir + data.get(2));
                        Files.move(file1, file2);
                        dos.writeUTF("Arquivo renomeado!");
                        System.out.println("Renomeação feita!");
                    } else if (data.get(0).equals("create")) {
                        Path p = Paths.get(dir + data.get(1));
                        Files.createFile(p);
                        dos.writeUTF("Arquivo criado!");

                    } else if (data.get(0).equals("remove")) {
                        Path p = Paths.get(dir + data.get(1));
                        if (Files.exists(p)) {
                            Files.delete(p);
                            dos.writeUTF("Arquivo removido!");
                            System.out.println("Remoção feita");
                        } else
                            dos.writeUTF("Arquivo não existe!");
                            System.out.println("Arquivo não existe!");
                    } else {
                        dos.writeUTF("Ação inválida!");
                    }
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
