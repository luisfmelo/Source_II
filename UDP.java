/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;
import java.net.*;
import java.io.*;

///         ///
public class UDP {
    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(54321);
        
        byte[] receiveData = new byte[8];
        byte[] sendData = new byte[8];
        
        while(true)
        {
            DatagramPacket receivePacket = 
                    new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            String sentence = new String(receivePacket.getData());
            
            InetAddress IPAddress= receivePacket.getAddress();
            
            int port = receivePacket.getPort();
            
            String capitalizedSentence = sentence.toUpperCase();
            
            System.out.println(capitalizedSentence);
            
            sendData = capitalizedSentence.getBytes();   
            
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            
            serverSocket.send(sendPacket);
        }
    }
}
