package com.example.application.api_requests;

import com.example.application.model.LoginModel;
import com.example.application.model.TokenModel;
import com.example.application.views.TaskView;
import com.example.application.model.TaskModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiRequest {
    private static final String url = "http://localhost:8080";
    public static HttpResponse<String> response;
    public static   List<TokenModel> responseToken;
    public static   TokenModel tokenGlobal;
    public static    String tokenString;


    public static void getAllTask() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .header("Authorization", "Bearer "+tokenString)
                .uri(URI.create(url + "/task/getAll")).build();

        List<TaskModel> gridTaskLists = new ArrayList<>();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            List<TaskModel> tasks = mapper.readValue(response.body()
                    , new TypeReference<>() {
                    });
            gridTaskLists.addAll(tasks);
            TaskView.taskGrid.setItems(gridTaskLists);

        } catch (IOException | InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.print(response);
    }

    public static boolean loginRequest(LoginModel loginRequest ) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(loginRequest);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/api/v1/auth/authenticate"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            responseToken = objectMapper.readValue(response.body()
                    , new TypeReference<>() {
                    });
            for (TokenModel tokenNeed:responseToken) {
                tokenString =tokenNeed.getToken().toString();
            }
        } catch (IOException | InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        System.out.print(responseToken);
        return true;
    }

    public static void saveTask(TaskModel saveTaskList) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(saveTaskList);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/task/save"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+tokenString)
                .build();

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println(response.body());
    }

    public static void deleteTask(Long id){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/task/delete?id=" + id))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();

        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println(response.body());
    }
}
