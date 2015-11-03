/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;
import java.net.*;
import java.io.*;
import java.util.*;

public class UDP implements Runnable
{
    private DatagramSocket serverSocket = null;
    Queue<String> orders = new LinkedList<String>();
    
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

        byte[] receiveData = new byte[9];
        byte[] sendData = new byte[9];      // Debug (tirar!)

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

            String sentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());

            InetAddress IPAddress= receivePacket.getAddress();

            int port = receivePacket.getPort();

            String capitalizedSentence = sentence.toUpperCase();

            // Exemplo de uma ordem: :T6669788
            
            // Ver se a mensagem recebida corresponde a uma ordem
            if(capitalizedSentence.charAt(0) == ':' && capitalizedSentence.length() == 9) {
            
                orders.add(capitalizedSentence.substring(1));
                
                System.out.println(orders.element());    // Debug (tirar!)

                sendData = capitalizedSentence.getBytes();      // Debug (tirar!)

                DatagramPacket sendPacket =           // Debug (tirar!)
                        new DatagramPacket(sendData, sendData.length, IPAddress, port);

                try     // Debug (tirar!)
                {
                    serverSocket.send(sendPacket);
                }
                catch(IOException e) 
                {
                   System.out.println(e);
                }
            }
            else
                System.out.println("Rebeceu um frame que não era uma ordem");   // Debug (tirar!)
        } 
    }
    
    public String getUdpOrder() {
        return orders.poll();
    }
}
