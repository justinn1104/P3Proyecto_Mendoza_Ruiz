package Controller;

import Model.ModelGestionEstudiante;
import Model.ModelLogin;
import Model.mongoDB;
import View.GestionEstudiante;
import View.Login;
import com.mongodb.client.MongoCollection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;

public class ControllerGestionEstudiante implements ActionListener {

  ModelGestionEstudiante model;
  GestionEstudiante view;
  private String cedula;
  private final mongoDB mongo;
  private MongoCollection<Document> collection;
  
  public ControllerGestionEstudiante(ModelGestionEstudiante model, GestionEstudiante view, String cedula) {
    this.model = model;
    this.view = view;
    this.cedula = cedula;
    this.view.btnSalir.addActionListener(this);
    this.view.btnRefrescar.addActionListener(this);
    this.view.btnFiltro.addActionListener(this);
    this.view.btnSubirActividad.addActionListener(this);
    this.view.btnLimpiarActividad.addActionListener(this);
    this.view.toggBtnCalculoVectorial.addActionListener(this);
    this.view.toggBtnCalculoVectorial1.addActionListener(this);
    this.view.toggBtnFisica.addActionListener(this);
    this.view.toggBtnFisica1.addActionListener(this);
    this.view.toggBtnProgramacion.addActionListener(this);
    this.view.toggBtnProgramacion1.addActionListener(this);
    this.view.toggBtnQuimica.addActionListener(this);
    this.view.toggBtnQuimica1.addActionListener(this);
    this.mongo = mongoDB.getInstance();
    this.cargarDatosEstudiante();
    this.cargarDatosTablasPorMateria();
    this.limpiarValidadores();
    this.view.txtTitulo.setEnabled(false);
    this.view.txtAreaDetalle.setEnabled(false);
    
  }
  
  public void cargarDatosTablasPorMateria(){
    boolean band = materiaSeleccionada();
    if(band){
        cargarDatosTablas(model.getMateria());
        cargarDatosTablasCalificaiones(getCedula(), model.getUnidad(), model.getActividad());
    }else{
        System.out.println("ERROR: No se ha seleccionado la materia o no se ha encontrado.");
    }
  }

  private String getCellValue(javax.swing.JTable table, int row, int column) {
    Object value = table.getValueAt(row, column);
    return value != null ? value.toString() : ""; // Devuelve la cadena vacía si el valor es null
  }
  
  public String getCedula() {
    return cedula;
  }

  public void setCedula(String cedula) {
    this.cedula = cedula;
  }

  public void iniciarView() {
    view.setVisible(true);
  }

  public void btnSalir() {
    Login view1 = new Login();
    ModelLogin model1 = new ModelLogin("", "");
    ControllerLogin controller = new ControllerLogin(model1, view1);
    controller.viewIniciar();
    view.setVisible(false);
  }
  
  public boolean materiaSeleccionada(){
      boolean band = true;
      if(view.toggBtnProgramacion1.isSelected()){
          model.setMateria("Programacion");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnCalculoVectorial1.isSelected()){
          model.setMateria("Calculo Vectorial");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnFisica1.isSelected()){
          model.setMateria("Fisica");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnQuimica1.isSelected()){
          model.setMateria("Quimica");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnProgramacion.isSelected()){
          model.setMateria("Programacion");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnCalculoVectorial.isSelected()){
          model.setMateria("Calculo Vectorial");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnFisica.isSelected()){
          model.setMateria("Fisica");
          view.errMateria.setVisible(false);
      }else if(view.toggBtnQuimica.isSelected()){
          model.setMateria("Quimica");
          view.errMateria.setVisible(false);
      }else{
          view.errMateria.setText("Seleccionar 1: Asignatura.");
          view.errMateria.setVisible(true);
          band=false;
      }
      return band;
  }
  
  //Limpia todos los datos de las  Tablas en la vista de Gestion Estudiante
  private void LimpiarTablas() {
    DefaultTableModel modelo1 = (DefaultTableModel) view.tbUnidad1.getModel();
    modelo1.setRowCount(0);
    DefaultTableModel modelo2 = (DefaultTableModel) view.tbUnidad2.getModel();
    modelo2.setRowCount(0);
    DefaultTableModel modelo3 = (DefaultTableModel) view.tbUnidad3.getModel();
    modelo3.setRowCount(0);
    DefaultTableModel modelo4 = (DefaultTableModel) view.tbCalificaiones.getModel();
    modelo4.setRowCount(0);
  }
  
