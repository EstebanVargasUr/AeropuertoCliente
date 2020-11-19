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
import java.net.URLEncoder;
import org.una.aeropuertocliente.DTOs.AerolineaDTO;
import org.una.aeropuertocliente.utility.FlowController;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class AerolineaWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/aerolineas";
    
    public static List<AerolineaDTO> getAllAerolineas(String finalToken) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findAll"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AerolineaDTO> aerolineas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AerolineaDTO>>() {});
        aerolineas.forEach(System.out::println);
        response.join();
        return aerolineas;
    }

    public static AerolineaDTO getAerolineaById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {   
        AerolineaDTO bean=new AerolineaDTO();
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        
        if(response.get().statusCode() == 500)
            System.out.println("Aerolinea No Encontrada");

        else
        {
            if (response.get().body().isBlank()) {
                System.out.println("No existen aerolineas con este Id");
            }
            else{
                bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
                System.out.println(bean);
            }
        }
        response.join();
        return bean;
    }
    
    public static List<AerolineaDTO> getAerolineaByNombreAerolinea(String nombre, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String nombreAdaptado = URLEncoder.encode(nombre, "UTF-8");
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombreAerolinea/"+nombreAdaptado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AerolineaDTO> aerolineas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AerolineaDTO>>() {});
        aerolineas.forEach(System.out::println);
        response.join();
        return aerolineas;
    }
    
    public static List<AerolineaDTO> getAerolineaByNombreResponsable(String nombre, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String nombreAdaptado = URLEncoder.encode(nombre, "UTF-8");
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombreResponsable/"+nombreAdaptado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AerolineaDTO> aerolineas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AerolineaDTO>>() {});
        aerolineas.forEach(System.out::println);
        response.join();
        return aerolineas;
    }

    public static List<AerolineaDTO> getAerolineaByEstado(Boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
         .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AerolineaDTO> aerolineas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AerolineaDTO>>() {});
        aerolineas.forEach(System.out::println);
        response.join();
        return aerolineas;
    }

    public static void createAerolinea(AerolineaDTO bean, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());
         if(response.get().statusCode() == 500)
            System.out.println("No se pudo crear la Aerolinea");

        else {
            TransaccionWebService.createTransaccion("Creación de Aerolinea.\nNombre: "+bean.getNombreAerolinea(),"Transacción",
            FlowController.getInstance().authenticationResponse.getUsuario() , FlowController.getInstance().authenticationResponse.getJwt());
        }
        response.join();
    }

    public static void updateAerolinea(AerolineaDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar la Aerolinea");

        else {
            TransaccionWebService.createTransaccion("Modificación de Aerolinea.\nNombre: "+bean.getNombreAerolinea(),"Transacción",
            FlowController.getInstance().authenticationResponse.getUsuario() , FlowController.getInstance().authenticationResponse.getJwt());
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}