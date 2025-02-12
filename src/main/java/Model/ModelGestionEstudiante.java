package Model;

import View.GestionEstudiante;

public class ModelGestionEstudiante {
    private String estado, comentario, caliU1, caliU2, caliU3, promedio;
    private String email, name, lastname, materias, cedula, materia, unidad, actividad;
    private String actividadTabla, tituloTabla, unidadTabla, detalleTabla, nomApeTabla, calificacionTabla, observacionTabla, ponderacoinTabla; 

    public ModelGestionEstudiante(String estado, String comentario, String caliU1, String caliU2, String caliU3, String promedio) {
        this.estado = estado;
        this.comentario = comentario;
        this.caliU1 = caliU1;
        this.caliU2 = caliU2;
        this.caliU3 = caliU3;
        this.promedio = promedio;
    }
    
    public boolean validarCampos(String comentario, GestionEstudiante view){
        boolean band = true;
        if(comentario.isEmpty()){
            view.errComentario1.setText("*Campo Obligatorio.");
            view.errComentario1.setVisible(true);
            band = false;
        }else{
            view.errComentario1.setVisible(false);
        }
        return band;
    }
    
    public boolean validarCampos(String unidad, String actividad, GestionEstudiante view){
        boolean band = true;
        if (unidad.equalsIgnoreCase("Seleccionar")) {
            view.errUnidad.setText("*Campo: Invalido");
            view.errUnidad.setVisible(true);
            band = false;
        } else {
          view.errUnidad.setVisible(false);
        }
        if (actividad.equalsIgnoreCase("Seleccionar")) {
          view.errActividad.setText("*Campo Invalido");
          view.errActividad.setVisible(true);
          band = false;
        } else {
          view.errActividad.setVisible(false);
        }
        return band;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCaliU1() {
        return caliU1;
    }

    public void setCaliU1(String caliU1) {
        this.caliU1 = caliU1;
    }

    public String getCaliU2() {
        return caliU2;
    }

    public void setCaliU2(String caliU2) {
        this.caliU2 = caliU2;
    }

    public String getCaliU3() {
        return caliU3;
    }

    public void setCaliU3(String caliU3) {
        this.caliU3 = caliU3;
    }

    public String getPromedio() {
        return promedio;
    }

    public void setPromedio(String promedio) {
        this.promedio = promedio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMaterias() {
        return materias;
    }

    public void setMaterias(String materias) {
        this.materias = materias;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getActividadTabla() {
        return actividadTabla;
    }

    public void setActividadTabla(String actividadTabla) {
        this.actividadTabla = actividadTabla;
    }

    public String getTituloTabla() {
        return tituloTabla;
    }

    public void setTituloTabla(String tituloTabla) {
        this.tituloTabla = tituloTabla;
    }

    public String getUnidadTabla() {
        return unidadTabla;
    }

    public void setUnidadTabla(String unidadTabla) {
        this.unidadTabla = unidadTabla;
    }

    public String getDetalleTabla() {
        return detalleTabla;
    }

    public void setDetalleTabla(String detalleTabla) {
        this.detalleTabla = detalleTabla;
    }

    public String getNomApeTabla() {
        return nomApeTabla;
    }

    public void setNomApeTabla(String nomApeTabla) {
        this.nomApeTabla = nomApeTabla;
    }

    public String getCalificacionTabla() {
        return calificacionTabla;
    }

    public void setCalificacionTabla(String calificacionTabla) {
        this.calificacionTabla = calificacionTabla;
    }

    public String getObservacionTabla() {
        return observacionTabla;
    }

    public void setObservacionTabla(String observacionTabla) {
        this.observacionTabla = observacionTabla;
    }

    public String getPonderacoinTabla() {
        return ponderacoinTabla;
    }

    public void setPonderacoinTabla(String ponderacoinTabla) {
        this.ponderacoinTabla = ponderacoinTabla;
    }
    
    
    
    
}