  // Metodo para cargar los datos de MongoDB en las Tablas
  public void cargarDatosTablas(String materia) {
    DefaultTableModel tdmUnidad1 = mongo.cargarDataTableCalificacioUnidades("Actividades","Unidad 1",getCedula(), materia);
    view.tbUnidad1.setModel(tdmUnidad1);
    DefaultTableModel tdmUnidad2 = mongo.cargarDataTableCalificacioUnidades("Actividades","Unidad 2",getCedula(), materia);
    view.tbUnidad2.setModel(tdmUnidad2);
    DefaultTableModel tdmUnidad3 = mongo.cargarDataTableCalificacioUnidades("Actividades","Unidad 3",getCedula(), materia);
    view.tbUnidad3.setModel(tdmUnidad3);
  }
  
  //Cargar datos del docente
  public void cargarDatosEstudiante() {
    mongo.setCollName("Estudiantes");
    collection = mongo.getCollection();
    Document filtro = new Document("Cedula", cedula);
    System.out.println("Filtro utilizado: " + filtro.toJson());
    // Verificar si el documento fue encontrado
    Document docenteDoc = collection.find(filtro).first();
    if (docenteDoc != null) {
      // Ahora puedes obtener los valores del documento
      model.setCedula(docenteDoc.getString("Cedula"));
      model.setName(docenteDoc.getString("Nombre"));
      model.setLastname(docenteDoc.getString("Apellido"));
      model.setEmail(docenteDoc.getString("E-Mail"));
      model.setMaterias(docenteDoc.getString("Materia"));
      view.txtEmail.setText(model.getEmail());
      view.txtNameLastname.setText(model.getName()+ " " + model.getLastname());
    } else {
      System.out.println("ERROR: Al bucar los datos del estudiantes.");
    }
  }
  
  public void btnSubirActividadClicked() {
    // Validar que se haya seleccionado al menos un toggle button
    if (!view.toggBtnCalculoVectorial1.isSelected() && !view.toggBtnFisica1.isSelected() && !view.toggBtnProgramacion1.isSelected() && !view.toggBtnQuimica1.isSelected()) {
        // Mostrar un mensaje en el JLabel de error
        view.errMateria.setText("*Campo Obligatorio: Asignatura para los datos de las calificaiones.");
        view.errMateria.setVisible(true);
        return;
    }
    if(view.CheckDone.isSelected()){
        model.setEstado("Si");
    }else{
        model.setEstado("No");
    }
    model.setComentario(view.txtComentario.getText());
    boolean band = model.validarCampos(model.getComentario(), view);
    if(band){
        boolean band1 = materiaSeleccionada();
        if(band1){
            cargarDatosTablas(model.getMateria());
            mongo.setCollName("Actividades");
            collection = mongo.getCollection();
            Document filtroActividad = new Document("Cedula", model.getCedula())
                .append("Unidad", model.getUnidadTabla())
                .append("Materia", model.getMateria())
                .append("Actividad", model.getActividadTabla())
                .append("Titulo", model.getTituloTabla())
                .append("Detalle", model.getDetalleTabla());
            System.out.println("Filtro utilizado: " + filtroActividad.toJson());
            // Verificar si el documento fue encontrado
            Document docenteDoc = collection.find(filtroActividad).first();
            if (docenteDoc != null) {
                Document doc = new Document("Materia",docenteDoc.getString("Materia"))
                    .append("Actividad", docenteDoc.getString("Actividad"))
                    .append("Titulo", docenteDoc.getString("Titulo"))
                    .append("Unidad", docenteDoc.getString("Unidad"))
                    .append("Detalle", docenteDoc.getString("Detalle"))
                    .append("Ponderacion", docenteDoc.getString("Ponderacion"))
                    .append("Nombre-Apellido", docenteDoc.getString("Nombre-Apellido"))
                    .append("Cedula", getCedula())
                    .append("Calificacion", docenteDoc.getString("Calificacion"))
                    .append("Estado", model.getEstado())
                    .append("Comentario", model.getComentario())
                    .append("Obsevacion",docenteDoc.getString("Obsevacion"));
                mongo.setCollName("Actividades");
                mongo.updateDocument(filtroActividad, doc);
            }else{
                System.out.println("ERROR: No se ha podido subir la actividad.");
            }            
        }else{
            System.out.println("ERROR: No se ha seleccionado la materia o no se ha encontrado.");
            LimpiarTablas();
        }
    }
  }
  
