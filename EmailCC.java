import java.io.*;
import javax.net.ssl.*;
import java.util.Base64;

public class EmailCC {

    private static DataOutputStream dos;
    private static BufferedReader br;

    public static void main(String[] args) throws Exception{
        String fromEmail = "your-email@gmail.com";
        String pass = "App password";
        String toEmail = "to@gmail.com";
        String ccEmail = "cc@gmail.com";

        String username = Base64.getEncoder().encodeToString(fromEmail.getBytes());
        String password = Base64.getEncoder().encodeToString(pass.getBytes());

        SSLSocket socket = (SSLSocket) SSLSocketFactory
            .getDefault()
            .createSocket("smtp.gmail.com",465);

        dos = new DataOutputStream(socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        readLine();

        send("EHLO smpt.gmail.com\r\n");
        readEHLO();

        send("AUTH LOGIN\r\n");
        readLine();

        send(username + "\r\n");
        readLine();

        send(password + "\r\n");
        readLine();

        send("MAIL FROM:<" + fromEmail + ">\r\n");
        readLine();

        send("RCPT TO:<" + toEmail + ">\r\n");
        readLine();

        send("RCPT TO:<" + ccEmail + ">\r\n");
        readLine();

        send("DATA\r\n");
        readLine();

        send("From: " + fromEmail + "\r\n");
        send("To: " + toEmail + "\r\n");
        send("Cc: " + ccEmail + "\r\n");
        send("Subject: CC test Email\r\n");
        send("\r\n");

        send("Hello,\r\nThis is a CC Email test\r\n");

        send(".\r\n");
        readLine();

        send("QUIT\r\n");
        readLine();

        socket.close();

    }
    private static void send(String s) throws Exception{
        dos.writeBytes(s);
        dos.flush();
        System.out.println("CLIENT: "+s);
        Thread.sleep(200);
    }

    private static void readLine() throws Exception{
        String line = br.readLine();
        if(line == null)
            throw new RuntimeException("Server close connection(Broken pipe root cause)");

        System.out.println("SERVER: "+line);
    }

    private static void readEHLO() throws Exception{
        String line;
        while((line = br.readLine()) != null){
            System.out.println("SERVER: "+line);
            if(line.length() > 3 && line.charAt(3)==' ')
                break;
        }
    }
}
