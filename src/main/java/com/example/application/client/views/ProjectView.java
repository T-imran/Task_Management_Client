package com.example.application.client.views;


import com.example.application.client.Api_Request;
import com.example.application.model.TaskModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.Json;

import java.util.List;

@CssImport(value = "./themes/taskmanagement/Styles.css", themeFor = "vaadin-grid")
@PageTitle("")
@Route(value = "project", layout = MainLayout.class)
public class ProjectView extends Div {

    public static Grid<TaskModel> taskGrid;
    public static final String failed = "Failed";
    public static final String success = "Successfully Loaded";
    String response;
    Json gson = new Json();
    List<TaskModel> nidList;

    ProjectView(){
        Div mainDiv = new Div();

        Div subDiv = new Div();
        subDiv.getStyle().set("margin","100px");


        TextField projectField = new TextField("Projects");
        DatePicker startDate = new DatePicker("Sprint start");
        TextField endDate = new TextField("Sprint end");
        HorizontalLayout horizontalLayout = new HorizontalLayout();



        Button createTask = new Button("Add task", VaadinIcon.PLUS.create());
        createTask.addThemeVariants(ButtonVariant.LUMO_SMALL);
        createTask.getStyle().set("color","#2ecc71");
        HorizontalLayout buttonHori = new HorizontalLayout(createTask);
        buttonHori.setAlignItems(FlexComponent.Alignment.END);
        buttonHori.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        //Grid Section

        taskGrid = new Grid<>();
        HorizontalLayout gridHorizontalLayout1 = new HorizontalLayout();
        taskGrid.setClassName("my-grid");
        taskGrid.setHeight("600px");
        taskGrid.addColumn(TaskModel::getTaskTitle).setWidth("120px").setHeader("Task Name").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getTaskDate).setWidth("120px").setHeader("Date").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getStartTime).setWidth("120px").setHeader("Start Date").setSortable(true)
                .setResizable(true);
        taskGrid.addColumn(TaskModel::getEndTime).setWidth("120px").setHeader("End Date").setSortable(true)
                .setResizable(true);

        taskGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_ROW_STRIPES);
        Api_Request.getAllTask();
        gridHorizontalLayout1.add(taskGrid);


        horizontalLayout.add(projectField,startDate,endDate);
        subDiv.add(buttonHori,gridHorizontalLayout1);
        mainDiv.add(subDiv);
        add(mainDiv);
    }
}
