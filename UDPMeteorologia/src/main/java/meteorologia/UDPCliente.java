/*
                        ***** PRIMER EXAMEN PARCIAL *****
--> Nombre y Apellido: Oscar Moisés Alvarenga Mendoza
--> C.I N°: 4.975.153
--> Carrera: IIN
--> Fecha: 17/09/2022
*/

package meteorologia;

// Importamos los paquetes o clases ha utilizar dentro del proyecto
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;

// Clase principal: UDPCliente
public class UDPCliente {
    // MAIN - Cliente      
    public static void main(String[] args) throws IOException {
        // Se imprimen mis datos
        System.out.println("\t\t***** PRIMER EXAMEN PARCIAL *****");
        System.out.println("--> Nombre y Apellido: Oscar Moisés Alvarenga Mendoza");
        System.out.println("--> Fecha de nacimiento: 19/08/2001\n");
        
        // Dirección del servidor, en este caso el local host
        String direccionServidor = "127.0.0.1";
        // Número de puerto por el cual escuchará el servidor        
        int puertoServidor = 9876;
        
        try {
            // Instanciamos un objeto de la clase Scanner para realizar las lecturas por consola
            Scanner consola = new Scanner(System.in);
            
            // Creamos el socket de datagramas (UDP) y establecemos la conexión
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(direccionServidor);
            System.out.println("Intentando conectar a = " + IPAddress + ":" + puertoServidor + " via UDP...\n");
            
            // Buffer de datos a enviar y recibir
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            
            // Instancias de la clase JSONObject las cuales serán enviadas al servidor            
            JSONObject jo = new JSONObject();
            JSONObject data = new JSONObject();
            
            // Desplegamos el menú en la consola
            System.out.println("Elija una de las siguientes operaciones: ");
            System.out.println("--> 1. Cargar datos de sensores meteorológicos");
            System.out.println("--> 2. Consultar temperatura de una ciudad");
            System.out.println("Opción: ");
            int opcion = Integer.parseInt(consola.nextLine());
            jo.put("op", opcion);
            
            // Verificamos que opción fue la que escogió el usuario
            if(opcion == 1) {
                System.out.println("\n\t\t****** CARGA DE DATOS ********");
                System.out.println("ID_estacion: ");
                data.put("id_estacion", consola.nextLine());
                System.out.println("Ciudad: ");
                data.put("ciudad", consola.nextLine());
                System.out.println("Porcentaje de humedad: ");
                data.put("porcentaje_humedad", consola.nextLine());
                System.out.println("Temperatura (int): ");
                data.put("temperatura", Integer.parseInt(consola.nextLine()));
                System.out.println("Velocidad del viento: ");
                data.put("velocidad_viento", consola.nextLine());
                System.out.println("Fecha: ");
                data.put("fecha", consola.nextLine());
                System.out.println("Hora: ");
                data.put("hora", consola.nextLine());
                jo.put("data", data);
                sendData = jo.toString().getBytes();
            } else if(opcion == 2) {
                System.out.println("\n\t\t****** OBTENER TEMPERATURA DE UNA CIUDAD ********");
                System.out.println("Ciudad: ");
                data.put("ciudad", consola.nextLine());
                jo.put("data", data);
                sendData = jo.toString().getBytes();
            } else {
                System.out.println("ERROR!!! La operación seleccionada no existe");
            }
            
            // Enviamos el paquete
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puertoServidor);
            clientSocket.send(sendPacket);

            // Recibimos el paquete
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Establecemos un timeout maximo de espera en la función bloqueante
            clientSocket.setSoTimeout(10000);

            try {
                // Esperamos la respuesta, se bloquea el proceso hasta recibir respuesta
                clientSocket.receive(receivePacket);
                String respuesta = new String(receivePacket.getData());
                InetAddress returnIPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                System.out.println("Respuesta desde =  " + returnIPAddress + ":" + port + ": " + respuesta);
            } catch (SocketTimeoutException ste) {
                System.out.println("TimeOut: El paquete udp se asume perdido.");
            }
            clientSocket.close();
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}
