package Model;

import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;

public final class mongoDB {
    private final MongoClient mongoClient;
    private MongoDatabase database;
    private static mongoDB instance; // Singleton instance
    public MongoCollection<Document> collection;
    private String dbName, collName;
    private String dbMsg = "";

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getCollName() {
        return collName;
    }

    public void setCollName(String collName) {
        this.collName = collName;
    }

    public MongoCollection<Document> getCollection() {
        return collection = getInstance().getDatabase().getCollection(collName);
    }

    public void setCollection(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public String getDbMsg() {
        return dbMsg;
    }

    public void setDbMsg(String dbMsg) {
        this.dbMsg = dbMsg;
    }
    
    //PATRON SINGLE
    public mongoDB(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    
    //PATRON SINGLE
    private mongoDB() {
        mongoClient = (MongoClient) MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("P3Proyecto_Mendoza_Ruiz");
        // Subcategorías dentro de Actividades
        String[] colecciones = {"Usuarios","Credenciales", "Docentes", "Estudiantes", "Actividades", 
            "Actividades_Fisica", "Actividades_Programacion", "Actividades_CalculoVectorial", 
            "Actividades_Quimica"};
        for (String subcategoria : colecciones) {
            setCollName(subcategoria);
            if (database.getCollection(getCollName()).countDocuments() == 0) {
                System.out.println("SE CREO CON EXITO LA COLECCION : "+ getCollName());
                database.createCollection(getCollName());
            }
        }
    }
    
    // Método estático para obtener la instancia Singleton
    //PATRON SINGLE
    public static mongoDB getInstance() {
        if (instance == null) {
            instance = new mongoDB();
        }
        return instance;
    }
    
    //PATRON SINGLE
    public MongoDatabase getDatabase() {
        return database;
    }
    
    /*METODOS DE CRUD*/
    //CREATE desde BD
    public void createDocument(Document documento) {
        collection = getInstance().getDatabase().getCollection(collName);
        if (collection.countDocuments() == 0) {
            dbMsg = "Colección " + collName + " no existente, se ha creado.";
        }
        collection.insertOne(documento);
    }

    //UPDATE Actualizar desde BD
    public void updateDocument(Document filtro, Document actualizado) {
        collection = getInstance().getDatabase().getCollection(collName);
        UpdateOptions options = new UpdateOptions().upsert(false);
        collection.updateOne(filtro, new Document("$set", actualizado), options);
    }
    //DELETE desde BD

    public void deleteDocument(Document filtro) {
        collection = getInstance().getDatabase().getCollection(collName);
        collection.deleteOne(filtro);
    }

    //READER Leer desde BD
    public ArrayList<Document> readDocument() {
        collection = getInstance().getDatabase().getCollection(collName);
        ArrayList<Document> datos = new ArrayList<>();
        for (Document doc : collection.find()) {
            datos.add(doc);
        }
        return datos;
    }
    //Verificar registros en collecciones
    public boolean validateDocument (String id_Key, String id_Search) {
        collection = getInstance().getDatabase().getCollection(collName);
        Document filtro = new Document(id_Key, id_Search);
        Document resultado = collection.find(filtro).first();
        return resultado != null;
    }
    //Buscar desde la BD
    public ArrayList<Document> searchDocument(Document filtro) {
        collection = getInstance().getDatabase().getCollection(collName);
        ArrayList<Document> resultados = new ArrayList<>();
        for (Document doc : collection.find(filtro)) {
            resultados.add(doc);
        }
        return resultados;
    }
    
    public static ArrayList<Document> returnDocuments(MongoCollection<Document> collection) {
        ArrayList<Document> documentos = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                documentos.add(cursor.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documentos;
    }
    
    public DefaultTableModel cargarDataTableActividades(String materia) {
        MongoCollection<Document> collection = null;
        // Mapear el nombre de la materia a la colección correspondiente
        if (materia.equalsIgnoreCase("Programacion")) {
            collection = database.getCollection("Actividades_Programacion");
        } else if (materia.equalsIgnoreCase("Quimica")) {
            collection = database.getCollection("Actividades_Quimica");
        } else if (materia.equalsIgnoreCase("Calculo Vectorial")) {
            collection = database.getCollection("Actividades_CalculoVectorial");
        } else if (materia.equalsIgnoreCase("Fisica")) {
            collection = database.getCollection("Actividades_Fisica");
        }
        // Verificar si la colección es nula
        if (collection == null) {
            System.err.println("Error: La colección para la materia '" + materia + "' no existe.");
            return new DefaultTableModel(); // Devolver un modelo vacío
        }
        // Crear una lista de documentos
        List<Document> documents;
        try {
            documents = collection.find().into(new ArrayList<>());
        } catch (MongoException e) {
            e.printStackTrace();
            System.err.println("Error al acceder a la colección: " + e.getMessage());
            return new DefaultTableModel(); // Devolver un modelo vacío en caso de error
        }
        // Definir los nombres de las columnas
        String[] columnNames = {"Unidad", "Actividad", "Titulo", "Detalle", "Ponderacion"};  // Cambia estos según tus campos en MongoDB
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);
        // Iterar sobre los documentos y añadir filas al modelo de la tabla
        for (Document doc : documents) {
            Object[] row = {
                doc.get("Unidad"),
                doc.get("Actividad"),
                doc.get("Titulo"),
                doc.get("Detalle"),
                doc.get("Ponderacion"),
            };
            tdm.addRow(row);
        }
        return tdm;
    }
    public DefaultTableModel cargarDataTableCalificar(String collectionName, String materia) {
        // Obtener la colección "Actividades"
        collection = database.getCollection(collectionName);
        // Crear un filtro para la materia
        Document filtro = new Document("Materia", materia);
        // Obtener los documentos que coincidan con el filtro
        List<Document> documents = collection.find(filtro).into(new ArrayList<>());
        // Definir los nombres de las columnas
        String[] columnNames = {"Actividad", "Titulo", "Unidad", "Detalle", "Nombre-Apellido", "Calificacion", "Estado", "Comentario", "Observacion"};
        // Crear un modelo de tabla
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);
        // Iterar sobre los documentos y añadir filas al modelo de la tabla
        for (Document doc : documents) {
            Object[] row = {
                doc.get("Actividad"),
                doc.get("Titulo"),
                doc.get("Unidad"),
                doc.get("Detalle"),
                doc.get("Nombre-Apellido"),
                doc.get("Calificacion"),
                doc.get("Estado"),
                doc.get("Comentario"),
                doc.get("Observacion")
            };
            tdm.addRow(row);
        }
        return tdm;
    }
    public DefaultTableModel cargarDataTableCalificar(String collectionName, String materia, String filtroUnidad) {
        // Obtener la colección "Actividades"
        collection = database.getCollection(collectionName);
        // Crear un filtro para la materia
        Document filtro = new Document("Materia", materia).append("Unidad", filtroUnidad);
        // Obtener los documentos que coincidan con el filtro
        List<Document> documents = collection.find(filtro).into(new ArrayList<>());
        // Definir los nombres de las columnas
        String[] columnNames = {"Actividad", "Titulo", "Unidad", "Detalle", "Nombre-Apellido", "Calificacion", "Estado", "Comentario", "Observacion"};
        // Crear un modelo de tabla
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);
        // Iterar sobre los documentos y añadir filas al modelo de la tabla
        for (Document doc : documents) {
            Object[] row = {
                doc.get("Actividad"),
                doc.get("Titulo"),
                doc.get("Unidad"),
                doc.get("Detalle"),
                doc.get("Nombre-Apellido"),
                doc.get("Calificacion"),
                doc.get("Estado"),
                doc.get("Comentario"),
                doc.get("Observacion")
            };
            tdm.addRow(row);
        }
        return tdm;
    }
    public DefaultTableModel cargarDataTableCalificacioUnidades(String collectionName, String filtroUnidad, String cedula, String Materia) {
        // Obtener la colección "Actividades"
        collection = database.getCollection(collectionName);
        // Crear un filtro para la materia
        Document filtro = new Document("Cedula", cedula).append("Unidad", filtroUnidad).append("Materia", Materia);
        // Obtener los documentos que coincidan con el filtro
        List<Document> documents = collection.find(filtro).into(new ArrayList<>());
        // Definir los nombres de las columnas
        String[] columnNames = {"Actividad", "Titulo", "Unidad", "Detalle", "Nombre-Apellido", "Calificacion", "Estado", "Comentario", "Observacion"};
        // Crear un modelo de tabla
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);
        // Iterar sobre los documentos y añadir filas al modelo de la tabla
        for (Document doc : documents) {
            Object[] row = {
                doc.get("Actividad"),
                doc.get("Titulo"),
                doc.get("Unidad"),
                doc.get("Detalle"),
                doc.get("Nombre-Apellido"),
                doc.get("Calificacion"),
                doc.get("Estado"),
                doc.get("Comentario"),
                doc.get("Observacion")
            };
            tdm.addRow(row);
        }
        return tdm;
    }
    public DefaultTableModel cargarDataTableCalificaciones(String collectionName, String filtroUnidad, String cedula, String Materia, String actividad) {
        // Obtener la colección "Actividades"//"Actividades", unidad, getCedula(), materia, actividad
        collection = database.getCollection(collectionName);
        // Crear un filtro para la materia
        Document filtro = new Document("Cedula", cedula).append("Unidad", filtroUnidad).append("Materia", Materia).append("Actividad", actividad);
        // Obtener los documentos que coincidan con el filtro
        List<Document> documents = collection.find(filtro).into(new ArrayList<>());
        // Definir los nombres de las columnas
        String[] columnNames = {"Actividad", "Ponderacion", "Calificaion", "Porcentaje", "Retroalimentacion"};
        // Crear un modelo de tabla
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);
        // Iterar sobre los documentos y añadir filas al modelo de la tabla
        for (Document doc : documents) {
            Object[] row = {
                doc.get("Actividad"),
                doc.get("Ponderacion"),
                doc.get("Calificaion"),
                doc.get("Porcentaje"),
                doc.get("Retroalimentacion")
            };
            tdm.addRow(row);
        }
        return tdm;
    }
}
