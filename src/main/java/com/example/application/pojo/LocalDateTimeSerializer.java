/*
package com.example.application.pojo;

import com.example.application.model.TaskModel;
import com.example.application.utility.DateUtility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class LocalDateTimeSerializer extends JsonSerializer<TaskModel> {


    @Override
    public void serialize(TaskModel model, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", model.getId());
        jsonGenerator.writeStringField("taskTitle", model.getTaskTitle());
       jsonGenerator.writeStringField("taskDate", model.getTaskDate().toString());
        jsonGenerator.writeStringField("startTime", DateUtility.formatDateTime(model.getStartTime()));
        jsonGenerator.writeStringField("endTime", DateUtility.formatDateTime(model.getEndTime()));
        jsonGenerator.writeEndObject();
    }
}*/
