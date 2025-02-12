package Controller;

import Model.ModelGestionDocente;
import Model.ModelLogin;
import Model.mongoDB;
import View.GestionDocente;
import View.Login;
import com.mongodb.client.MongoCollection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;

import java.io.FileReader;
import java.io.IOException;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControllerGestionDocente implements ActionListener {

  ModelGestionDocente model;
  GestionDocente view;
  private String cedula;
  private final mongoDB mongo;
  private MongoCollection<Document> collection;
  private String jsonNameFile = "estudiantes.json";

  public ControllerGestionDocente(ModelGestionDocente model, GestionDocente view, String cedula) {
    this.model = model;
    this.view = view;
    this.cedula = cedula;
    this.view.btnSalir.addActionListener(this);
    this.view.btnRefrescar.addActionListener(this);
    this.view.btnCargarJson.addActionListener(this);
    this.view.btnSubirActividad.addActionListener(this);
    this.view.btnEliminarActividad.addActionListener(this);
    this.view.btnBuscarActividad.addActionListener(this);
    this.view.btnModificarActividad.addActionListener(this);
    this.view.btnLimpiarActividad.addActionListener(this);
    this.view.btnEliminarCalificaciones.addActionListener(this);
    this.view.btnBuscarCalificaciones.addActionListener(this);
    this.view.btnModificarCalificaciones.addActionListener(this);
    this.view.btnLimpiarCalificaciones.addActionListener(this);
    this.view.btnFiltroCalificacion.addActionListener(this);
    this.view.checkEstadoNo.setEnabled(false);
    this.view.checkEstadoSi.setEnabled(false);
    this.view.spnCalificacion.setModel(new SpinnerNumberModel(0.0, 0.0, 20.0, 0.1)); // Valor inicial, mínimo, máximo, incremento
    this.mongo = mongoDB.getInstance();
    this.cargarDatosDocente();
    this.cargarDatosTablas();
    this.limpiarValidadoresActividad();
    this.limpiarValidadoresCalificar();
    // Agregar un ListSelectionListener a la tabla
    view.tbActiviades.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) { // Comprobar que la selección no está ajustándose
                int rowTbActividades = view.tbActiviades.getSelectedRow(); // Obtener la fila seleccionada
                if (rowTbActividades != -1) { // Verificar que se ha seleccionado una fila
                    // Validar y obtener valores de las celdas seleccionadas
                    String unidad = getCellValue(view.tbActiviades, rowTbActividades, 0);
                    String actividad = getCellValue(view.tbActiviades, rowTbActividades, 1);
                    String titulo = getCellValue(view.tbActiviades, rowTbActividades, 2);
                    String detalle = getCellValue(view.tbActiviades, rowTbActividades, 3);
                    String ponderacion = getCellValue(view.tbActiviades, rowTbActividades, 4);
                    int ponde = ponderacion.isEmpty() ? 0 : Integer.parseInt(ponderacion);

                    // Colocar los valores en los campos de texto
                    model.setUnidad(unidad);
                    model.setActividad(actividad);
                    model.setNombreActividad(titulo);
                    model.setDetalle(detalle);
                    model.setCedulaStudent(cedula);
                    model.setPonderacion(ponde);

                    view.boxActividades.setSelectedItem(model.getActividad());
                    view.boxUnidadActividad.setSelectedItem(model.getUnidad());
                    view.txtNombreActividad.setText(model.getNombreActividad());
                    view.txtDetalleActividad.setText(model.getDetalle());
                    view.spnPonderacionActividad.setValue(model.getPonderacion());
                }
            }
        }   
    });
    view.tbStudent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) { // Comprobar que la selección no está ajustándose
                int rowTbCalificar = view.tbStudent.getSelectedRow(); // Obtener la fila seleccionada
                if (rowTbCalificar != -1) { // Verificar que se ha seleccionado una fila
                    // Validar y obtener valores de las celdas seleccionadas
                    String unidad = getCellValue(view.tbStudent, rowTbCalificar, 2);
                    String estado = getCellValue(view.tbStudent, rowTbCalificar, 6);
                    String calificacion = getCellValue(view.tbStudent, rowTbCalificar, 5);
                    String observacion = getCellValue(view.tbStudent, rowTbCalificar, 8);
                    float califi = calificacion.isEmpty() ? 0.0f : Float.parseFloat(calificacion);

                    // Colocar los valores en los campos de texto
                    model.setUnidad(unidad);
                    model.setEstadoStudent(estado);
                    model.setCalificacion(califi);
                    model.setObservacion(observacion);

                    view.boxUnidadCalificacion.setSelectedItem(model.getUnidad());
                    if (model.getEstadoStudent().equalsIgnoreCase("Si")) {
                        view.checkEstadoSi.setSelected(true);
                        view.checkEstadoNo.setSelected(false);
                    } else if (model.getEstadoStudent().equalsIgnoreCase("No")) {
                        view.checkEstadoSi.setSelected(false);
                        view.checkEstadoNo.setSelected(true);
                    } else {
                        view.checkEstadoSi.setSelected(false);
                        view.checkEstadoNo.setSelected(false);
                    }

                    view.spnPonderacionActividad.setValue(model.getCalificacion());
                    view.txtObservacionCalificacion.setText(model.getObservacion());
                }
            }
        }
    });
  }
  
  private String getCellValue(javax.swing.JTable table, int row, int column) {
        Object value = table.getValueAt(row, column);
        return value != null ? value.toString() : ""; // Devuelve la cadena vacía si el valor es null
  }
  
  public ModelGestionDocente getModel() {
        return model;
  }

  public void setModel(ModelGestionDocente model) {
        this.model = model;
  }

  public GestionDocente getView() {
        return view;
  }

  public void setView(GestionDocente view) {
        this.view = view;
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

  //Limpia todos los datos de mi Tbla
  private void LimpiarTablas() {
    DefaultTableModel modelo1 = (DefaultTableModel) view.tbStudent.getModel();
    modelo1.setRowCount(0);
    DefaultTableModel modelo2 = (DefaultTableModel) view.tbActiviades.getModel();
    modelo2.setRowCount(0);
  }

  // Metodo para cargar los datos de MongoDB en la Tabla
  public void cargarDatosTablas() {
    DefaultTableModel tdmActividades = mongo.cargarDataTableActividades(model.getMateriaTeaching());
    view.tbActiviades.setModel(tdmActividades);
    DefaultTableModel tdmCalificacion = mongo.cargarDataTableCalificar("Actividades", model.getMateriaTeaching());
    view.tbStudent.setModel(tdmCalificacion);
  }

  //VERIFICA Q NO ESTÉ VACÍA LA DB
  public boolean docentesVacia() {
    mongo.setCollName("Docentes");
    ArrayList<Document> datos = mongo.readDocument();
    return !datos.isEmpty();
  }

  //AQUI EL METODO CARGAR EL .JSON
  public void cargarJSON() {
    jsonNameFile = "";
    JTextField fileNameField = new JTextField();
    Object[] message = {
      "Ingrese el nombre del archivo JSON (sin extensión):", fileNameField
    };
    int option = JOptionPane.showConfirmDialog(null, message, "Cargar JSON", JOptionPane.OK_CANCEL_OPTION);
    //USUARIO SELECCIONA OK
    if (option == JOptionPane.OK_OPTION) {
      jsonNameFile = fileNameField.getText().trim();
      //VALIDA NOMBRE VACIO
      if (jsonNameFile.isEmpty()) {
        JOptionPane.showMessageDialog(null, "El nombre del archivo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
      }
      //USUARIO CANCELA CUADRO DE DIALOGO
    } else {
      JOptionPane.showMessageDialog(null, "Operación cancelada por el usuario.");
      System.out.println("Operación cancelada por el usuario.");
    }
    //LEE Y CARGA JSON
    if (!jsonNameFile.isEmpty()) {
        jsonNameFile += ".json";
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonNameFile)) {
            JSONArray eventsArray = (JSONArray) parser.parse(reader);
            for (Object eventObjectJSON : eventsArray) {
                JSONObject datos = (JSONObject) eventObjectJSON;
                Document documento = new Document()
                    .append("Cedula", datos.get("Cedula"))
                    .append("Nombre", datos.get("Nombre"))
                    .append("Apellido", datos.get("Apellido"))
                    .append("Materias", datos.get("Materias"))
                    .append("Tipo", datos.get("Tipo"))
                    .append("Fecha-Nacimiento", datos.get("Fecha-Nacimiento"))
                    .append("E-Mail", datos.get("E-Mail"))
                    .append("Usuario", datos.get("Usuario"))
                    .append("Password", datos.get("Password"))
                    .append("Unidad-1", datos.get("Unidad-1"))
                    .append("Unidad-2", datos.get("Unidad-2"))
                    .append("Unidad-3", datos.get("Unidad-3"))
                    .append("Promedio", datos.get("Promedio"));
                mongo.setCollName("Estudiantes");
                mongo.createDocument(documento);
                Document documento2 = new Document()
                    .append("Cedula", datos.get("Cedula"))
                    .append("Nombre-Apellido", datos.get("Nombre")+" "+datos.get("Apellido"))
                    .append("Tipo", datos.get("Tipo"))
                    .append("E-Mail", datos.get("E-Mail"))
                    .append("Usuario", datos.get("Usuario"))
                    .append("Password", datos.get("Password"));
                mongo.setCollName("Credenciales");
                mongo.createDocument(documento2);
            }
            JOptionPane.showMessageDialog(null, "Archivo " + jsonNameFile + " importado con éxito:");
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(null, "Archivo " + jsonNameFile + " no existe");
            e.printStackTrace();
        }
    }
  }

  //Cargar datos del docente
  public void cargarDatosDocente() {
    mongo.setCollName("Docentes");
    collection = mongo.getCollection();
    Document filtro = new Document("Cedula", cedula);
    System.out.println("Filtro utilizado: " + filtro.toJson());
    // Verificar si el documento fue encontrado
    Document docenteDoc = collection.find(filtro).first();
    if (docenteDoc != null) {
      // Ahora puedes obtener los valores del documento
      model.setCedulaDocen(docenteDoc.getString("Cedula"));
      model.setNameTeaching(docenteDoc.getString("Nombre"));
      model.setLastNameTeaching(docenteDoc.getString("Apellido"));
      model.setEmailTeanching(docenteDoc.getString("E-Mail"));
      model.setMateriaTeaching(docenteDoc.getString("Materia"));
      view.txtEmailDocen.setText(model.getEmailTeanching());
      view.txtNameLastnameDocen.setText(model.getLastNameTeaching() + " " + model.getNameTeaching());
      view.txtMateriaDocen.setText(model.getMateriaTeaching());
    } else {
      System.out.println("ERROR: Al bucar los datos del docente.");
    }
  }

  //Acciones de Actividad
  public void btnSubirActividad() {
    model.setUnidad((String) view.boxUnidadActividad.getSelectedItem().toString());
    model.setActividad((String) view.boxActividades.getSelectedItem().toString());
    model.setNombreActividad(view.txtNombreActividad.getText());
    model.setPonderacion(((Number) view.spnPonderacionActividad.getValue()).intValue());
    System.out.println("dato de spn: " + model.getPonderacion());
    model.setDetalle(view.txtDetalleActividad.getText());
    boolean band = model.validarActividad(model.getUnidad(), model.getActividad(), model.getNombreActividad(), model.getDetalle(), model.getPonderacion(), view);
    if (band) {
        // Obtener la colección
        mongo.setCollName("Estudiantes");
        collection = mongo.getCollection();
        // Obtener todos los documentos de la colección
        ArrayList<Document> documentos = mongoDB.returnDocuments(collection);
        // Crear documento común para la colección "Docentes"
        Document Doc_Actividad = new Document("Actividad", model.getActividad())
                .append("Titulo", model.getNombreActividad())
                .append("Unidad", model.getUnidad())
                .append("Detalle", model.getDetalle())
                .append("Ponderacion", model.getPonderacion());
        if (model.getMateriaTeaching().equalsIgnoreCase("Programacion")) {
            mongo.setCollName("Actividades_Programacion");
            mongo.createDocument(Doc_Actividad);
        } else if (model.getMateriaTeaching().equalsIgnoreCase("Quimica")) {
            mongo.setCollName("Actividades_Quimica");
            mongo.createDocument(Doc_Actividad);
        } else if (model.getMateriaTeaching().equalsIgnoreCase("Calculo Vectorial")) {
            mongo.setCollName("Actividades_CalculoVectorial");
            mongo.createDocument(Doc_Actividad);
        } else if (model.getMateriaTeaching().equalsIgnoreCase("Fisica")) {
            mongo.setCollName("Actividades_Fisica");
            mongo.createDocument(Doc_Actividad);
        }
        // Acceder a campos específicos
        for (Document doc : documentos) {
            // Buscar si el elemento está en la cadena
            if (doc.getString("Materias").contains("Programacion")&&model.getMateriaTeaching().equalsIgnoreCase("Programacion")) {
                // Crear documento común para la colección "Actividades"
                Document Doc_Actividades = new Document("Materia", model.getMateriaTeaching() )
                    .append("Actividad", model.getActividad())
                    .append("Titulo", model.getNombreActividad())
                    .append("Unidad", model.getUnidad())
                    .append("Detalle", model.getDetalle())
                    .append("Ponderacion", model.getPonderacion())
                    .append("Nombre-Apellido", doc.getString("Nombre")+" "+doc.getString("Apellido"))
                    .append("Cedula", doc.getString("Cedula"))
                    .append("Calificacion", "")
                    .append("Estado", "")
                    .append("Comentario", "")
                    .append("Obsevacion", "");
                mongo.setCollName("Actividades");
                mongo.createDocument(Doc_Actividades);
            }else if (doc.getString("Materias").contains("Quimica")&&model.getMateriaTeaching().equalsIgnoreCase("Quimica")) {
                // Crear documento común para la colección "Actividades"
                Document Doc_Actividades = new Document("Materia", model.getMateriaTeaching() )
                    .append("Actividad", model.getActividad())
                    .append("Titulo", model.getNombreActividad())
                    .append("Unidad", model.getUnidad())
                    .append("Detalle", model.getDetalle())
                    .append("Ponderacion", model.getPonderacion())
                    .append("Nombre-Apellido", doc.getString("Nombre")+" "+doc.getString("Apellido"))
                    .append("Cedula", doc.getString("Cedula"))
                    .append("Calificacion", "")
                    .append("Estado", "")
                    .append("Comentario", "")
                    .append("Obsevacion", "");
                mongo.setCollName("Actividades");
                mongo.createDocument(Doc_Actividades);
            }else if (doc.getString("Materias").contains("Calculo Vectorial")&&model.getMateriaTeaching().equalsIgnoreCase("Calculo Vectorial")) {
                // Crear documento común para la colección "Actividades"
                Document Doc_Actividades = new Document("Materia", model.getMateriaTeaching() )
                    .append("Actividad", model.getActividad())
                    .append("Titulo", model.getNombreActividad())
                    .append("Unidad", model.getUnidad())
                    .append("Detalle", model.getDetalle())
                    .append("Ponderacion", model.getPonderacion())
                    .append("Nombre-Apellido", doc.getString("Nombre")+" "+doc.getString("Apellido"))
                    .append("Cedula", doc.getString("Cedula"))
                    .append("Calificacion", "")
                    .append("Estado", "")
                    .append("Comentario", "")
                    .append("Obsevacion", "");
                mongo.setCollName("Actividades");
                mongo.createDocument(Doc_Actividades);
            }else if (doc.getString("Materias").contains("Fisica")&&model.getMateriaTeaching().equalsIgnoreCase("Fisica")) {
                // Crear documento común para la colección "Actividades"
                Document Doc_Actividades = new Document("Materia", model.getMateriaTeaching() )
                    .append("Actividad", model.getActividad())
                    .append("Titulo", model.getNombreActividad())
                    .append("Unidad", model.getUnidad())
                    .append("Detalle", model.getDetalle())
                    .append("Ponderacion", model.getPonderacion())
                    .append("Nombre-Apellido", doc.getString("Nombre")+" "+doc.getString("Apellido"))
                    .append("Cedula", doc.getString("Cedula"))
                    .append("Calificacion", "")
                    .append("Estado", "")
                    .append("Comentario", "")
                    .append("Obsevacion", "");
                mongo.setCollName("Actividades");
                mongo.createDocument(Doc_Actividades);
            }
        }
        
        System.out.println("Actividad: Subida con Exito.");
        cargarDatosTablas();
        limpiarActividad();
    } else {
      System.out.println("ERROR: Al validar los campos de panel de actividad");
    }
  }

  public void btnEditarActividad() {
    view.boxUnidadActividad.setEnabled(false);
    view.txtNombreActividad.setEnabled(false);
    model.setUnidad((String) view.boxUnidadActividad.getSelectedItem().toString());
    model.setActividad((String) view.boxActividades.getSelectedItem().toString());
    model.setNombreActividad(view.txtNombreActividad.getText());
    model.setPonderacion(((Number) view.spnPonderacionActividad.getValue()).intValue());
    System.out.println("dato de spn: " + model.getPonderacion());
    model.setDetalle(view.txtDetalleActividad.getText());
    boolean band = model.validarActividad(model.getUnidad(), model.getActividad(), model.getNombreActividad(), model.getDetalle(), model.getPonderacion(), view);
    if (band) {
      Document filtroActividad = new Document("Nombre", model.getNombreActividad());
      // Crear documento común para la colección "Docentes"
      Document Doc_Actividad_New = new Document("Actividad", model.getActividad())
              .append("Titulo", model.getNombreActividad())
              .append("Unidad", model.getUnidad())
              .append("Detalle", model.getDetalle())
              .append("Ponderacion", model.getPonderacion());
      if (model.getMateriaTeaching().equalsIgnoreCase("Programacion")) {
          mongo.setCollName("Actividades_Programacion");
          mongo.updateDocument(filtroActividad, Doc_Actividad_New);
      } else if (model.getMateriaTeaching().equalsIgnoreCase("Quimica")) {
          mongo.setCollName("Actividades_Quimica");
          mongo.updateDocument(filtroActividad, Doc_Actividad_New);
      } else if (model.getMateriaTeaching().equalsIgnoreCase("Calculo Vectorial")) {
          mongo.setCollName("Actividades_CalculoVectorial");
          mongo.updateDocument(filtroActividad, Doc_Actividad_New);
      } else if (model.getMateriaTeaching().equalsIgnoreCase("Fisica")) {
          mongo.setCollName("Actividades_Fisica");
          mongo.updateDocument(filtroActividad, Doc_Actividad_New);
      }
      System.out.println("Actividad: Editada con Exito.");
    } else {
      System.out.println("ERROR: Al validar los campos de panel de actividad");
    }
  }

  public void btnEliminarActividad() {
    int filaSeleccionada = view.tbActiviades.getSelectedRow();
    DefaultTableModel dtm = (DefaultTableModel) view.tbActiviades.getModel();
    if (filaSeleccionada < 0) {
      JOptionPane.showMessageDialog(null, "Debe seleccionar una fila para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este Estudiante?", "Confirmación", JOptionPane.YES_NO_OPTION);
    if (opcion == JOptionPane.YES_OPTION) {
      String titulo = dtm.getValueAt(filaSeleccionada, 2).toString();
      dtm.removeRow(filaSeleccionada);
      // Eliminar de la base de datos
      Document filtro = new Document("Titulo", titulo);
      if (model.getMateriaTeaching().equalsIgnoreCase("Programacion")) {
          mongo.setCollName("Actividades_Programacion");
          mongo.deleteDocument(filtro);
      } else if (model.getMateriaTeaching().equalsIgnoreCase("Quimica")) {
          mongo.setCollName("Actividades_Quimica");
          mongo.deleteDocument(filtro);
      } else if (model.getMateriaTeaching().equalsIgnoreCase("Calculo Vectorial")) {
          mongo.setCollName("Actividades_CalculoVectorial");
          mongo.deleteDocument(filtro);
      } else if (model.getMateriaTeaching().equalsIgnoreCase("Fisica")) {
          mongo.setCollName("Actividades_Fisica");
          mongo.deleteDocument(filtro);
      }
      cargarDatosTablas();
      System.out.println("Actividad: Eliminada con Exito.");
    }
  }

  public void btnBuscarActividad() {
    String titulo = null;
    boolean ValidaTitulo = false;
    while (!ValidaTitulo) {
      titulo = JOptionPane.showInputDialog(view, "Ingrese la Tutulo de la actividad a buscar:");
      if (titulo == null) {
        return;
      } else if (titulo.isEmpty() || cedula.length() < 5 || cedula.length() > 20) {
        JOptionPane.showMessageDialog(view, "El titulo de la actividad debe tener de [5 a 20] dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        ValidaTitulo = true;
      }
    }
    LimpiarTablas();
    Document filtro = new Document("Titulo", titulo);
    ArrayList<Document> resultados = null;
    if (model.getMateriaTeaching().equalsIgnoreCase("Programacion")) {
        mongo.setCollName("Actividades_Programacion");
        resultados = mongo.searchDocument(filtro);
    } else if (model.getMateriaTeaching().equalsIgnoreCase("Quimica")) {
        mongo.setCollName("Actividades_Quimica");
        resultados = mongo.searchDocument(filtro);
    } else if (model.getMateriaTeaching().equalsIgnoreCase("Calculo Vectorial")) {
        mongo.setCollName("Actividades_CalculoVectorial");
        resultados = mongo.searchDocument(filtro);
    } else if (model.getMateriaTeaching().equalsIgnoreCase("Fisica")) {
        mongo.setCollName("Actividades_Fisica");
        resultados = mongo.searchDocument(filtro);
    }
    DefaultTableModel dtm = (DefaultTableModel) view.tbActiviades.getModel();
    dtm.setRowCount(0);
    if (resultados != null && !resultados.isEmpty()) {
      for (Document doc : resultados) {
        Object[] row = {
          doc.get("Unidad"),
          doc.get("Actividad"),
          doc.get("Titulo"),
          doc.get("Detalle"),
          doc.get("Ponderacion"),};
        dtm.addRow(row);
      }
      System.out.println("Actividad: Buscada con Exito.");
    } else {
      JOptionPane.showMessageDialog(null, "Actividad no existente.", "Búsqueda", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void btnLimpiarActividad() {
    limpiarActividad();
    limpiarValidadoresActividad();
    LimpiarTablas();
  }

  public void limpiarValidadoresActividad() {
    view.errDetalleActividad.setVisible(false);
    view.errNombreActividad.setVisible(false);
    view.errUnidadActividad.setVisible(false);
    view.errActividad.setVisible(false);
    view.errPonderacionActividad.setVisible(false);
  }

  public void limpiarActividad() {
    view.txtDetalleActividad.setText("");
    view.txtNombreActividad.setText("");
    view.boxUnidadActividad.setSelectedItem("Seleccionar");
    view.boxActividades.setSelectedItem("Seleccionar");
    view.spnPonderacionActividad.setValue(0);
  }

  //Acciones de calificar
  public void btnEditarCalificar() {
      
  }

  public void btnEliminarCalificar() {
      
  }

  public void btnBuscarCalificar() {
      
  }

  public void btnLimpiarCalificar() {
      limpiarCalificar();
      limpiarValidadoresCalificar();
      LimpiarTablas();
  }

  public void limpiarValidadoresCalificar() {
      view.errUnidadCalificacion.setVisible(false);
      view.errEstado.setVisible(false);
      view.errCalifiCalificacion1.setVisible(false);
      view.errObservacion.setVisible(false);
  }

  public void limpiarCalificar() {
      view.boxUnidadCalificacion.setSelectedIndex(0);
      view.checkEstadoSi.setSelected(false);
      view.checkEstadoNo.setSelected(false);
      view.spnCalificacion.setValue(0);
      view.txtObservacionCalificacion.setText("");
  }

  //Accion de filtrar actividades por unidad
  public void btnFiltroActiviadad() {
      model.setUnidad(view.boxUnidadCalificacion.getSelectedItem().toString());
      if(model.getUnidad().equalsIgnoreCase("Seleccionar")){
          view.errUnidadCalificacion.setText("Campo Obligatorio");
          view.errUnidadCalificacion.setVisible(true);
          return;
      }else {
          view.errUnidadCalificacion.setVisible(false);
          DefaultTableModel tdmActividades = mongo.cargarDataTableCalificar("Actividades",model.getMateriaTeaching(), model.getUnidad());
          view.tbStudent.setModel(tdmActividades);
      }
  }

  //Accion refrfescar view
  public void btnRefrescar() {
    cargarDatosTablas();
    limpiarActividad();
    limpiarValidadoresActividad();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == view.btnSalir) {
      btnSalir();
    } else if (e.getSource() == view.btnRefrescar) {
      btnRefrescar();
    } else if (e.getSource() == view.btnCargarJson) {
      //AQUI LLAMAS EL METODO
      cargarJSON();
    } else if (e.getSource() == view.btnSubirActividad) {
      btnSubirActividad();
    } else if (e.getSource() == view.btnEliminarActividad) {
      btnEliminarActividad();
    } else if (e.getSource() == view.btnBuscarActividad) {
      btnBuscarActividad();
    } else if (e.getSource() == view.btnModificarActividad) {
      btnEditarActividad();
    } else if (e.getSource() == view.btnLimpiarActividad) {
      btnLimpiarActividad();
    } else if (e.getSource() == view.btnEliminarCalificaciones) {
      btnEliminarCalificar();
    } else if (e.getSource() == view.btnBuscarCalificaciones) {
      btnBuscarCalificar();
    } else if (e.getSource() == view.btnModificarCalificaciones) {
      btnEditarCalificar();
    } else if (e.getSource() == view.btnLimpiarCalificaciones) {
      btnLimpiarCalificar();
    } else if (e.getSource() == view.btnFiltroCalificacion) {
      btnFiltroActiviadad();
    }
  }
}
