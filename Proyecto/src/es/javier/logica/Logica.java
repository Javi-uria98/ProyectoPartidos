package es.javier.logica;

import es.javier.models.Partido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Logica {

    private static Logica INSTANCE = null;
    private ObservableList<Partido> listaPartidos;
    private ObservableList<String> listaComboBox;

    private Logica() {
        listaPartidos = FXCollections.observableArrayList(); //IMPORTANTE!!!!! ESTE ES EL METODO QUE IMPORTO, Y NO EL NEW OBSERVABLELIST<PARTIDOS>();
        listaComboBox = FXCollections.observableArrayList();
    }

    public static Logica getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Logica();
        }

        return INSTANCE;
    }

    public void add(Partido partido) {
        listaPartidos.add(partido);
    }

    public ObservableList<Partido> getListaPartidos() {
        return listaPartidos;
    }

    public ObservableList<String> getComboBox() {
        listaComboBox.addAll("Primera", "Segunda", "Tercera");
        return listaComboBox;
    }

    public void borrarPartido(int indexborrar) {
        listaPartidos.remove(indexborrar);
    }

    public void modificarPartido(Partido partido, int posicion) {
        listaPartidos.set(posicion, partido);
    }
}

