/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

///         ///
public class UDP implements Runnable
{
    DatagramSocket serverSocket = null;
    public void run()
    {
        try
        {
           serverSocket = new DatagramSocket(54321);
        }
        catch(IOException e) 
        {
           System.out.println(e);
        }

        byte[] receiveData = new byte[8];
        byte[] sendData = new byte[8];

        while(true)
        {
            DatagramPacket receivePacket = 
                    new DatagramPacket(receiveData, receiveData.length);
            
            try 
            {
                serverSocket.receive(receivePacket);
            }
            catch(IOException e) 
            {
               System.out.println(e);
            }

            String sentence = new String(receivePacket.getData());

            InetAddress IPAddress= receivePacket.getAddress();

            int port = receivePacket.getPort();

            String capitalizedSentence = sentence.toUpperCase();

            System.out.println(capitalizedSentence);

            sendData = capitalizedSentence.getBytes();   

            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);

            try 
            {
                serverSocket.send(sendPacket);
            }
            catch(IOException e) 
            {
               System.out.println(e);
            }
        }
        
    }
}
