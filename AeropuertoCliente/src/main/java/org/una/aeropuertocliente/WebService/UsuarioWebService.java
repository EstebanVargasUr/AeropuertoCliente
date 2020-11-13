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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.una.aeropuertocliente.DTOs.RolDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class UsuarioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/usuarios";
    
    public static List<UsuarioDTO> getAllUsuarios(String finalToken) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findAll"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<UsuarioDTO> usuarios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<UsuarioDTO>>() {});
        usuarios.forEach(System.out::println);
        response.join();
        return usuarios;
    }

    public static UsuarioDTO getUsuarioById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            UsuarioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioDTO.class);
            System.out.println(bean);
            return bean;
        }
        return null;
    }
    
    public static List<UsuarioDTO> getUsuarioByCedulaAproximate(String cedula, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByCedula/"+cedula))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            List<UsuarioDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<UsuarioDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
       return null;
    }

    public static List<UsuarioDTO> getUsuarioByNombreCompletoAproximateIgnoreCase(String nombre, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String nombreAdaptado = URLEncoder.encode(nombre, "UTF-8");
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombre/"+nombreAdaptado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            List<UsuarioDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<UsuarioDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        return null;
    }
    
    public static List<UsuarioDTO> getUsuarioByUsuarioJefeId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByUsuarioJefeId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            List<UsuarioDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<UsuarioDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        return null;
    }
    
    public static List<UsuarioDTO> getUsuarioByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+stringDate+"/"+stringDate2))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            List<UsuarioDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<UsuarioDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        return null;
    }
    
    public static void createUsuario(UsuarioDTO bean, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateUsuario(UsuarioDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Usuario");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
}