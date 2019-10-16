package es.javier.vistas;

import es.javier.logica.Logica;
import es.javier.models.Partido;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;


public class DialogoPartido extends Stage {

    private TextField equipoloctf;
    private TextField equipovistf;
    private DatePicker fechadp;
    private TextField resultadotf;
    private Button botonAceptar;
    private ComboBox divisioncb;
    private final int MAXLENGTH = 5;  //variable que uso en el método LimiteDigitos


    public DialogoPartido() { //Añadir
        inicializaVista();
        botonAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addPartido();
            }
        });

    }

    public DialogoPartido(Partido partido, int posicion) { //modificar
        inicializaVista();
        equipoloctf.setText(partido.getEquipoloc());
        equipovistf.setText(partido.getEquipovis());
        resultadotf.setText(partido.getResultado());
        fechadp.setValue(partido.getFecha());
        botonAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String eloc = equipoloctf.getText();
                String evis = equipovistf.getText();
                String resul = resultadotf.getText();
                String div = divisioncb.getSelectionModel().getSelectedItem().toString();
                LocalDate fecha = fechadp.getValue();
                Partido partido = new Partido(eloc, evis, resul, div, fecha);
                Logica.getINSTANCE().modificarPartido(partido, posicion);

                divisioncb.getSelectionModel().clearSelection(); // Estas dos líneas son porque, cada vez que le doy a alta, los valores que recoge combobox de listaCombobox (primera,segunda y tercera)
                divisioncb.setValue(null); //se van añadiendo de dos en dos, osea, la primera vez que de de alta estará bien, la segunda el cb desplegará ("primera,segunda,tercera" 3 veces), la segunda 5 veces, y así sucesivamente
                //cada vez que ejecute. Mi intención era que, una vez modificado el partido, se limpiase el combobox, pero no soy capaz de lograrlo. Realmente ni siquiera sé porqué se comporta así el programa para empezar
                close();
            }
        });
    }

    private void inicializaVista() {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Alta partido");
        VBox vBox = new VBox();

        vBox.getChildren().add(new Label("Equipo local:"));
        equipoloctf = new TextField();
        TextFieldsSoloLetras(equipoloctf);
        AnchoTextfield(equipoloctf);
        vBox.getChildren().add(equipoloctf);

        vBox.getChildren().add(new Label("Equipo visitante:"));
        equipovistf = new TextField();
        TextFieldsSoloLetras(equipovistf);
        AnchoTextfield(equipovistf);
        vBox.getChildren().add(equipovistf);

        vBox.getChildren().add(new Label("Resultado:"));
        resultadotf = new TextField();
        TextFieldsSoloNumerosyGuion(resultadotf);
        resultadotf.setPromptText("Formato 'nº-nº', 5 digitos max");
        LimiteDigitos(resultadotf);
        AnchoTextfield(resultadotf);
        vBox.getChildren().add(resultadotf);

        vBox.getChildren().add(new Label("División:"));
        divisioncb = new ComboBox<String>(Logica.getINSTANCE().getComboBox());
        divisioncb.setPromptText("Elija de entre las 3 divisiones");
        divisioncb.getSelectionModel().selectFirst();
        vBox.getChildren().add(divisioncb);

        vBox.getChildren().add(new Label("Fecha:"));
        fechadp = new DatePicker();
        fechadp.setPromptText("Introduzca la fecha usando el calendario");
        AnchoDatePicker(fechadp);
        vBox.getChildren().add(fechadp);

        botonAceptar = new Button("Aceptar");
        vBox.getChildren().add(botonAceptar);

        Scene scene = new Scene(vBox, 300, 300);
        setScene(scene);
    }

    private void addPartido() {
        Partido partido = new Partido(equipoloctf.getText(), equipovistf.getText(), resultadotf.getText(), divisioncb.getSelectionModel().getSelectedItem().toString(), fechadp.getValue());
        if (partido.getEquipovis().isEmpty() || partido.getEquipoloc().isEmpty() || Logica.getINSTANCE().getComboBox().isEmpty() || partido.getFecha() == null || partido.getResultado().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ventana de error");
            alert.setHeaderText("Error al añadir el partido");
            alert.setContentText("Ha dejado algún campo en blanco o no ha introducido la fecha usando el calendario. El partido no será añadido");
            alert.show();
        } else {
            Logica.getINSTANCE().add(partido);
        }
        close();
    }

    private void TextFieldsSoloNumerosyGuion(TextField t) throws IllegalArgumentException { //para que solo me permita introducir numeros y el guión (lo uso en el resultado)
        t.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*-")) {
                    t.setText(newValue.replaceAll("[^\\d-]", ""));
                }
            }
        });
    }

    private void TextFieldsSoloLetras(TextField t) {  //para que solo me permita introducir letras y espacios (lo uso en equipo local y visitante)
        t.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("[A-Za-z ]")) {
                    t.setText(newValue.replaceAll("[^A-Za-z ]", ""));
                }
            }
        });
    }

    private void LimiteDigitos(TextField tf) { //contemplando que lo mayor que puede ser un resultado es, por ejemplo 10-11 (y ya sería bastante raro, quizá debería dejarlo en 3 dígitos en vez de 5)
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, String oldValue, String newValue) {
                if (tf.getText().length() > MAXLENGTH) {
                    String s = tf.getText().substring(0, MAXLENGTH);
                    tf.setText(s);
                }
            }
        });

    }

    private void AnchoTextfield(TextField tf) { //ancho de los TextFields
        tf.setPrefWidth(180);
        tf.setMaxWidth(180);
    }

    private void AnchoDatePicker(DatePicker dp) { //ancho del DatePicker
        dp.setPrefWidth(350);
        dp.setMaxWidth(350);
    }


}