  public void limpiarValidadores(){
    view.errMateria.setVisible(false);
    view.errComentario1.setVisible(false);
    view.errActividad.setVisible(false);
    view.errAsignatura.setVisible(false);
    view.errUnidad.setVisible(false);
  }
  
  public void limpiarCampos(){
    view.toggBtnFisica1.setSelected(false);
    view.toggBtnProgramacion1.setSelected(false);
    view.toggBtnQuimica1.setSelected(false);
    view.toggBtnCalculoVectorial1.setSelected(false);
    view.toggBtnFisica.setSelected(false);
    view.toggBtnProgramacion.setSelected(false);
    view.toggBtnQuimica.setSelected(false);
    view.toggBtnCalculoVectorial.setSelected(false);
    view.CheckDone.setSelected(false);
    view.txtAreaDetalle.setText("");
    view.boxActividades.setSelectedIndex(0);
    view.boxUnidad.setSelectedIndex(0);
  }

  public void btnLimpiarActividadClicked() {
    limpiarCampos();
    limpiarValidadores();
    LimpiarTablas();
  }
  
  //Accion refrfescar view
  public void btnRefrescar() {
    cargarDatosTablasPorMateria();
    limpiarCampos();
    limpiarValidadores();
  }
  
  public void btnFiltro(){
    // Validar que se haya seleccionado al menos un toggle button
    if (!view.toggBtnCalculoVectorial.isSelected() && !view.toggBtnFisica.isSelected() && !view.toggBtnProgramacion.isSelected() && !view.toggBtnQuimica.isSelected()) {
        // Mostrar un mensaje en el JLabel de error
        view.errAsignatura.setText("*Campo Obligatorio: Asignatura para los datos de las calificaiones.");
        view.errAsignatura.setVisible(true);
        return;
    }
    // Si se seleccionó uno de los toggles, obtener el valor ingresado en el campo
    model.setUnidad(view.boxUnidad.getSelectedItem().toString());
    model.setActividad(view.boxActividades.getSelectedItem().toString());
    boolean band = model.validarCampos(model.getUnidad(), model.getActividad(), view);
    if(band){
        view.txtUnidadDato.setText(model.getUnidad());
        view.txtActividadDato.setText(model.getActividad());
        boolean band1 = materiaSeleccionada();
        if(band1){
            cargarDatosTablasCalificaiones(model.getMateria(), model.getUnidad(),model.getActividad());
        }else{
            System.out.println("ERROR: No se ha seleccionado la materia o no se ha encontrado.");
            LimpiarTablas();
        }
    }
  }
  
  public void cargarDatosTablasCalificaiones(String materia, String unidad, String actividad){
      DefaultTableModel tdm = mongo.cargarDataTableCalificaciones("Actividades", unidad, getCedula(), materia, actividad);
      view.tbCalificaiones.setModel(tdm);
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == view.btnSalir) {
        btnSalir();
    }else if(e.getSource() == view.btnRefrescar){
        btnRefrescar();
    }else if(e.getSource() == view.btnFiltro){
        btnFiltro();
    }else if (e.getSource() == view.btnSubirActividad) {
        btnSubirActividadClicked();
    }else if (e.getSource() == view.btnLimpiarActividad) {
        btnLimpiarActividadClicked();
    }else if(e.getSource()==view.toggBtnCalculoVectorial){
        
    }else if(e.getSource()==view.toggBtnCalculoVectorial1){
        btnLimpiarActividadClicked();
        cargarDatosTablasPorMateria();
    }else if(e.getSource()==view.toggBtnFisica){
        
    }else if(e.getSource()==view.toggBtnFisica1){
        btnLimpiarActividadClicked();
        cargarDatosTablasPorMateria();
    }else if(e.getSource()==view.toggBtnProgramacion){
        
    }else if(e.getSource()==view.toggBtnProgramacion1){
        btnLimpiarActividadClicked();
        cargarDatosTablasPorMateria();
    }else if(e.getSource()==view.toggBtnQuimica){
        
    }else if(e.getSource()==view.toggBtnQuimica1){
        btnLimpiarActividadClicked();
        cargarDatosTablasPorMateria();
    }
  }

}
