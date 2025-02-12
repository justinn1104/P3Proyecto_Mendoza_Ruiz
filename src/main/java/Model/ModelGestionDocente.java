package Model;

import View.GestionDocente;

public class ModelGestionDocente {

  private String nameStudent, lastNameStudent, emailStudent, materiaTeaching, emailTeanching, nameTeaching, lastNameTeaching;
  private String estadoStudent, cedulaDocen, promedio, cedulaStudent, materiaAtudent;
  private String unidad, actividad, nombreActividad, detalle, observacion;
  private int ponderacion;
  private float calificacion;

  public ModelGestionDocente(String nameStudent, String lastNameStudent, String emailStudent, String emailTeanching, String nameTeaching, String lastNameTeaching) {
    this.nameStudent = nameStudent;
    this.lastNameStudent = lastNameStudent;
    this.emailStudent = emailStudent;
    this.emailTeanching = emailTeanching;
    this.nameTeaching = nameTeaching;
    this.lastNameTeaching = lastNameTeaching;
  }

  public boolean validarActividad(String unidad, String actividad, String nombre, String detalle, int ponderacion, GestionDocente view) {
    boolean band = true;
    if (unidad.equalsIgnoreCase("Seleccionar")) {
      view.errUnidadActividad.setText("*Campo: Invalido");
      view.errUnidadActividad.setVisible(true);
      band = false;
    } else {
      view.errUnidadActividad.setVisible(false);
    }
    if (actividad.equalsIgnoreCase("Seleccionar")) {
      view.errActividad.setText("*Campo Invalido");
      view.errActividad.setVisible(true);
      band = false;
    } else {
      view.errActividad.setVisible(false);
    }
    if (nombre.isEmpty()) {
      view.errNombreActividad.setText("*Campo: Invalido");
      view.errNombreActividad.setVisible(true);
      band = false;
    } else if (nombre.length() < 5 || nombre.length() > 20) {
      view.errNombreActividad.setText("*Campo Invalido: [10 a 20] Caracteres");
      view.errNombreActividad.setVisible(true);
      band = false;
    } else {
      view.errNombreActividad.setVisible(false);
    }
    if (detalle.isEmpty()) {
      view.errDetalleActividad.setText("*Campo: Invalido");
      view.errDetalleActividad.setVisible(true);
      band = false;
    } else if (detalle.length() < 35 || detalle.length() > 150) {
      view.errDetalleActividad.setText("*Campo Invalido: [35 a 150] Caracteres");
      view.errDetalleActividad.setVisible(true);
      band = false;
    } else {
      view.errDetalleActividad.setVisible(false);
    }

    if (ponderacion <= 0) {
      view.errPonderacionActividad.setText("*Campo Invalido: [MAYOR a 0]");
      view.errPonderacionActividad.setVisible(true);
      band = false;
    } else {
      view.errPonderacionActividad.setVisible(false);
      if (actividad.equalsIgnoreCase("Tarea")) {
        if (ponderacion > 2) {
          view.errPonderacionActividad.setText("*Campo Invalido: [0 a 2]");
          view.errPonderacionActividad.setVisible(true);
          band = false;
        } else {
          view.errPonderacionActividad.setVisible(false);
        }
      } else if (actividad.equalsIgnoreCase("Taller")) {
        if (ponderacion > 2) {
          view.errPonderacionActividad.setText("*Campo Invalido: [0 a 2]");
          view.errPonderacionActividad.setVisible(true);
          band = false;
        } else {
          view.errPonderacionActividad.setVisible(false);
        }
      } else if (actividad.equalsIgnoreCase("Laboratorio")) {
        if (ponderacion > 4) {
          view.errPonderacionActividad.setText("*Campo Invalido: [0 a 4]");
          view.errPonderacionActividad.setVisible(true);
          band = false;
        } else {
          view.errPonderacionActividad.setVisible(false);
        }
      } else if (actividad.equalsIgnoreCase("Prueba")) {
        if (ponderacion > 5) {
          view.errPonderacionActividad.setText("*Campo Invalido: [0 a 5]");
          view.errPonderacionActividad.setVisible(true);
          band = false;
        } else {
          view.errPonderacionActividad.setVisible(false);
        }
      } else if (actividad.equalsIgnoreCase("Examen")) {
        if (ponderacion > 7) {
          view.errPonderacionActividad.setText("*Campo Invalido: [0 a 7]");
          view.errPonderacionActividad.setVisible(true);
          band = false;
        } else {
          view.errPonderacionActividad.setVisible(false);
        }
      } else {
        view.errPonderacionActividad.setVisible(false);
      }
    }
    return band;
  }

