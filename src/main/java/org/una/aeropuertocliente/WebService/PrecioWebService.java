package org.una.aeropuertocliente.WebService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.una.aeropuertocliente.DTOs.PrecioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class PrecioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/precios";
    
    public static void getPrecioById() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Precio No Encontrado");

        else
        {
            PrecioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), PrecioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getPrecioByFechaRegistroBetween() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/fecha/{termino1}{termino2}/")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Precio No Encontrado");

        else
        {
            PrecioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), PrecioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getPrecioByTipoServicioId() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/tipoServicioId/{id}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Precio No Encontrado");

        else
        {
            PrecioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), PrecioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void createPrecio(Float monto, Long tipoServicioId) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        PrecioDTO bean = new PrecioDTO();
        
        bean.setMonto(monto);
        bean.setTipoServicioId(tipoServicioId);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updatePrecio(PrecioDTO bean) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Precio");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), PrecioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}