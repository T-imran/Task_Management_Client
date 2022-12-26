package com.example.application.client.api_requests;
import com.example.application.client.views.TaskView;
import com.example.application.model.TaskModel;
import com.fasterxml.jackson.core.type.TypeReference;
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

public class Api_Request {
    private static final String url = "http://localhost:8080/task";
    public static HttpResponse<String> response;


public static void getAllTask() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .header("accept", "application/json")
        .uri(URI.create(url+"/getAll")).build();

        List<TaskModel> gridTaskLists = new ArrayList<>();
        try {
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<TaskModel> tasks = mapper.readValue(response.body()
        , new TypeReference<>() {
        });

        for (TaskModel task : tasks) {

        gridTaskLists.add(task);

        }
        TaskView.taskGrid.setItems(gridTaskLists);

        } catch (IOException | InterruptedException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
        }
        System.out.print(response);
        }}
