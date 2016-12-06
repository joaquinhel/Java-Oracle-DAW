package tarea10;

import java.sql.Array;
import java.sql.Connection; //Clases para poder conectarse vía JDBC a una base de datos
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; //Relaciona una clave con una variable
import java.util.Iterator;

/**
 * @author Joaquín Diccionario de traducciones
 */
public class Tarea10 {

    public static void main(String[] args) throws SQLException {
        int error = 0;
        int numero = 0;
        int visto;

        //Utilizando la clase proporcionada,creamos un objeto para realizar la conexión con la base de datos.
        ConexionAOracle conexionBD = new ConexionAOracle();
        String namespace = "XE"; //Esquema de la base de datos con la que estamos trabajando
        String usuario = "PROG10";//Debemos introducir el usuario con el que queremos realizar la conexión
        String password = "xxxxxx";//Clave que se corresponde con el usuario de antes
        Sql tablas = new Sql();
        String sentenciaSQL;
        String lengua;
        String traduc;
        PreparedStatement ps; //Aumenta la velocidad del programa, no analiza cada vez que se ejecuta la sentencia
        Array mi_array;
        String termino;
        /*Con la clase connection nos conectamos a oracle utilizando un objeto(conexionBD) de la clase facilitadora conexionAOracle
         la cual nos permite conectarnos a la base de datos en local. Le pasamos como parámetros tres Strings con los datos necesarios*/
        Connection conexionOracle = conexionBD.construirConexion(namespace, usuario, password);
        //Una vez que se ha creado la conexión
        if (conexionOracle != null) {
            tablas.crear_tablas(conexionOracle); //Creamos las tablas
            do {
                try {
                    //Creamos el menú de opcions con un formato adecuado
                    System.out.print("\n----------------------------------------------------------");
                    System.out.print("\n\t\tAPLICACIÓN DICCIONARIO");
                    System.out.print("\n----------------------------------------------------------");
                    System.out.print("\nElige una opción:");
                    System.out.print("\n\t1.- Añadir una nueva palabra al diccionario.");
                    System.out.print("\n\t2.- Ver todas las palabras almacenadas en Castellano "
                            + "y las veces que han sido vistas.");
                    System.out.print("\n\t3.- Introduzca la palabra de la que desea ver las traducciones.");
                    System.out.print("\n\t4.- Eliminar una palabra y sus traducciones.");
                    System.out.print("\n\t5.- Salir de la aplicación.");
                    System.out.print("\n----------------------------------------------------------\n");
                    //Solicitamos la entrada al switch que no puede estar vacia(paramétro TRUE)
                    numero = UtilConsola.leerEntero("\nIntroduce una de las opciones anteriores (1 al 5): ", true);

                    switch (numero) {
                        //Guardar en el diccionario una nueva palabra
                        case 1:
                            //Creamos la lista que contendrá la palabra y su traducción
                            Hashtable lista = new Hashtable();
                            /*Llamamos a la clase facilitadora con la que leemos la palabra introducida por teclado
                             Muestra como mensaje el primer parámetro, comprueba que no esté en blanco (parámetro TRUE) y
                             elimina los posibles espacios en blanco*/
                            termino = UtilConsola.leerCadena("\nIntroduce una palabra en CASTELLANO: ", true);
                            //Llamamos al método exista que comprueba si tenemos la palabra ya almacenada en el diccionario
                            while (exista(termino, conexionOracle)) {
                                //Si el termino ya existe
                                System.out.println("\nEsa palabra ya esta almacenada");
                                //Volvemos a llamar a la clase facilitadora
                                termino = UtilConsola.leerCadena("\nIntroduce otra palabra: ", true);
                            }

                            String idioma = UtilConsola.leerCadena("Introduce el nombre del idioma al que desea traducirla: ", true);
                            //Mientras que se le de un valor a idioma
                            while (idioma != null) {
                                String traduccion = UtilConsola.leerCadena("Introduce la traducción de la palabra al idioma propuesto: ", true);
                                lista.put(idioma, traduccion);//Añadimos el nombre del idioma y la traducción a la lista HashTable
                                idioma = UtilConsola.leerCadena("Introduce el nombre del idioma al que desea traducir ahora la palabra o "
                                        + "pulsa enter para salir y guardar: ", false); //Al poner false se puede dejar en blanco
                            }
                            System.out.println("Ha guardado la palabra \"" + termino + "\" y sus traducciones.");
                            Iterator<String> it = (Iterator) lista.keys();//Iteramos las llaves de la HashTable
                            //Guardamos la palabra(termino), veces_visto vale 0 y tabla_traducción
                            sentenciaSQL = "INSERT INTO diccionario VALUES ('" + termino + "',0, tabla_traduccion (";
                            while (it.hasNext()) {
                                idioma = it.next(); //Mientras se introduzca un nuevo idioma
                                sentenciaSQL = sentenciaSQL.concat("objeto_traduccion('"); //Cremos un nuevo objeto de traducción
                                sentenciaSQL = sentenciaSQL.concat(idioma);//Que almacenará el idioma al que vamos a realizar la traducción
                                sentenciaSQL = sentenciaSQL.concat("', '"); //Ponemos una coma
                                sentenciaSQL = sentenciaSQL.concat((String) lista.get(idioma));//Mostramos el valor asociado en la tabla al idioma
                                if (it.hasNext()) {//Si hay más datos 
                                    sentenciaSQL = sentenciaSQL.concat("'), "); //Ponemos un como y guardamos la siguiente traducción
                                } else { //Si no tenemos más datos
                                    sentenciaSQL = sentenciaSQL.concat("')))"); //Cerramos la sentencia SQL
                                }
                            }

                            //Con RETURN_GENERATED_KEYS especificamos que queremos tener acceso a la clave primaria
                            ps = conexionOracle.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
                            ps.executeUpdate(); //Llevamos a cabo la actualización, guardamos la siguiente palabra. No devuelve nada.
                            break;

                        // Listamos las palabras almacenadas en el diccionario;
                        case 2:
                            sentenciaSQL = "SELECT * FROM diccionario"; //Muestra todas las palabras almacenadas
                            ps = conexionOracle.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
                            //ResultSet devuelve los datos que proporciona la consulta (sentenciaSQL) a la Base de Datos
                            try (ResultSet datosConsulta = ps.executeQuery(sentenciaSQL)) {
                                while (datosConsulta.next()) {//Mientras la consulta devuelva datos
                                    //Imprimimos por pantalla las veces que ha sido vista
                                    System.out.print("\n La palabra \"" + datosConsulta.getString(1) + "\", ha sido vista: " + datosConsulta.getInt(2) + " veces.");
                                }
                            }
                            break;

                        // Ver el listado de terminos a buscar por aproximación;
                        case 3:
                            /*Llamamos a la clase facilitadora con la que leemos la palabra introducida por teclado
                             Muestra como mensaje el primer parámetro, comprueba que no esté en blanco (parámetro TRUE) y
                             elimina los posibles espacios en blanco*/
                            termino = UtilConsola.leerCadena("\nIntroduce la palabra a buscar: ", true);
                            //Buscamos todas las palabras de la base de datos que coincide totalmente o en parte con el termino introducido
                            sentenciaSQL = "SELECT * FROM diccionario WHERE (palabra LIKE '%" + termino + "%')";
                            //Recuperamos la clave primaria
                            ps = conexionOracle.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
                            //Recuperamos los datos de la consulta (sentenciaSQL)
                            try (ResultSet datosConsulta = ps.executeQuery(sentenciaSQL)) {
                                while (datosConsulta.next()) { //Mientras existan datos
                                    //Aumentamos las visualizaciones en uno
                                    visto = datosConsulta.getInt(2) + 1;
                                    //Imprimimos por consola los 
                                    System.out.print("\nPalabra: " + datosConsulta.getString(1)
                                            + "   \nTraducciones: ");
                                    //Guardamos en un array de objetos los idiomas y las traducciones de los objetos tabla de traduccion
                                    mi_array = datosConsulta.getArray(3);
                                    Object[] traducciones = (Object[]) mi_array.getArray();
                                    //Recorremos todas las posiciones del array de objetos traducciones
                                    for (int x = 0; x < traducciones.length; x++) {
                                        /*Struct realiza el mapeo de datos, de forma que los recupera de los obejtos de la base de datos 
                                         y los transforma a formato java*/
                                        java.sql.Struct obj1 = (java.sql.Struct) traducciones[x];
                                        lengua = (String) obj1.getAttributes()[0]; //Extraemos la lengua
                                        traduc = (String) obj1.getAttributes()[1]; //Extraemos la traducción
                                        //Imprimimos por consola la lengua y la traducción
                                        System.out.print(lengua + " - " + traduc + ", ");
                                    }
                                    //Imprimimos las veces que ha sido visualizado
                                    System.out.print("visualizado " + visto + " veces");
                                }
                            }
                            // Incremento el campo visto de los registros de la consulta anterior sumando 1 a veces visto
                            sentenciaSQL = "UPDATE diccionario SET veces_visto = veces_visto +1 WHERE (palabra LIKE '%" + termino + "%')";
                            ps = conexionOracle.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
                            ps.executeUpdate(sentenciaSQL);
                            break;

                        // Eliminamos un determinado termino con sus traducciones del diccionario
                        case 4:
                            //Usamos la clase facilitadora UtilConsola
                            termino = UtilConsola.leerCadena("\nIntroduce la palabra a borrar: ", true);
                            //Borramos con la clausula delete la palabra almacenada
                            sentenciaSQL = "DELETE FROM diccionario WHERE (palabra = '" + termino + "')";
                            //Establecemos las claves primarias
                            ps = conexionOracle.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
                            //Llevamos a cabo el borrado
                            ps.executeUpdate();
                            System.out.println("El borrado de \"" + termino + "\" se ha realizado de forma correcta.");
                            break;
                        // Salimos de la aplicación sin nada más;
                        case 5:
                            System.out.println("Ha cerrado la aplicacion");
                            break;
                        //Si se introduce un número que no esté entre el 1 y el 5 mostramos la opción por defecto del Switch
                        default:
                            System.out.println("Sólo del 1 al 5");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("\nDebe introducir números.\n\n");

                } catch (IllegalArgumentException e) {
                    System.err.println("\n" + e.getMessage() + "\n\n");
                } finally {
                    if (numero != 4 && error != 0) {
                        System.err.println("\n Error \n\n");
                        numero = 0;
                    }
                }
            } while (numero != 5);
            //Cerramos la conexión a la base de dats
            conexionOracle.close();
        } else {
            //Si se produce un error a realizar la conexión mostramos el siguiente mensaje
            System.err.println("Conexión imposible. Revise la configuración.");
        }
    }

    /*Función con la que comprobamos si el termino ya está en la base de datos
     String termino es la palabra que vamos a buscar.
     Connection conexionOracle es la conexión con la base de datos Oracle
     return devuelve verdadero en caso de que exista*/
    public static boolean exista(String termino, Connection conexionOracle) throws SQLException {
        //Preparamos la consulta SQL
        String sentenciaSQL = "SELECT * FROM diccionario WHERE (palabra = '" + termino + "')";
        //Realimos la consulta a la base de datos
        PreparedStatement ps = conexionOracle.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS);
        //Obtenemos los resultados de la consulta
        ResultSet datosConsulta = ps.executeQuery(sentenciaSQL);
        //Avanzamos el cursor una posición
        datosConsulta.next();
        //getRow() lleva a cabo el mapeo de datos 
        if (datosConsulta.getRow() == 0) {//Devuelve 0 si el dato no existe
            return false;
        } else { //Si existe nos devuelve su número de fila
            return true;
        }
    }
}
