/*
                        ***** PRIMER EXAMEN PARCIAL *****
--> Nombre y Apellido: Oscar Moisés Alvarenga Mendoza
--> C.I N°: 4.975.153
--> Carrera: IIN
--> Fecha de nacimiento: 19/08/2001
*/

package meteorologia;

// Importamos los paquetes o clases ha utilizar dentro del proyecto
import java.net.*;
import org.json.*;
import java.io.*;
import java.util.*;

// Clase principal: UDPServer
public class UDPServer {
    // Estructura para guardar los datos    
    public static ArrayList<JSONObject> data = new ArrayList<>();
    
    // MAIN - Servidor    
    public static void main(String[] a) {
        // Se imprimen mis datos
        System.out.println("\t\t***** PRIMER EXAMEN PARCIAL *****");
        System.out.println("--> Nombre y Apellido: Oscar Moisés Alvarenga Mendoza");
        System.out.println("--> Fecha de nacimiento: 19/08/2001\n");
        
        // Variables
        int puertoServidor = 9876;

        try {
            // Creamos el socket Servidor de Datagramas (UDP)
            DatagramSocket serverSocket = new DatagramSocket(puertoServidor);
            System.out.println("Servidor Sistemas Distribuidos - UDP\n");

            // Buffer de datos a enviar y recibir
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            // Servidor siempre esperando
            while (true) {
                receiveData = new byte[1024];
                
                System.out.println("Esperando a algun cliente... ");
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // 4) Receive LLAMADA BLOQUEANTE
                serverSocket.receive(receivePacket);
                System.out.println("________________________________________________");
                System.out.println("Aceptamos un paquete");
                
                // Datos recibidos
                String datoRecibido = new String(receivePacket.getData());
                System.out.println("DatoRecibido: " + datoRecibido);

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                // Instanciamos un objeto json para guardar los datos
                JSONObject guardarDato = new JSONObject(datoRecibido);
                int temp = 0;
                
                // Obtenemos el número de opción que el usuario escogió
                int opcion = guardarDato.getInt("op");
                if(opcion == 1) {
                    // Agregamos el dato enviado por el cliente a nuestra base de datos
                    data.add(guardarDato.getJSONObject("data"));
                    sendData = "{\"Result\": \"Agregado a la Base de Datos (local-temporal)\"}".getBytes();
                    System.out.println("--> Datos agregados de forma exitosa");
                } else if(opcion == 2) {
                    boolean exist = false;
                    // Buscamos si la ciudad existe dentro de nuestra base de datos
                    for (JSONObject datapoint : data) {
                        // Si es que existe entonces guardamos la temperatura de dicha ciudad
                        if (datapoint.getString("ciudad").compareTo(guardarDato.getJSONObject("data").getString("ciudad")) == 0) {
                            temp = datapoint.getInt("temperatura");
                            exist = true;
                        }
                    }
                    
                    // Verificamos si existe
                    if(exist) {
                        sendData = ("{\"Result\": " + temp + "}").getBytes();
                        System.out.println("--> Ciudad encontrada");
                    } else {
                        sendData = "{\"Error\": \"la ciudad no existe\"}".getBytes();
                        System.out.println("--> Ciudad NO encontrada");
                    }
                } else {
                    sendData = "{\"Error\": \"opción invalida\"}".getBytes();
                }
                        
                // Enviamos la respuesta al cliente
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
                System.out.println("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
