package com.example.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.data.binder.Binder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Route
public class MainView extends VerticalLayout {
    private Repository repo;
    private TextField firstName=new TextField("First Name");
    private TextField lastName=new TextField("Last name");
    private EmailField email=new EmailField("Email");
    private Binder<Person> binder=new Binder<>(Person.class);
    private Grid<Person> table=new Grid<>(Person.class);


    public MainView(Repository repo) {
        this.repo = repo;
        table.setColumns("firstName","lastName","email");
        add(getForm(),table);
        refreshGrid();
    }

    private Component getForm() {
        var layout=new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);
        var addButton=new Button("Add");
        addButton.addClickShortcut(Key.ENTER);
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(firstName,lastName,email,addButton);
        binder.bindInstanceFields(this);
        addButton.addClickListener(click -> {
           try {
              var person=new Person();
              binder.writeBean(person);
              repo.save(person);
              binder.readBean(new Person());
              refreshGrid();
           }catch(ValidationException e) {
               ///
           }
        });
        return layout;
    }

    private void refreshGrid() {
        table.setItems(repo.findAll());
    }

}
