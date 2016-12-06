package tarea10;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*Clase que crea las tablas y los objetos para poder guardar todos los datos introducidos.
 Las palabras no pueden estar repetidas */

public class Sql {
//El método recibe como parámetro la conexión a Oracle. Una vez conectado crerá las tablas.
    public static void crear_tablas(Connection conexionOracle) throws SQLException {
        /*Statement es la clase que se utiliza para ejecutar una sentencia SQL estática y devovler
         los resultados que la misma proporciona.
         El método createStatement() genera el objeto que será enviado a la base de datos, según lo definido en 
         el statement.
         También se podría usar preparedStatement() que recibe como parámetro la sentencia SQL y almacena los objetos 
         precompilados. Es recomendado para conexiones recurrentes. Como las tablas sólo se crean una vez no es necesario*/
        Statement sentencia = conexionOracle.createStatement();
        try {
            /* Creamos un objeto que guardará las palabras, las veces que se han visto y la tabla de traducciones.
             Se permite heredar de la misma */
            sentencia.execute("CREATE OR REPLACE TYPE objeto_palabra AS OBJECT("
                    + "palabra VARCHAR2(25),"
                    + "veces_visto INTEGER,"
                    + "traducciones tabla_traduccion"
                    + ") NOT FINAL");

            /* Creamos los (objetos_traducción) que guardan el idioma y la traduccion a ese idioma*/
            sentencia.execute("CREATE OR REPLACE TYPE objeto_traduccion AS OBJECT("
                    + "idioma varchar2(25),"
                    + "traduccion varchar2(25)"
                    + ") NOT FINAL");

            //Creamos el tipo que almacenará los objeto de traducción 
            sentencia.execute("CREATE OR REPLACE TYPE tabla_traduccion AS TABLE OF objeto_traduccion");

            // La tabla diccionario está formada por objetos de palabra, la clave primaria son las palabras
            sentencia.execute("CREATE OR REPLACE TABLE diccionario OF objeto_palabra"
                    + " ( CONSTRAINT palabra_c PRIMARY KEY ( palabra ),"
                    + " CHECK ( palabra IS NOT NULL ) )"
                    //La tabla traducciones se guarda dentro de diccionario
                    + " NESTED TABLE traducciones STORE AS traduc_nestedtable");

        } catch (SQLException e) {
            System.err.println("Tablas ya existentes");
        }
    }
}
