package es.javier.vistas;

import es.javier.logica.Logica;
import es.javier.models.Partido;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;


public class PantallaPrincipal extends Application {

    public void start(Stage stage) throws Exception {

        cargarFichero();

        stage.setTitle("Programa para la gestión de partidos");
        TableView tableViewPartidos = new TableView(Logica.getINSTANCE().getListaPartidos());

        AnchorPane.setTopAnchor(tableViewPartidos, 10d);
        AnchorPane.setLeftAnchor(tableViewPartidos, 10d);
        AnchorPane.setRightAnchor(tableViewPartidos, 10d);
        AnchorPane.setBottomAnchor(tableViewPartidos, 50d);

        TableColumn<String, Partido> column1 = new TableColumn<>("Equipo local");
        column1.setCellValueFactory(new PropertyValueFactory<>("equipoloc"));

        TableColumn<String, Partido> column2 = new TableColumn<>("Equipo visitante");
        column2.setCellValueFactory(new PropertyValueFactory<>("equipovis"));

        TableColumn<String, Partido> column3 = new TableColumn<>("Resultado");
        column3.setCellValueFactory(new PropertyValueFactory<>("resultado"));

        TableColumn<ObservableList, Partido> column4 = new TableColumn<>("División");
        column4.setCellValueFactory(new PropertyValueFactory<>("division"));

        TableColumn<Date, Partido> column5 = new TableColumn<>("Fecha");
        column5.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tableViewPartidos.getColumns().addAll(column1, column2, column3, column4, column5);

        Button botonalta = new Button("Alta");
        AnchorPane.setBottomAnchor(botonalta, 10d);
        AnchorPane.setLeftAnchor(botonalta, 10d);

        Button botonborrar = new Button("Borrar");
        AnchorPane.setBottomAnchor(botonborrar, 10d);
        AnchorPane.setRightAnchor(botonborrar, 10d);

        Button botonmodificar = new Button("Modificar");
        AnchorPane.setLeftAnchor(botonmodificar, 60d);
        AnchorPane.setBottomAnchor(botonmodificar, 10d);

        botonalta.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DialogoPartido dialogoPartido = new DialogoPartido();
                dialogoPartido.show();
            }
        });

        botonborrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alertconf = new Alert(Alert.AlertType.CONFIRMATION);
                alertconf.setTitle("Opción borrar");
                alertconf.setHeaderText("Borrado de partido");
                alertconf.setContentText("¿Seguro que quiere borrar el partido seleccionado?");
                alertconf.showAndWait();
                if ((alertconf.getResult() == ButtonType.OK)) {
                    int indiceBorrar = tableViewPartidos.getSelectionModel().getSelectedIndex();
                    if (indiceBorrar >= 0) {
                        Logica.getINSTANCE().borrarPartido(indiceBorrar);
                    } else {
                        Alert alerterror = new Alert(Alert.AlertType.ERROR);
                        alerterror.setTitle("Ventana de error");
                        alerterror.setHeaderText("Error al borrar el partido");
                        alerterror.setContentText("No ha seleccionado ningún partido para ser borrado");
                        alerterror.show();
                    }
                }
            }
        });

        botonmodificar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    int indice = tableViewPartidos.getSelectionModel().getSelectedIndex();
                    Partido partidoSeleccionado = Logica.getINSTANCE().getListaPartidos().get(indice);
                    DialogoPartido dmodificar = new DialogoPartido(partidoSeleccionado, indice);
                    dmodificar.show();
                } catch (IndexOutOfBoundsException e) {
                    Alert alerterror = new Alert(Alert.AlertType.ERROR);
                    alerterror.setTitle("Ventana de error");
                    alerterror.setHeaderText("Error al modificar el partido");
                    alerterror.setContentText("No ha seleccionado ningún partido para ser modificado");
                    alerterror.show();
                }
            }
        });

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ObservableList<Partido> listaPartidos = Logica.getINSTANCE().getListaPartidos(); //lo creé aquí e hice el paso por parámetro por cambiar un poco respecto al método cargarFichero()
                crearFichero(listaPartidos);
            }
        });

        ImageView selectedImage = new ImageView();
        Image image1 = new Image(new FileInputStream("src\\es\\javier\\resources\\futbol.jpg"));
        selectedImage.setFitHeight(100);
        selectedImage.setFitWidth(100);

        BackgroundImage imgbackground = new BackgroundImage(image1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        tableViewPartidos.setBackground(new Background(imgbackground));

        AnchorPane anchorPane = new AnchorPane(tableViewPartidos, botonalta, botonborrar, botonmodificar, selectedImage);
        Scene scene = new Scene(anchorPane, 420, 300);
        stage.setScene(scene);
        stage.show();
    }

    public void crearFichero(ObservableList<Partido> listaObservable) {
        try {
            FileOutputStream fos = new FileOutputStream(new File("partidos.dat"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<Partido>(listaObservable));
            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error al buscar el fichero");
        } catch (IOException e) {
            System.out.println("Error al inicializar el fichero");
            e.printStackTrace();
        }
    }

    public void cargarFichero() {
        try {
            ObservableList<Partido> listaObservable = Logica.getINSTANCE().getListaPartidos();
            FileInputStream fis = new FileInputStream(new File("partidos.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Partido> listaNoObservable = new ArrayList<>(listaObservable);
            listaNoObservable = (ArrayList<Partido>) ois.readObject();
            listaObservable.addAll(listaNoObservable);
            fis.close();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("La primera vez que ejecutes no estará creado el fichero... ¡pero no hay problema alguno, pues se creará cuando cierres el programa! " +
                    "Así pues, nunca más volverás a ver este mensaje... (a no ser que borres el fichero)");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

}
