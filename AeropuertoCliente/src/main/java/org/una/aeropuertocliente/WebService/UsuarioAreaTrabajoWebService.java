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
import java.util.Date;
import org.una.aeropuertocliente.DTOs.AreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.UsuarioAreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Adrian
 */
public class UsuarioAreaTrabajoWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/usuariosAreasTrabajo";
    
    public static void getUsuarioAreaTrabajoById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area de trabajo del usuario No Encontrada");

        else
        {
            UsuarioAreaTrabajoDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioAreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getUsuarioAreaTrabajoByFechaRegistroBetween(Date fechaInicial, Date fechaFinal) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area trabajo del usuario No Encontrado");

        else
        {
            UsuarioAreaTrabajoDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioAreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }


    public static void createUsuarioAreaTrabajo(AreaTrabajoDTO areaTrabajo, UsuarioDTO usuario) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        UsuarioAreaTrabajoDTO bean = new UsuarioAreaTrabajoDTO();
        
        bean.setAreaTrabajo(areaTrabajo);
        bean.setUsuario(usuario);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateUsuarioAreaTrabajo(UsuarioAreaTrabajoDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Area de trabajo del usuario");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioAreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}