  public boolean validarCalificaciones(String observacion, int calificacion, GestionDocente view) {
    boolean band = true;
    if (observacion.isEmpty()) {
      view.errObservacion.setText("*Campo: Invalido");
      view.errObservacion.setVisible(true);
      band = false;
    } else if (observacion.length() < 5 || observacion.length() > 20) {
      view.errObservacion.setText("*Campo Invalido: [5 a 20] Caracteres");
      view.errObservacion.setVisible(true);
      band = false;
    } else {
      view.errObservacion.setVisible(false);
    }
    if (calificacion < 0 || calificacion > 20) {
      view.errEstado.setText("*Campo Invalido: [10 a 20]");
      view.errEstado.setVisible(true);
      band = false;
    } else {
      view.errEstado.setVisible(false);
    }
    return band;
  }

  public String getNameStudent() {
    return nameStudent;
  }

  public void setNameStudent(String nameStudent) {
    this.nameStudent = nameStudent;
  }

  public String getLastNameStudent() {
    return lastNameStudent;
  }

  public void setLastNameStudent(String lastNameStudent) {
    this.lastNameStudent = lastNameStudent;
  }

  public String getEmailStudent() {
    return emailStudent;
  }

  public void setEmailStudent(String emailStudent) {
    this.emailStudent = emailStudent;
  }

  public String getMateriaTeaching() {
    return materiaTeaching;
  }

  public void setMateriaTeaching(String materiaTeaching) {
    this.materiaTeaching = materiaTeaching;
  }

  public String getEmailTeanching() {
    return emailTeanching;
  }

  public void setEmailTeanching(String emailTeanching) {
    this.emailTeanching = emailTeanching;
  }

  public String getNameTeaching() {
    return nameTeaching;
  }

  public void setNameTeaching(String nameTeaching) {
    this.nameTeaching = nameTeaching;
  }

  public String getLastNameTeaching() {
    return lastNameTeaching;
  }

  public void setLastNameTeaching(String lastNameTeaching) {
    this.lastNameTeaching = lastNameTeaching;
  }

  public String getEstadoStudent() {
    return estadoStudent;
  }

  public void setEstadoStudent(String estadoStudent) {
    this.estadoStudent = estadoStudent;
  }

  public String getPromedio() {
    return promedio;
  }

  public void setPromedio(String promedio) {
    this.promedio = promedio;
  }

  public String getCedulaDocen() {
    return cedulaDocen;
  }

  public void setCedulaDocen(String cedulaDocen) {
    this.cedulaDocen = cedulaDocen;
  }

  public String getCedulaStudent() {
    return cedulaStudent;
  }

  public void setCedulaStudent(String cedulaStudent) {
    this.cedulaStudent = cedulaStudent;
  }

  public String getMateriaAtudent() {
    return materiaAtudent;
  }

  public void setMateriaAtudent(String materiaAtudent) {
    this.materiaAtudent = materiaAtudent;
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

  public String getNombreActividad() {
    return nombreActividad;
  }

  public void setNombreActividad(String nombreActividad) {
    this.nombreActividad = nombreActividad;
  }

  public String getDetalle() {
    return detalle;
  }

  public void setDetalle(String detalle) {
    this.detalle = detalle;
  }

  public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public int getPonderacion() {
    return ponderacion;
  }

  public void setPonderacion(int ponderacion) {
    this.ponderacion = ponderacion;
  }

  public float getCalificacion() {
    return calificacion;
  }

  public void setCalificacion(float calificacion) {
    this.calificacion = calificacion;
  }

}
