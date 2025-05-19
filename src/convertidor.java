import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class convertidor {
    public static void main(String[] arg){
        Scanner scanner = new Scanner(System.in);
        DecimalFormat formatoDecimal = new DecimalFormat("0.00");

        while (true) {
            try {
                //Muestra menú de opciones5
                System.out.println();
                System.out.println("Sea Bienvenido/a al Conversor de Moneda");
                System.out.println();
                System.out.println("1) Dólar (USD) =>> Peso argentino (ARS)");
                System.out.println("2) Peso argentino (ARS) =>> Dólar (USD)");
                System.out.println("3) Dólar (USD) =>> Real brasilero (BRL)");
                System.out.println("4) Real brasilero (BRL) =>> Dólar (USD)");
                System.out.println("5) Dólar (USD) =>> Peso colombiano (COP)");
                System.out.println("6) Peso colombiano (COP) =>> Dólar (USD)");
                System.out.println("0) Salir");
                System.out.println();
                System.out.print("Elija una opción valida: ");
                int opcion = scanner.nextInt();
                System.out.println();

                // Salir del programa si la opción es 0
                if (opcion == 0) {
                    System.out.println("Saliendo del programa...");
                    break;
                }
                if (opcion < 0 || opcion > 6) {
                    System.out.println("Opción no válida, por favor intente de nuevo.");
                    continue;
                }

                // Solicita la cantidad a convertir
                System.out.print("Ingrese la cantidad que desea convertir: ");
                double cantidad = scanner.nextDouble();
                String url = "";
                String monedaOrigen = "";
                String monedaDestino = "";

                // Configurar la URL y las monedas según la opción seleccionada
                switch (opcion) {
                    case 1:
                        url = "https://v6.exchangerate-api.com/v6/d8617db474b63a8cf238ef38/pair/USD/ARS";
                        monedaOrigen = "USD";
                        monedaDestino = "ARS";
                        break;
                    case 2:
                        url = "https://v6.exchangerate-api.com/v6/d8617db474b63a8cf238ef38/pair/ARS/USD";
                        monedaOrigen = "ARS";
                        monedaDestino = "USD";
                        break;
                    case 3:
                        url = "https://v6.exchangerate-api.com/v6/d8617db474b63a8cf238ef38/pair/USD/BRL";
                        monedaOrigen = "USD";
                        monedaDestino = "BRL";
                        break;
                    case 4:
                        url = "https://v6.exchangerate-api.com/v6/d8617db474b63a8cf238ef38/pair/BRL/USD";
                        monedaOrigen = "BRL";
                        monedaDestino = "USD";
                        break;
                    case 5:
                        url = "https://v6.exchangerate-api.com/v6/d8617db474b63a8cf238ef38/pair/USD/COP";
                        monedaOrigen = "USD";
                        monedaDestino = "COP";
                        break;
                    case 6:
                        url = "https://v6.exchangerate-api.com/v6/d8617db474b63a8cf238ef38/pair/COP/USD";
                        monedaOrigen = "COP";
                        monedaDestino = "USD";
                        break;
                }

                // Crea un cliente y solicita HTTP
                HttpClient cliente = HttpClient.newHttpClient();
                HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
                HttpResponse<String> respuesta = cliente
                .send(solicitud, HttpResponse.BodyHandlers.ofString());

                // Obtiene la respuesta JSON
                String json = respuesta.body();
                //System.out.println("Respuesta JSON: " + json);

                // Convertir el JSON a un objeto Java
                Gson gson = new Gson();
                Conversion conversion = gson.fromJson(json, Conversion.class);

                System.out.println();

                // Calcula y muestra el resultado de la conversión
                if (conversion != null) {
                double resultado = cantidad * conversion.tasaConversion;
                String resultadoFormateado = formatoDecimal.format(resultado);
                System.out.println("*********************************************************");
                System.out.println("Conversión: " + cantidad + " " + monedaOrigen + " = " + resultadoFormateado + " " + monedaDestino);
                System.out.println("*********************************************************");
                } else {
                    System.out.println("Error en la conversión.");
                }
            } catch (InputMismatchException e) {
                // Maneja la entrada no válida del usuario
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                scanner.next(); // Limpia el scanner para la próxima entrada
            } catch (IOException | InterruptedException e) {
               // Maneja los errores de solicitud HTTP
               System.out.println("Se produjo un error al realizar la solicitud de datos. Por favor, intente nuevamente.");
            }
        }
    }

    // Clase  para representar la respuesta de la API
    static class Conversion {

        @SerializedName("conversion_rate")
        double tasaConversion;
    }
}


