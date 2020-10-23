package org.una.aeropuertocliente.WebService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Date;
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class AvionWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/aviones";
    
    public static void getAllAviones() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AvionDTO> aviones = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
        aviones.forEach(System.out::println);
        response.join();
    }

    public static void getAvionById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            AvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAvionByMatricula(String matricula) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByMatricula/"+matricula)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            AvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAvionByTipoAvion(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByTipoAvion/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            AvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getAvionByFechaRegistroBetween(Date fechaInicial, Date fechaFinal) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            AvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAvionByEstado(boolean estado) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            AvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void createAerolinea(String matricula, String tipoAvion, Long aerolineaId) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AvionDTO bean = new AvionDTO();
        
        bean.setMatricula(matricula);
        bean.setTipoAvion(tipoAvion);
        bean.setAerolineaId(aerolineaId);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAerolinea(AvionDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Avion");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}