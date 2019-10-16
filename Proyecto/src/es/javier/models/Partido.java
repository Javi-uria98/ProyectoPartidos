package es.javier.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;
    private String equipoloc;
    private String equipovis;
    private String resultado;
    private String division;
    private LocalDate fecha;

    public Partido(String equipoloc, String equipovis, String resultado, String division, LocalDate fecha) {
        setEquipoloc(equipoloc);
        setEquipovis(equipovis);
        setResultado(resultado);
        setDivision(division);
        setFecha(fecha);
    }

    public String getEquipoloc() {

        return equipoloc;
    }

    public void setEquipoloc(String equipoloc) {

        this.equipoloc = equipoloc;
    }

    public String getEquipovis() {

        return equipovis;
    }

    public void setEquipovis(String equipovis) {

        this.equipovis = equipovis;
    }

    public String getResultado() {

        return resultado;
    }

    public void setResultado(String resultado) {

        this.resultado = resultado;
    }

    public String getDivision() {

        return division;
    }

    public void setDivision(String division) {

        this.division = division;
    }


    public LocalDate getFecha() {

        return fecha;
    }

    public void setFecha(LocalDate fecha) {

        this.fecha = fecha;
    }

}

