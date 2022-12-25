package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskView {


        private static final Logger log = LoggerFactory.getLogger(SearchNid.class);
        private static final String DUMMY_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTg6FRpBIWpe4w9iStHT2G-LxQQo7QhxkrMg&usqp=CAU";
        // URL url = new URL ("https://reqres.in/api/users");
        // public static final String url = "http://localhost:8080/nid/status";
        String nidUrl = "http://localhost:8080/nid/status";
        private static final String emptyStr = "";
        static Grid<NationalId> grid_1;
        static Grid<NationalId> grid_2;
        public static final String failed = "Failed";
        public static final String success = "Successfully Loaded";
        String response;
        Gson gson = new Gson();
        List<NationalId> nidList;

	public SearchNid() {

        // Title --start
        Div titleText = new Div();

        HorizontalLayout TextLayout = new HorizontalLayout();
        H4 searchText = new H4("NID Search");

        TextLayout.add(searchText);
        TextLayout.setAlignItems(FlexComponent.Alignment.START);
        TextLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        // TextLayout.getStyle().set("margin", "0px 0px 0px 10px");
        searchText.setClassName("text-field");
        searchText.getStyle().set("margin", "0px 0px 0px 30px");
        titleText.getStyle().set("background", "#0C1C57").set("padding", "5px");

        Div titleText2 = new Div();

        HorizontalLayout TextLayout2 = new HorizontalLayout();
        H4 searchText2 = new H4("Previous Search History");

        TextLayout2.add(searchText2);
        TextLayout2.setAlignItems(FlexComponent.Alignment.START);
        TextLayout2.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        TextLayout2.setSpacing(false);
        searchText2.setClassName("text-field");
        searchText2.getStyle().set("margin", "0px 0px 0px 10px");
        titleText2.getStyle().set("background", "#0C1C57").set("padding", "5px");
        // Title --End

        // SearchBar text Fields --start
        Div searchBar = new Div();
        searchBar.setClassName("search");
        HorizontalLayout searchBarLayout = new HorizontalLayout();

        ComboBox comboBox = new ComboBox("Search Condition");
        comboBox.setId("condition-combox");
        List<String> comboList = new ArrayList<>();
        comboList.add("All");
        comboList.add("NID and DOB");
        comboList.add("NID(only Internal DB)");
        comboList.add("Date of Birth(only Internal DB)");
        comboList.add("Name(only Internal DB)");
        comboBox.setItems(comboList);

        TextField nid = new TextField("NID");
        nid.setId("nid-field");
        DatePicker date = new DatePicker("Date of Birth");
        date.setId("date-field");
        TextField name = new TextField("Name");
        name.setId("name-field");

        comboBox.setClassName("text-field");
        nid.setClassName("text-field");
        date.setClassName("text-field");
        name.setClassName("text-field");

        comboBox.getElement().setAttribute("theme", TextFieldVariant.LUMO_SMALL.getVariantName());
        nid.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        date.getElement().setAttribute("theme", TextFieldVariant.LUMO_SMALL.getVariantName());
        name.addThemeVariants(TextFieldVariant.LUMO_SMALL);

        searchBarLayout.getStyle().set("margin", "10px 0px 0px 10px");
        searchBarLayout.add(comboBox, nid, date, name);

        // SearchBar text Fields --End

        // Action Button --Start
        Div actionBtn = new Div();
        actionBtn.setClassName("action-Btn");
        HorizontalLayout actionBtnLayout = new HorizontalLayout();

        Button search = new Button("Search", VaadinIcon.SEARCH.create(), e -> {
            // Do Search Method Here
            if(nid.getValue().isEmpty() && date.getValue()==null) {

                Span success = new Span("Please Enter 'NID' and 'Date of Birth'");
                success.getStyle().set("color", "var(--lumo-error-color");
                Notification.show("", 2000, Notification.Position.TOP_CENTER).add(success);
            }
            else if(nid.getValue().isEmpty()) {

                Span success = new Span("Please Enter 'NID'");
                success.getStyle().set("color", "var(--lumo-error-color");
                Notification.show("", 2000, Notification.Position.TOP_CENTER).add(success);
            }
            else if(date.getValue()==null) {

                Span success = new Span("Please Enter 'Date of Birth'");
                success.getStyle().set("color", "var(--lumo-error-color");
                Notification.show("", 2000, Notification.Position.TOP_CENTER).add(success);
            }
            else {
                nidDetail(nid, date, "SELECT_NID_DETAILS");
            }
        });
        search.setId("search-button");



        Button reset = new Button("Reset", VaadinIcon.REFRESH.create(), e -> {
            comboBox.clear();
            nid.clear();
            date.clear();
            name.clear();
        });
        reset.setId("reset-button");

        search.setClassName("all-btn");
        reset.setClassName("all-btn");

        Checkbox checkboxEC = new Checkbox();
        checkboxEC.setLabel("Forcefully Pull From EC");
        checkboxEC.setId("checkbox");

        search.addThemeVariants(ButtonVariant.LUMO_SMALL);
        reset.addThemeVariants(ButtonVariant.LUMO_SMALL);

        searchBarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        actionBtnLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        searchBarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        actionBtnLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        actionBtnLayout.getStyle().set("margin", "10px 0px 0px 10px");

        actionBtnLayout.add(search, reset, checkboxEC);
        // Action Button --End

        // Grid 1 --Start
        Div gridSection = new Div();
        HorizontalLayout gridsectionLayout = new HorizontalLayout();
        VerticalLayout gridvertical = new VerticalLayout();
        Span textR = new Span("Search Result:");
        textR.getStyle().set("font-size", "16px");
        gridvertical.setSpacing(false);

        NidResultModel nidR = new NidResultModel();
        nidR.setNid("123");
        // nidR.setCondition("NID and DOB");

        grid_1 = new Grid<>();
        grid_1.setClassName("my-grid");
        grid_1.setHeight("200px");

        grid_1.addColumn(NationalId::getNidDetailsFrom).setWidth("120px").setHeader("Result Status").setSortable(true)
                .setResizable(true);
        grid_1.addColumn(NationalId::getNid).setWidth("120px").setHeader("NID").setSortable(true).setResizable(true);
        grid_1.addColumn(NationalId::getDateOfBirth).setWidth("120px").setHeader("Date of Birth").setSortable(true)
                .setResizable(true);
        grid_1.addColumn(NationalId::getNameEnglish).setWidth("120px").setHeader("Name").setSortable(true)
                .setResizable(true);
        grid_1.addColumn(NationalId::getFatherName).setWidth("100px").setHeader("Father Name").setSortable(true)
                .setResizable(true);

        grid_1.addItemDoubleClickListener(doubleClikEvent -> {
            Dialog dialog = new Dialog();

            dialog.removeAll();
            // log.info("double clicked {}", doubleClikEvent.getItem().getBranchName());
            Div editDialogLayout = nidSearchDialogLayout(dialog, doubleClikEvent);
            dialog.add(editDialogLayout);
            dialog.add(editDialogLayout);
            dialog.open();
            dialog.getElement().setAttribute("aria-label", "Add note");
            dialog.setHeight("calc(85vh - (2*var(--lumo-space-s)))");
            dialog.setWidth("calc(90vw - (4*var(--lumo-space-s)))");
            dialog.getElement().setAttribute("aria-label", "Add note");
//
//
//			NidDetail nidDetail = new NidDetail();
//			dialog.add(nidDetail);
//			dialog.open();
        });
        grid_1.addComponentColumn(cibSubjectData -> {

            // Span editButton = new Span("View");
            Button editButton = new Button("View");
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            // Icon editButton = new Icon(VaadinIcon.EDIT);
//			editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editButton.setClassName("all-btn");
            editButton.getStyle().set("font-size", "14px");
//			editButton.getStyle().set("background", "#4b6584").set("color", "#ffffff");
            // editButton.getStyle().set("color", "#4b6584").set("width", "16px");
            editButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Add note");
                dialog.setHeight("calc(85vh - (2*var(--lumo-space-s)))");
                dialog.setWidth("calc(90vw - (4*var(--lumo-space-s)))");
                dialog.getElement().setAttribute("aria-label", "Add note");

                NidDetail nidDetail = new NidDetail();
                dialog.add(nidDetail);
                dialog.open();
            });
            return editButton;
        }).setWidth("100px").setFlexGrow(0).setFrozen(true).setHeader("Action").setVisible(false);

        grid_1.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_ROW_STRIPES);
        // grid_1.getStyle().set("font-size", "8px");
        grid_1.addThemeName("grid-selection-theme");
        gridsectionLayout.getStyle().set("margin", "0px 0px 0px 0px");
        gridvertical.add(textR, grid_1);
        gridsectionLayout.add(gridvertical);
        // Grid 1 --End

        // Grid 2 --Start
        Div gridSection2 = new Div();
        HorizontalLayout gridsectionLayout2 = new HorizontalLayout();
        VerticalLayout gridvertical2 = new VerticalLayout();
        Div gridbtn = new Div();
        HorizontalLayout gridBtnLayout = new HorizontalLayout();
        // gridbtn.getStyle().set("margin", "0px 50px 0px 50px");

        gridBtnLayout.setWidth("100%");
        Span textR2 = new Span("Search Result:");
        textR2.getStyle().set("font-size", "16px");

        Button allRequest = new Button("All Request History Serch", e -> {
            UI.getCurrent().navigate(RequestHistory.class);
        });

        allRequest.setClassName("all-btn");
        allRequest.setId("nav-history-button");
        allRequest.getStyle().set("margin", "25px 0 30px 10");
        allRequest.addThemeVariants(ButtonVariant.LUMO_SMALL);

        gridvertical2.setSpacing(false);

        gridBtnLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        grid_2 = new Grid<>();
        grid_2.setClassName("my-grid");
        grid_2.setHeight("370px");

        grid_2.addColumn(NationalId::getNidDetailsFrom).setWidth("150px").setHeader("Hit/Miss Status").setSortable(true)
                .setResizable(true);
        grid_2.addColumn(NationalId::getNidDetailsFrom).setWidth("180px").setHeader("Search Condition")
                .setSortable(true).setResizable(true);
        grid_2.addColumn(NationalId::getNid).setWidth("200px").setHeader("NID").setSortable(true).setResizable(true);
        grid_2.addColumn(NationalId::getDateOfBirth).setWidth("120px").setHeader("Date of Birth").setSortable(true)
                .setResizable(true);
        grid_2.addColumn(NationalId::getNameEnglish).setWidth("200px").setHeader("Name").setSortable(true)
                .setResizable(true);
        grid_2.addColumn(NationalId::getFatherName).setWidth("250px").setHeader("Father Name").setSortable(true)
                .setResizable(true);
        grid_2.addColumn(NationalId::getExtractTime).setWidth("200px").setHeader("Internal DB Save Date").setSortable(true)
                .setResizable(true);
        grid_2.addColumn(NationalId::getRequestedDate).setWidth("200px").setHeader("Search Date").setSortable(true)
                .setResizable(true);
        grid_2.addColumn(NationalId::getNid).setWidth("120px").setHeader("Searched By").setSortable(true)
                .setResizable(true);


        grid_2.addItemDoubleClickListener(doubleClikEvent -> {
//			Dialog dialog = new Dialog();
//
//			dialog.removeAll();
//
            NationalId getData = doubleClikEvent.getItem();

            String nid3 =getData.getNid().toString();
            String date3 = getData.getDateOfBirth().toString();
            nidHistoryDetail(nid3,date3,"SELECT_NID_DETAILS");
        });
        // grid_2.getStyle().set("font-size", "8px");
        grid_2.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT,
                GridVariant.LUMO_ROW_STRIPES);

        gridSection2.getStyle().set("margin", "0px 0px 20px 0px");

        gridBtnLayout.add(textR2);

        gridbtn.add(gridBtnLayout);
        gridvertical2.add(gridbtn, grid_2, allRequest);
        gridsectionLayout2.add(gridvertical2);
        // Grid 2 --End

        // Layout components --start

        // Layout components --End

        // Divs
        titleText.add(TextLayout);
        titleText2.add(TextLayout2);
        searchBar.add(searchBarLayout);
        actionBtn.add(actionBtnLayout);
        gridSection.add(gridsectionLayout);
        gridSection2.add(gridsectionLayout2);

        searchBar.getStyle().set("margin", "0px 0px 0px 10px");
        actionBtn.getStyle().set("margin", "0px 0px 0px 10px");

        Div padDiv = new Div();
        padDiv.setClassName("div-border");
        padDiv.add(titleText, searchBar, actionBtn, gridSection);

        Div padDiv2 = new Div();
        padDiv2.setClassName("div-border");
        padDiv2.add(titleText2, gridSection2);

        // Main Page view
        add(padDiv, padDiv2);
        history();
    }

        private Div nidSearchDialogLayout(Dialog dialog, ItemDoubleClickEvent<NationalId> event) {



        Div mainDiv = new Div();
        mainDiv.setWidthFull();
        mainDiv.setClassName("div-border2");
        mainDiv.getStyle().set("background", "#ffffff");

        Div heading = new Div();
        heading.setWidthFull();
        heading.getStyle().set("background", "#00114F");
        heading.setHeight("30px");
        //Span span = new Span("NID Full Detail of: " + nidRs.getNameEnglish());
        //span.getStyle().set("color", "#ffffff").set("margin", "0px 0px 0px 15px");



        Span exit = new Span(VaadinIcon.CLOSE_CIRCLE.create());
        exit.addClickListener(e->{
            dialog.close();
        });
        exit.setId("exit-btn");
        //exit.addThemeVariants(ButtonVariant.LUMO_SMALL);
        //exitdialog.setWidth("5px");
        exit.getStyle().set("margin-left", "96.5%");

        //heading.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        heading.add(exit);



        // Start
        TextField nameBn = new TextField();
        nameBn.setReadOnly(true);
        nameBn.setValue(event.getItem().getNameBangla().toString());
        H6 hNameBn = new H6("Name (Bangla):");
        hNameBn.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        nameBn.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiNameBn = new HorizontalLayout(hNameBn, nameBn);
        horiNameBn.setWidth("300px");
        // END

        // Start
        TextField nameEn = new TextField();
        nameEn.setReadOnly(true);
        nameEn.setValue(event.getItem().getNameEnglish().toString());
        H6 hNameEn = new H6("Name (English):");
        hNameEn.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        nameEn.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiNameEn = new HorizontalLayout(hNameEn, nameEn);
        horiNameEn.setWidth("300px");
        // END
        // Start
        TextField dob = new TextField();
        dob.setReadOnly(true);
        dob.setValue(event.getItem().getDateOfBirth().toString());
        H6 hdob = new H6("Date of Birth:");
        hdob.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        dob.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horidob = new HorizontalLayout(hdob, dob);
        horidob.setWidth("300px");
        // END

        // Start
        TextField FatherName = new TextField();
        FatherName.setReadOnly(true);
        FatherName.setValue(event.getItem().getFatherName().toString());
        H6 hFatherName = new H6("Father Name:");
        hFatherName.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        FatherName.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiFatherName = new HorizontalLayout(hFatherName, FatherName);
        horiFatherName.setWidth("300px");
        // horiName.setAlignItems(FlexComponent.Alignment.CENTER);
        // END

        // Start 2nd section
        TextField MotherName = new TextField();
        MotherName.setReadOnly(true);
        MotherName.setValue(event.getItem().getMotherName().toString());
        H6 hMotherName = new H6("Mother Name:");
        hMotherName.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        MotherName.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiMotherName = new HorizontalLayout(hMotherName, MotherName);
        horiMotherName.setWidth("300px");
        // END

        // Start
        TextField SpouseName = new TextField();
        SpouseName.setReadOnly(true);
        SpouseName.setValue(event.getItem().getSpouseName().toString());
        H6 hSpouseName = new H6("Spouse Name:");
        hSpouseName.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        SpouseName.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiSpouseName = new HorizontalLayout(hSpouseName, SpouseName);
        horiSpouseName.setWidth("300px");
        // END

        // Start
        TextField Occupation = new TextField();
        Occupation.setReadOnly(true);
        Occupation.setValue(event.getItem().getOccupation().toString());
        H6 hOccupation = new H6("Occupation:");
        hOccupation.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        Occupation.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiOccupation = new HorizontalLayout(hOccupation, Occupation);
        horiOccupation.setWidth("300px");
        // END

        // Start 3rd section
        TextField nationalID = new TextField();
        nationalID.setReadOnly(true);
        nationalID.setValue(event.getItem().getNid().toString());
        H6 hNationalID = new H6("National ID:");
        hNationalID.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        nationalID.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiNationalID = new HorizontalLayout(hNationalID, nationalID);
        horiNationalID.setWidth("300px");
        // END

        // Start
        TextField bloodGrp = new TextField();
        bloodGrp.setReadOnly(true);
        bloodGrp.setValue(event.getItem().getBloodGroup().toString());
        H6 hbloodGrp = new H6("Blood Group:");
        hbloodGrp.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        bloodGrp.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horibloodGrp = new HorizontalLayout(hbloodGrp, bloodGrp);
        horibloodGrp.setWidth("300px");
        // END

        // Start
        TextField pin = new TextField();
        pin.setReadOnly(true);
        pin.setValue(event.getItem().getNidPin().toString());
        H6 hpin = new H6("PIN:");
        hpin.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        pin.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horipin = new HorizontalLayout(hpin, pin);
        horipin.setWidth("300px");
        // END

        // Present Address Start
        // Start
        TextField division = new TextField();
        division.setWidth("350px");
        division.setReadOnly(true);
        division.setValue(event.getItem().getPresentDivision().toString());
        H6 hDivision = new H6("Division:");
        hDivision.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        division.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDivision = new HorizontalLayout(hDivision, division);
        horiDivision.setWidth("530px");
        // END

        // Start
        TextField district = new TextField();
        district.setWidth("350px");
        district.setReadOnly(true);
        district.setValue(event.getItem().getPresentDistrict().toString());
        H6 hDistrict = new H6("District:");
        hDistrict.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        district.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDistrict = new HorizontalLayout(hDistrict, district);
        horiDistrict.setWidth("530px");
        // END

        // Start
        TextField rmo = new TextField();
        rmo.setWidth("350px");
        rmo.setReadOnly(true);
        rmo.setValue(event.getItem().getPresentRmo().toString());
        H6 hRmo = new H6("RMO:");
        hRmo.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        rmo.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRmo = new HorizontalLayout(hRmo, rmo);
        horiRmo.setWidth("530px");
        // END

        // Start
        TextField cityMunicipality = new TextField();
        cityMunicipality.setWidth("350px");
        cityMunicipality.setReadOnly(true);
        cityMunicipality.setValue(event.getItem().getPresentMunicipality().toString());
        H6 hCityMunicipality = new H6("city or Municipality:");
        hCityMunicipality.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        cityMunicipality.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiCityMunicipality = new HorizontalLayout(hCityMunicipality, cityMunicipality);
        horiCityMunicipality.setWidth("530px");
        // END

        // Start
        TextField upozilla = new TextField();
        upozilla.setWidth("350px");

        upozilla.setReadOnly(true);
        upozilla.setValue(event.getItem().getPresentUpozila().toString());
        H6 hUpozilla = new H6("upozilla:");
        hUpozilla.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        upozilla.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUpozilla = new HorizontalLayout(hUpozilla, upozilla);
        horiUpozilla.setWidth("530px");
        // END

        // Start
        TextField union = new TextField();
        union.setWidth("350px");
        union.setReadOnly(true);
        union.setValue(event.getItem().getPresentUnion().toString());
        H6 hUnion = new H6("union/ward:");
        hUnion.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        union.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnion = new HorizontalLayout(hUnion, union);
        horiUnion.setWidth("530px");
        // END

        // Start
        TextField moholla = new TextField();
        moholla.setWidth("350px");
        moholla.setReadOnly(true);
        moholla.setValue(event.getItem().getPresentMoholla().toString());
        H6 hMoholla = new H6("moholla:");
        hMoholla.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        moholla.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiMoholla = new HorizontalLayout(hMoholla, moholla);
        horiMoholla.setWidth("530px");
        // END

        // Start
        TextField addMoholla = new TextField();
        addMoholla.setWidth("350px");
        addMoholla.setReadOnly(true);
        addMoholla.setValue(event.getItem().getPresentAddMoholla().toString());
        H6 hAddMoholla = new H6("Additional Moholla:");
        hAddMoholla.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        addMoholla.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAddMoholla = new HorizontalLayout(hAddMoholla, addMoholla);
        horiAddMoholla.setWidth("530px");
        // END

        // Start
        TextField unionPorishod = new TextField();
        unionPorishod.setWidth("350px");
        unionPorishod.setReadOnly(true);
        unionPorishod.setValue(event.getItem().getPresentWardUnionPorishod().toString());
        H6 hUnionPorishod = new H6("union Porishod:");
        hUnionPorishod.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        unionPorishod.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnionPorishod = new HorizontalLayout(hUnionPorishod, unionPorishod);
        horiUnionPorishod.setWidth("530px");
        // END
        // Start
        TextField village = new TextField();
        village.setWidth("350px");
        village.setReadOnly(true);
        village.setValue(event.getItem().getPresentVillage().toString());
        H6 hVillage = new H6("village/road:");
        hVillage.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        village.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiVillage = new HorizontalLayout(hVillage, village);
        horiVillage.setWidth("530px");
        // END

        // Start
        TextField additionalVillage = new TextField();
        additionalVillage.setWidth("350px");
        additionalVillage.setReadOnly(true);
        additionalVillage.setValue(event.getItem().getPresentAddVillage().toString());
        H6 hAdditionalVillage = new H6("additional Village:");
        hAdditionalVillage.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        additionalVillage.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAdditionalVillage = new HorizontalLayout(hAdditionalVillage, additionalVillage);
        horiAdditionalVillage.setWidth("530px");
        // END

        // Start
        TextField home = new TextField();
        home.setWidth("350px");
        home.setReadOnly(true);
        home.setValue(event.getItem().getPresentHome().toString());
        H6 hHome = new H6("Home/holding no.:");
        hHome.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        home.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiHome = new HorizontalLayout(hHome, home);
        horiHome.setWidth("530px");
        // END
        // Start
        TextField postOffice = new TextField();
        postOffice.setWidth("350px");
        postOffice.setReadOnly(true);
        postOffice.setValue(event.getItem().getPresentPostOffice().toString());
        H6 hPostOffice = new H6("post Office:");
        hPostOffice.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postOffice.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostOffice = new HorizontalLayout(hPostOffice, postOffice);
        horiPostOffice.setWidth("530px");
        // END
        // Start
        TextField postalCode = new TextField();
        postalCode.setWidth("350px");
        postalCode.setReadOnly(true);
        postalCode.setValue(event.getItem().getPresentPostalCode().toString());
        H6 hPostalCode = new H6("Postal Code:");
        hPostalCode.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postalCode.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostalCode = new HorizontalLayout(hPostalCode, postalCode);
        horiPostalCode.setWidth("530px");
        // END
        // Start
        TextField region = new TextField();
        region.setWidth("350px");
        region.setReadOnly(true);
        region.setValue(event.getItem().getPresentRegion().toString());
        H6 hRegion = new H6("Region:");
        hRegion.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        region.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRegion = new HorizontalLayout(hRegion, region);
        horiRegion.setWidth("530px");
        // END
        // Present Address END

        // Permanent Address Start
        // Start
        TextField division_Per = new TextField();
        division_Per.setWidth("350px");
        division_Per.setReadOnly(true);
        division_Per.setValue(event.getItem().getPermanentDivision().toString());
        H6 hDivision_Per = new H6("Division:");
        hDivision_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        division_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDivision_Per = new HorizontalLayout(hDivision_Per, division_Per);
        horiDivision_Per.setWidth("530px");
        // END

        // Start
        TextField district_Per = new TextField();
        district_Per.setWidth("350px");
        district_Per.setReadOnly(true);
        district_Per.setValue(event.getItem().getPermanentDistrict().toString());
        H6 hDistrict_Per = new H6("District:");
        hDistrict_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        district_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDistrict_Per = new HorizontalLayout(hDistrict_Per, district_Per);
        horiDistrict_Per.setWidth("530px");
        // END

        // Start
        TextField rmo_Per = new TextField();
        rmo_Per.setWidth("350px");
        rmo_Per.setReadOnly(true);
        rmo_Per.setValue(event.getItem().getPermanentRmo().toString());
        H6 hRmo_Per = new H6("RMO:");
        hRmo_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        rmo_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRmo_Per = new HorizontalLayout(hRmo_Per, rmo_Per);
        horiRmo_Per.setWidth("530px");
        // END

        // Start
        TextField cityMunicipality_Per = new TextField();
        cityMunicipality_Per.setWidth("350px");
        cityMunicipality_Per.setReadOnly(true);
        cityMunicipality.setValue(event.getItem().getPermanentMunicipality().toString());
        H6 hCityMunicipality_Per = new H6("city or Municipality:");
        hCityMunicipality_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        cityMunicipality_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiCityMunicipality_Per = new HorizontalLayout(hCityMunicipality_Per, cityMunicipality_Per);
        horiCityMunicipality_Per.setWidth("530px");
        // END

        // Start
        TextField upozilla_Per = new TextField();
        upozilla_Per.setWidth("350px");

        upozilla_Per.setReadOnly(true);
        upozilla_Per.setValue(event.getItem().getPermanentUpozila().toString());
        H6 hUpozilla_Per = new H6("upozilla:");
        hUpozilla_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        upozilla_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUpozilla_Per = new HorizontalLayout(hUpozilla_Per, upozilla_Per);
        horiUpozilla_Per.setWidth("530px");
        // END

        // Start
        TextField union_Per = new TextField();
        union_Per.setWidth("350px");
        union_Per.setReadOnly(true);
        union_Per.setValue(event.getItem().getPermanentUnion().toString());
        H6 hUnion_Per = new H6("union/ward:");
        hUnion_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        union_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnion_Per = new HorizontalLayout(hUnion_Per, union_Per);
        horiUnion_Per.setWidth("530px");
        // END

        // Start
        TextField moholla_Per = new TextField();
        moholla_Per.setWidth("350px");
        moholla_Per.setReadOnly(true);
        moholla_Per.setValue(event.getItem().getPermanentMoholla().toString());
        H6 hMoholla_Per = new H6("moholla:");
        hMoholla_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        moholla_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiMoholla_Per = new HorizontalLayout(hMoholla_Per, moholla_Per);
        horiMoholla_Per.setWidth("530px");
        // END

        // Start
        TextField addMoholla_Per = new TextField();
        addMoholla_Per.setWidth("350px");
        addMoholla_Per.setReadOnly(true);
        addMoholla_Per.setValue(event.getItem().getPermanentAddMoholla().toString());
        H6 hAddMoholla_Per = new H6("Additional Moholla:");
        hAddMoholla_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        addMoholla_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAddMoholla_Per = new HorizontalLayout(hAddMoholla_Per, addMoholla_Per);
        horiAddMoholla_Per.setWidth("530px");
        // END

        // Start
        TextField unionPorishod_Per = new TextField();
        unionPorishod_Per.setWidth("350px");
        unionPorishod_Per.setReadOnly(true);
        unionPorishod_Per.setValue(event.getItem().getPermanentWardUnionPorishod().toString());
        H6 hUnionPorishod_Per = new H6("union Porishod:");
        hUnionPorishod_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        unionPorishod_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnionPorishod_Per = new HorizontalLayout(hUnionPorishod_Per, unionPorishod_Per);
        horiUnionPorishod_Per.setWidth("530px");
        // END
        // Start
        TextField village_Per = new TextField();
        village_Per.setWidth("350px");
        village_Per.setReadOnly(true);
        village_Per.setValue(event.getItem().getPermanentVillage().toString());
        H6 hVillage_Per = new H6("village/road:");
        hVillage_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        village_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiVillage_Per = new HorizontalLayout(hVillage_Per, village_Per);
        horiVillage_Per.setWidth("530px");
        // END

        // Start
        TextField additionalVillage_Per = new TextField();
        additionalVillage_Per.setWidth("350px");
        additionalVillage_Per.setReadOnly(true);
        additionalVillage_Per.setValue(event.getItem().getPermanentAddVillage().toString());
        H6 hAdditionalVillage_Per = new H6("additional Village:");
        hAdditionalVillage_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        additionalVillage_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAdditionalVillage_Per = new HorizontalLayout(hAdditionalVillage_Per,
                additionalVillage_Per);
        horiAdditionalVillage_Per.setWidth("530px");
        // END

        // Start
        TextField home_Per = new TextField();
        home_Per.setWidth("350px");
        home_Per.setReadOnly(true);
        home_Per.setValue(event.getItem().getPermanentHome().toString());
        H6 hHome_Per = new H6("Home/holding no.:");
        hHome_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        home_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiHome_Per = new HorizontalLayout(hHome_Per, home_Per);
        horiHome_Per.setWidth("530px");
        // END
        // Start
        TextField postOffice_Per = new TextField();
        postOffice_Per.setWidth("350px");
        postOffice_Per.setReadOnly(true);
        postOffice_Per.setValue(event.getItem().getPermanentPostOffice().toString());
        H6 hPostOffice_Per = new H6("post Office:");
        hPostOffice_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postOffice_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostOffice_Per = new HorizontalLayout(hPostOffice_Per, postOffice_Per);
        horiPostOffice_Per.setWidth("530px");
        // END
        // Start
        TextField postalCode_Per = new TextField();
        postalCode_Per.setWidth("350px");
        postalCode_Per.setReadOnly(true);
        postalCode_Per.setValue(event.getItem().getPermanentPostalCode().toString());
        H6 hPostalCode_Per = new H6("Postal Code:");
        hPostalCode_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postalCode_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostalCode_Per = new HorizontalLayout(hPostalCode_Per, postalCode_Per);
        horiPostalCode_Per.setWidth("530px");
        // END
        // Start
        TextField region_Per = new TextField();
        region_Per.setWidth("350px");
        region_Per.setReadOnly(true);
        region_Per.setValue(event.getItem().getPermanentRegion().toString());
        H6 hRegion_Per = new H6("Region:");
        hRegion_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        region_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRegion_Per = new HorizontalLayout(hRegion_Per, region_Per);
        horiRegion_Per.setWidth("530px");
        // END
        // Present Address END

        Image personImage = new Image(DUMMY_IMAGE, "");
        personImage.getStyle().set("margin-top", "20px");
        personImage.setWidth("120px");
        personImage.setHeight("120px");

        VerticalLayout verti = new VerticalLayout(horiNameBn, horiNameEn, horidob, horiOccupation);
        // verti.getStyle().set("background", "red");
        verti.setWidth("330px");
        verti.setSpacing(false);

        VerticalLayout verti2 = new VerticalLayout(horiFatherName, horiMotherName, horiSpouseName);
        // verti2.getStyle().set("background", "green");
        verti2.setWidth("330px");
        verti2.setSpacing(false);

        VerticalLayout verti3 = new VerticalLayout(horiNationalID, horipin, horibloodGrp);
        // verti2.getStyle().set("background", "green");
        verti3.setWidth("330px");
        verti3.setSpacing(false);

        H4 presentH3 = new H4("Present Address");
        presentH3.setClassName("top-heading");
        VerticalLayout leftBar = new VerticalLayout(presentH3, horiDivision, horiDistrict, horiRmo, horiUpozilla,
                horiUnion, horiMoholla, horiAddMoholla, horiUnionPorishod, horiVillage, horiAdditionalVillage, horiHome,
                horiPostOffice, horiPostalCode, horiRegion);
        // leftBar.getStyle().set("background", "green");
        leftBar.setWidth("550px");
        leftBar.setSpacing(false);

        H4 permanentH3 = new H4("Permanent Address");
        permanentH3.setClassName("top-heading");

        VerticalLayout rightBar = new VerticalLayout(permanentH3, horiDivision_Per, horiDistrict_Per, horiRmo_Per,
                horiUpozilla_Per, horiUnion_Per, horiMoholla_Per, horiAddMoholla_Per, horiUnionPorishod_Per,
                horiVillage_Per, horiAdditionalVillage_Per, horiHome_Per, horiPostOffice_Per, horiPostalCode_Per,
                horiRegion_Per);
        rightBar.getStyle().set("margin-left", "auto");

        rightBar.setWidth("550px");
        rightBar.setSpacing(false);

        HorizontalLayout topBar = new HorizontalLayout(personImage, verti, verti2, verti3);
        HorizontalLayout midBar = new HorizontalLayout(leftBar, rightBar);
        topBar.getStyle().set("margin", "0px 10px 0px 10px");
        mainDiv.add(heading,topBar, midBar);
        add(mainDiv);

        return mainDiv;
    }

        private Div historyDialogLayout(Dialog dialog, NationalId nidRs) {



        Div mainDiv = new Div();
        mainDiv.setWidthFull();
        mainDiv.setClassName("div-border2");
        mainDiv.getStyle().set("margin", "0px 0px 20px 0px");
        mainDiv.getStyle().set("background", "#ffffff");

        Div heading = new Div();
        heading.setWidthFull();
        heading.getStyle().set("background", "#00114F");
        heading.setHeight("30px");
        Span span = new Span("NID Full Detail of: " + nidRs.getNameEnglish());
        span.getStyle().set("color", "#ffffff").set("margin", "0px 0px 0px 15px");



        Span exit = new Span(VaadinIcon.CLOSE_CIRCLE.create());
        exit.addClickListener(e->{
            dialog.close();
        });
        exit.setId("exit-btn");
        //exit.addThemeVariants(ButtonVariant.LUMO_SMALL);
        //exitdialog.setWidth("5px");
        exit.getStyle().set("margin-left", "96.5%");

        //heading.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        heading.add(exit);


        // Start
        TextField nameBn = new TextField();
        nameBn.setReadOnly(true);
        nameBn.setValue(nidRs.getNameBangla());
        //nameBn.setValue(event.getItem().getNameBangla().toString());
        H6 hNameBn = new H6("Name (Bangla):");
        hNameBn.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        nameBn.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiNameBn = new HorizontalLayout(hNameBn, nameBn);
        horiNameBn.setWidth("300px");
        // END

        // Start
        TextField nameEn = new TextField();
        nameEn.setReadOnly(true);
        nameEn.setValue(nidRs.getNameEnglish());
        //nameEn.setValue(event.getItem().getNameEnglish().toString());
        H6 hNameEn = new H6("Name (English):");
        hNameEn.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        nameEn.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiNameEn = new HorizontalLayout(hNameEn, nameEn);
        horiNameEn.setWidth("300px");
        // END
        // Start
        TextField dob = new TextField();
        dob.setReadOnly(true);
        dob.setValue(nidRs.getDateOfBirth().toString());
        //dob.setValue(event.getItem().getDateOfBirth().toString());
        H6 hdob = new H6("Date of Birth:");
        hdob.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        dob.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horidob = new HorizontalLayout(hdob, dob);
        horidob.setWidth("300px");
        // END

        // Start
        TextField FatherName = new TextField();
        FatherName.setReadOnly(true);
        FatherName.setValue(nidRs.getFatherName());
        //FatherName.setValue(event.getItem().getFatherName().toString());
        H6 hFatherName = new H6("Father Name:");
        hFatherName.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        FatherName.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiFatherName = new HorizontalLayout(hFatherName, FatherName);
        horiFatherName.setWidth("300px");
        // horiName.setAlignItems(FlexComponent.Alignment.CENTER);
        // END

//		// Start 2nd section
        TextField MotherName = new TextField();
        MotherName.setReadOnly(true);
        MotherName.setValue(nidRs.getMotherName());

        H6 hMotherName = new H6("Mother Name:");
        hMotherName.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        MotherName.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiMotherName = new HorizontalLayout(hMotherName, MotherName);
        horiMotherName.setWidth("300px");
        // END

        // Start
        TextField SpouseName = new TextField();
        SpouseName.setReadOnly(true);
        SpouseName.setValue(nidRs.getSpouseName());

        H6 hSpouseName = new H6("Spouse Name:");
        hSpouseName.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        SpouseName.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiSpouseName = new HorizontalLayout(hSpouseName, SpouseName);
        horiSpouseName.setWidth("300px");
        // END

        // Start
        TextField Occupation = new TextField();
        Occupation.setReadOnly(true);
        Occupation.setValue(nidRs.getOccupation());

        H6 hOccupation = new H6("Occupation:");
        hOccupation.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        Occupation.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiOccupation = new HorizontalLayout(hOccupation, Occupation);
        horiOccupation.setWidth("300px");
        // END

        // Start 3rd section
        TextField nationalID = new TextField();
        nationalID.setReadOnly(true);
        nationalID.setValue(nidRs.getNid());

        H6 hNationalID = new H6("National ID:");
        hNationalID.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        nationalID.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiNationalID = new HorizontalLayout(hNationalID, nationalID);
        horiNationalID.setWidth("300px");
        // END

        // Start
        TextField bloodGrp = new TextField();
        bloodGrp.setReadOnly(true);
        bloodGrp.setValue(nidRs.getBloodGroup());

        H6 hbloodGrp = new H6("Blood Group:");
        hbloodGrp.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        bloodGrp.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horibloodGrp = new HorizontalLayout(hbloodGrp, bloodGrp);
        horibloodGrp.setWidth("300px");
        // END

        // Start
        TextField pin = new TextField();
        pin.setReadOnly(true);
        pin.setValue(nidRs.getNidPin());

        H6 hpin = new H6("PIN:");
        hpin.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        pin.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horipin = new HorizontalLayout(hpin, pin);
        horipin.setWidth("300px");
        // END

        // Present Address Start
        // Start
        TextField division = new TextField();
        division.setWidth("350px");
        division.setReadOnly(true);
        division.setValue(nidRs.getPresentDivision());

        H6 hDivision = new H6("Division:");
        hDivision.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        division.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDivision = new HorizontalLayout(hDivision, division);
        horiDivision.setWidth("530px");
        // END

        // Start
        TextField district = new TextField();
        district.setWidth("350px");
        district.setReadOnly(true);
        district.setValue(nidRs.getPresentDistrict());

        H6 hDistrict = new H6("District:");
        hDistrict.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        district.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDistrict = new HorizontalLayout(hDistrict, district);
        horiDistrict.setWidth("530px");
        // END

        // Start
        TextField rmo = new TextField();
        rmo.setWidth("350px");
        rmo.setReadOnly(true);
        rmo.setValue(nidRs.getPresentRmo());

        H6 hRmo = new H6("RMO:");
        hRmo.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        rmo.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRmo = new HorizontalLayout(hRmo, rmo);
        horiRmo.setWidth("530px");
        // END

        // Start
        TextField cityMunicipality = new TextField();
        cityMunicipality.setWidth("350px");
        cityMunicipality.setReadOnly(true);
        cityMunicipality.setValue(nidRs.getPresentMunicipality());

        H6 hCityMunicipality = new H6("city or Municipality:");
        hCityMunicipality.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        cityMunicipality.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiCityMunicipality = new HorizontalLayout(hCityMunicipality, cityMunicipality);
        horiCityMunicipality.setWidth("530px");
        // END

        // Start
        TextField upozilla = new TextField();
        upozilla.setWidth("350px");
        upozilla.setReadOnly(true);
        upozilla.setValue(nidRs.getPresentUpozila());

        H6 hUpozilla = new H6("upozilla:");
        hUpozilla.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        upozilla.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUpozilla = new HorizontalLayout(hUpozilla, upozilla);
        horiUpozilla.setWidth("530px");
        // END

        // Start
        TextField union = new TextField();
        union.setWidth("350px");
        union.setReadOnly(true);
        union.setValue(nidRs.getPresentUnion());

        H6 hUnion = new H6("union/ward:");
        hUnion.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        union.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnion = new HorizontalLayout(hUnion, union);
        horiUnion.setWidth("530px");
        // END

        // Start
        TextField moholla = new TextField();
        moholla.setWidth("350px");
        moholla.setReadOnly(true);
        moholla.setValue(nidRs.getPresentMoholla());

        H6 hMoholla = new H6("moholla:");
        hMoholla.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        moholla.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiMoholla = new HorizontalLayout(hMoholla, moholla);
        horiMoholla.setWidth("530px");
        // END

        // Start
        TextField addMoholla = new TextField();
        addMoholla.setWidth("350px");
        addMoholla.setReadOnly(true);
        addMoholla.setValue(nidRs.getPresentAddMoholla());

        H6 hAddMoholla = new H6("Additional Moholla:");
        hAddMoholla.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        addMoholla.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAddMoholla = new HorizontalLayout(hAddMoholla, addMoholla);
        horiAddMoholla.setWidth("530px");
        // END

        // Start
        TextField unionPorishod = new TextField();
        unionPorishod.setWidth("350px");
        unionPorishod.setReadOnly(true);
        unionPorishod.setValue(nidRs.getPresentWardUnionPorishod());

        H6 hUnionPorishod = new H6("union Porishod:");
        hUnionPorishod.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        unionPorishod.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnionPorishod = new HorizontalLayout(hUnionPorishod, unionPorishod);
        horiUnionPorishod.setWidth("530px");
        // END
        // Start
        TextField village = new TextField();
        village.setWidth("350px");
        village.setReadOnly(true);
        village.setValue(nidRs.getPresentVillage());

        H6 hVillage = new H6("village/road:");
        hVillage.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        village.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiVillage = new HorizontalLayout(hVillage, village);
        horiVillage.setWidth("530px");
        // END

        // Start
        TextField additionalVillage = new TextField();
        additionalVillage.setWidth("350px");
        additionalVillage.setReadOnly(true);
        additionalVillage.setValue(nidRs.getPresentAddVillage());

        H6 hAdditionalVillage = new H6("additional Village:");
        hAdditionalVillage.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        additionalVillage.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAdditionalVillage = new HorizontalLayout(hAdditionalVillage, additionalVillage);
        horiAdditionalVillage.setWidth("530px");
        // END

        // Start
        TextField home = new TextField();
        home.setWidth("350px");
        home.setReadOnly(true);
        home.setValue(nidRs.getPresentHome());

        H6 hHome = new H6("Home/holding no.:");
        hHome.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        home.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiHome = new HorizontalLayout(hHome, home);
        horiHome.setWidth("530px");
        // END
        // Start
        TextField postOffice = new TextField();
        postOffice.setWidth("350px");
        postOffice.setReadOnly(true);
        postOffice.setValue(nidRs.getPresentPostOffice());

        H6 hPostOffice = new H6("post Office:");
        hPostOffice.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postOffice.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostOffice = new HorizontalLayout(hPostOffice, postOffice);
        horiPostOffice.setWidth("530px");
        // END
        // Start
        TextField postalCode = new TextField();
        postalCode.setWidth("350px");
        postalCode.setReadOnly(true);
        postalCode.setValue(nidRs.getPresentPostalCode());

        H6 hPostalCode = new H6("Postal Code:");
        hPostalCode.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postalCode.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostalCode = new HorizontalLayout(hPostalCode, postalCode);
        horiPostalCode.setWidth("530px");
        // END
        // Start
        TextField region = new TextField();
        region.setWidth("350px");
        region.setReadOnly(true);
        region.setValue(nidRs.getPresentRegion());
        H6 hRegion = new H6("Region:");
        hRegion.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        region.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRegion = new HorizontalLayout(hRegion, region);
        horiRegion.setWidth("530px");
        // END
        // Present Address END
//
        // Permanent Address Start
        // Start
        TextField division_Per = new TextField();
        division_Per.setWidth("350px");
        division_Per.setReadOnly(true);
        division_Per.setValue(nidRs.getPermanentDivision());

        H6 hDivision_Per = new H6("Division:");
        hDivision_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        division_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDivision_Per = new HorizontalLayout(hDivision_Per, division_Per);
        horiDivision_Per.setWidth("530px");
        // END

        // Start
        TextField district_Per = new TextField();
        district_Per.setWidth("350px");
        district_Per.setReadOnly(true);
        district_Per.setValue(nidRs.getPermanentDistrict());

        H6 hDistrict_Per = new H6("District:");
        hDistrict_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        district_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiDistrict_Per = new HorizontalLayout(hDistrict_Per, district_Per);
        horiDistrict_Per.setWidth("530px");
        // END

        // Start
        TextField rmo_Per = new TextField();
        rmo_Per.setWidth("350px");
        rmo_Per.setReadOnly(true);
        rmo_Per.setValue(nidRs.getPermanentRmo());

        H6 hRmo_Per = new H6("RMO:");
        hRmo_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        rmo_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRmo_Per = new HorizontalLayout(hRmo_Per, rmo_Per);
        horiRmo_Per.setWidth("530px");
        // END

        // Start
        TextField cityMunicipality_Per = new TextField();
        cityMunicipality_Per.setWidth("350px");
        cityMunicipality_Per.setReadOnly(true);
        cityMunicipality_Per.setValue(nidRs.getPermanentMunicipality());

        H6 hCityMunicipality_Per = new H6("city or Municipality:");
        hCityMunicipality_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        cityMunicipality_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiCityMunicipality_Per = new HorizontalLayout(hCityMunicipality_Per, cityMunicipality_Per);
        horiCityMunicipality_Per.setWidth("530px");
        // END

        // Start
        TextField upozilla_Per = new TextField();
        upozilla_Per.setWidth("350px");
        upozilla_Per.setReadOnly(true);
        upozilla_Per.setValue(nidRs.getPermanentUpozila());
        H6 hUpozilla_Per = new H6("upozilla:");
        hUpozilla_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        upozilla_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUpozilla_Per = new HorizontalLayout(hUpozilla_Per, upozilla_Per);
        horiUpozilla_Per.setWidth("530px");
        // END
//
        // Start
        TextField union_Per = new TextField();
        union_Per.setWidth("350px");
        union_Per.setReadOnly(true);
        union_Per.setValue(nidRs.getPermanentUnion());
        H6 hUnion_Per = new H6("union/ward:");
        hUnion_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        union_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnion_Per = new HorizontalLayout(hUnion_Per, union_Per);
        horiUnion_Per.setWidth("530px");
        // END

        // Start
        TextField moholla_Per = new TextField();
        moholla_Per.setWidth("350px");
        moholla_Per.setReadOnly(true);
        moholla_Per.setValue(nidRs.getPermanentMoholla());
        H6 hMoholla_Per = new H6("moholla:");
        hMoholla_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        moholla_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiMoholla_Per = new HorizontalLayout(hMoholla_Per, moholla_Per);
        horiMoholla_Per.setWidth("530px");
        // END

        // Start
        TextField addMoholla_Per = new TextField();
        addMoholla_Per.setWidth("350px");
        addMoholla_Per.setReadOnly(true);
        addMoholla_Per.setValue(nidRs.getPermanentAddMoholla());
        H6 hAddMoholla_Per = new H6("Additional Moholla:");
        hAddMoholla_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        addMoholla_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAddMoholla_Per = new HorizontalLayout(hAddMoholla_Per, addMoholla_Per);
        horiAddMoholla_Per.setWidth("530px");
        // END

        // Start
        TextField unionPorishod_Per = new TextField();
        unionPorishod_Per.setWidth("350px");
        unionPorishod_Per.setReadOnly(true);
        unionPorishod_Per.setValue(nidRs.getPermanentWardUnionPorishod());
        H6 hUnionPorishod_Per = new H6("union Porishod:");
        hUnionPorishod_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        unionPorishod_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiUnionPorishod_Per = new HorizontalLayout(hUnionPorishod_Per, unionPorishod_Per);
        horiUnionPorishod_Per.setWidth("530px");
        // END
        // Start
        TextField village_Per = new TextField();
        village_Per.setWidth("350px");
        village_Per.setReadOnly(true);
        village_Per.setValue(nidRs.getPermanentVillage());
        H6 hVillage_Per = new H6("village/road:");
        hVillage_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        village_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiVillage_Per = new HorizontalLayout(hVillage_Per, village_Per);
        horiVillage_Per.setWidth("530px");
        // END

        // Start
        TextField additionalVillage_Per = new TextField();
        additionalVillage_Per.setWidth("350px");
        additionalVillage_Per.setReadOnly(true);
        additionalVillage_Per.setValue(nidRs.getPermanentAddVillage());
        H6 hAdditionalVillage_Per = new H6("additional Village:");
        hAdditionalVillage_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        additionalVillage_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiAdditionalVillage_Per = new HorizontalLayout(hAdditionalVillage_Per,
                additionalVillage_Per);
        horiAdditionalVillage_Per.setWidth("530px");
        // END

        // Start
        TextField home_Per = new TextField();
        home_Per.setWidth("350px");
        home_Per.setReadOnly(true);
        home_Per.setValue(nidRs.getPermanentHome());
        H6 hHome_Per = new H6("Home/holding no.:");
        hHome_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        home_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiHome_Per = new HorizontalLayout(hHome_Per, home_Per);
        horiHome_Per.setWidth("530px");
        // END
        // Start
        TextField postOffice_Per = new TextField();
        postOffice_Per.setWidth("350px");
        postOffice_Per.setReadOnly(true);
        postOffice_Per.setValue(nidRs.getPermanentPostalCode());
        H6 hPostOffice_Per = new H6("post Office:");
        hPostOffice_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postOffice_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostOffice_Per = new HorizontalLayout(hPostOffice_Per, postOffice_Per);
        horiPostOffice_Per.setWidth("530px");
        // END
        // Start
        TextField postalCode_Per = new TextField();
        postalCode_Per.setWidth("350px");
        postalCode_Per.setReadOnly(true);
        postalCode_Per.setValue(nidRs.getPermanentPostalCode());
        H6 hPostalCode_Per = new H6("Postal Code:");
        hPostalCode_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        postalCode_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiPostalCode_Per = new HorizontalLayout(hPostalCode_Per, postalCode_Per);
        horiPostalCode_Per.setWidth("530px");
        // END
        // Start
        TextField region_Per = new TextField();
        region_Per.setWidth("350px");
        region_Per.setReadOnly(true);
        region_Per.setValue(nidRs.getPermanentRegion());
        H6 hRegion_Per = new H6("Region:");
        hRegion_Per.getStyle().set("margin-top", "2px").set("color", "#00114F").set("font-weight", "bold");
        region_Per.getStyle().set("font-size", "14px").set("margin-left", "auto").set("margin-top", "-5px");
        HorizontalLayout horiRegion_Per = new HorizontalLayout(hRegion_Per, region_Per);
        horiRegion_Per.setWidth("530px");
        // END
        // Present Address END

        Image personImage = new Image(DUMMY_IMAGE, "");
        personImage.getStyle().set("margin-top", "20px");
        personImage.setWidth("120px");
        personImage.setHeight("120px");

        VerticalLayout verti = new VerticalLayout(horiNameBn, horiNameEn, horidob, horiOccupation);
        // verti.getStyle().set("background", "red");
        verti.setWidth("330px");
        verti.setSpacing(false);

        VerticalLayout verti2 = new VerticalLayout(horiFatherName, horiMotherName, horiSpouseName);
        // verti2.getStyle().set("background", "green");
        verti2.setWidth("330px");
        verti2.setSpacing(false);

        VerticalLayout verti3 = new VerticalLayout(horiNationalID, horipin, horibloodGrp);
        // verti2.getStyle().set("background", "green");
        verti3.setWidth("330px");
        verti3.setSpacing(false);

        H4 presentH3 = new H4("Present Address");
        presentH3.setClassName("top-heading");
        VerticalLayout leftBar = new VerticalLayout(presentH3, horiDivision, horiDistrict, horiRmo, horiUpozilla,
                horiUnion, horiMoholla, horiAddMoholla, horiUnionPorishod, horiVillage, horiAdditionalVillage, horiHome,
                horiPostOffice, horiPostalCode, horiRegion);
        // leftBar.getStyle().set("background", "green");
        leftBar.setWidth("550px");
        leftBar.setSpacing(false);

        H4 permanentH3 = new H4("Permanent Address");
        permanentH3.setClassName("top-heading");

        VerticalLayout rightBar = new VerticalLayout(permanentH3, horiDivision_Per, horiDistrict_Per, horiRmo_Per,
                horiUpozilla_Per, horiUnion_Per, horiMoholla_Per, horiAddMoholla_Per, horiUnionPorishod_Per,
                horiVillage_Per, horiAdditionalVillage_Per, horiHome_Per, horiPostOffice_Per, horiPostalCode_Per,
                horiRegion_Per);
        rightBar.getStyle().set("margin-left", "auto");

        rightBar.setWidth("550px");
        rightBar.setSpacing(false);

        HorizontalLayout topBar = new HorizontalLayout(personImage, verti, verti2, verti3);
        HorizontalLayout midBar = new HorizontalLayout(leftBar, rightBar);
        topBar.getStyle().set("margin", "0px 10px 0px 10px");
        midBar.getStyle().set("margin", "0px 10px 0px 0px");
        mainDiv.add(heading,topBar, midBar);
        add(mainDiv);

        return mainDiv;
    }


        public Grid<NationalId> nidDetail(TextField nid, DatePicker date, String actionType) {

        NationalId nidRs = new NationalId();
        LocalDate date2 = date.getValue();
        // LocalDate date2 = LocalDate.of(1994, 2, 2);
        nidRs.setDateOfBirth(date2);
        nidRs.setUserModKey(111148);
        nidRs.setNid(nid.getValue().trim());

        // nidRs.setNid("2854356249");
        nidRs.setForceFatch(false);
        nidRs.setSource("NID GUI");
        nidRs.setActionType(actionType);

        List<NationalId> nidList = new ArrayList<NationalId>();
        String sendjson = gson.toJson(nidRs);
        try {
            response = RequestSender.send4Str(nidUrl, sendjson);
            NidStatusResponce responseStatus = gson.fromJson(response, NidStatusResponce.class);
//				if()
            for (NationalId nationalId : responseStatus.getNationalId()) {
                log.info("Responce [{}]", nationalId.toString());
                nidList.add(nationalId);
            }
            grid_1.setItems(nidList);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return grid_1;

    }

        public void nidHistoryDetail(String nid, String date3, String actionType) {

        NationalId nidRs = new NationalId();
        LocalDate date2 = LocalDate.parse(date3);
        // LocalDate date2 = LocalDate.of(1994, 2, 2);
        nidRs.setDateOfBirth(date2);
        nidRs.setUserModKey(111148);
        nidRs.setNid(nid);
        // nidRs.setNid("2854356249");
        nidRs.setForceFatch(false);
        nidRs.setSource("NID GUI");
        nidRs.setActionType(actionType);

        List<NationalId> nidList = new ArrayList<NationalId>();
        String sendjson = gson.toJson(nidRs);
        Dialog dialog = new Dialog();
        dialog.removeAll();
        try {
            response = RequestSender.send4Str(nidUrl, sendjson);
            NidStatusResponce responseStatus = gson.fromJson(response, NidStatusResponce.class);
//				if()
            for (NationalId nationalId : responseStatus.getNationalId()) {
                log.info("Responce [{}]", nationalId.toString());


                Div editDialogLayout = historyDialogLayout(dialog, nationalId);
                dialog.add(editDialogLayout);
                //dialog.add(editDialogLayout);
                dialog.open();
                dialog.getElement().setAttribute("aria-label", "Add note");
                dialog.setHeight("calc(85vh - (2*var(--lumo-space-s)))");
                dialog.setWidth("calc(90vw - (4*var(--lumo-space-s)))");
                dialog.getElement().setAttribute("aria-label", "Add note");

            }
            //grid_1.setItems(nidList);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    }

        public Grid<NationalId> history() {

        NationalId nidRs = new NationalId();
        nidRs.setUserModKey(111148);
        nidRs.setActionType("SELECT_REQUEST_HISTORY");
        List<NationalId> individualData = new ArrayList<NationalId>();
        String sendjson = gson.toJson(nidRs);
        try {
            response = RequestSender.send4Str(nidUrl, sendjson);
            NidStatusResponce responseStatus = gson.fromJson(response, NidStatusResponce.class);
//				if()
            for (NationalId nationalId : responseStatus.getNationalId()) {
                log.info("Responce [{}]", nationalId.toString());
                individualData.add(nationalId);
            }
            grid_2.setItems(individualData);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return grid_2;

    }

    }

