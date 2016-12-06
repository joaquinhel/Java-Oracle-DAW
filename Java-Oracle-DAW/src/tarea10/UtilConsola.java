package tarea10;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Clase simple con utilidades básicas para entrada de información por consola.
 *
 * @author Salvador Romero Villegas
 */
public class UtilConsola {

    private static Scanner teclado = new Scanner(System.in);

    /**
     * Lee una cadena de la entrada estandar.
     *
     * @param repetirMientrasVacio Si es false, permitirá cadenas vacias como
     * entrada. Si es true, no permitirá cadenas vacías, obligará a introducir
     * una. Una cadena vacía se consigue simplemente pulsando Enter.
     * @param mensaje Pequeño trozo de texto que se mostrará al usuario para
     * indicar que debe introducir.
     * @return Retornará la cadena, o null si el usuario no ha introducido
     * ningún dato.
     */
    public static String leerCadena(String mensaje, boolean repetirMientrasVacio) {

        String cadena = null;
        do {
            if (cadena != null) {
                System.out.println("ERROR: En este caso, no es admisible un valor vacio o nulo.");
            }
            System.out.print(mensaje);
            cadena = teclado.nextLine();
        } while (repetirMientrasVacio && (cadena == null || cadena.length() == 0));
        cadena = cadena.trim();
        if (cadena.isEmpty()) {
            cadena = null;
        }
        return cadena;
    }

    /**
     * Lee un valor de un conjunto indicado y retorna un valor asociado. Util
     * para pedir al usuario que indique una opción entre varias.
     *
     * @param repetirMientrasVacio Si es false, permitirá cadenas vacias como
     * entrada. Si es true, no permitirá cadenas vacías, obligará a introducir
     * una. Una cadena vacía se consigue simplemente pulsando Enter.
     * @param mensaje Pequeño trozo de texto que se mostrará al usuario para
     * indicar que debe introducir.
     * @param opciones Permite indicar la opción que el usario tendrá que
     * introducir. Se indica de la siguiente forma "entrada=valor a
     * retornar,entrada2=valor a retornar". Por ejemplo:
     * "s=SI,si=SI,n=NO,no=NO".
     * @param ignoreCase Permite indicar si se deben considerar las mayusculas o
     * no. Si es true, se ignoran.
     * @return Retornará la cadena, o null si el usuario no ha introducido
     * ningún dato.
     */
    public static String leerOpciones(String mensaje, String opciones, boolean repetirMientrasVacio, boolean ignoreCase) {

        String cadena = null;
        String[] splittedOptions = opciones.trim().split(",");
        HashMap<String, String> t = new HashMap<String, String>();
        for (int i = 0; i < splittedOptions.length; i++) {
            String[] value = splittedOptions[i].trim().split("=");
            if (ignoreCase) {
                value[0] = value[0].toLowerCase();
            }
            if (value.length > 1) {
                t.put(value[0].trim(), value[1].trim()); //trim quita los espacios en blanco
            } else {
                t.put(value[0].trim(), "NULL");
            }
        }
        do {
            if (cadena != null) {
                System.out.println("ERROR: En este caso, no es admisible un valor vacio o nulo.");
            }
            System.out.print(mensaje);
            cadena = teclado.nextLine().trim(); //.trim elimina los espacios en blanco de la cadena
            if (ignoreCase) {
                cadena = cadena.toLowerCase();
            }
        } while (repetirMientrasVacio && !t.containsKey(cadena));

        if (t.containsKey(cadena)) {
            cadena = t.get(cadena);
        } else {
            cadena = null;
        }
        return cadena;
    }

    /**
     * Lee un entero desde consola. Si no puede leer el entero o se produce un
     * error, entonces retorna null.
     *
     * @param mensaje Pequeño trozo de texto que se mostrará al usuario para
     * indicar que debe introducir.
     * @param RepetirHastaCorrecto Si este parámetro es true se pedirá una y
     * otra vez el número mientras este sea incorrecto.
     * @return El entero leido o null si no se ha podido procesar el número.
     */
    public static Integer leerEntero(String mensaje, boolean RepetirHastaCorrecto) {
        Integer opt = null;
        do {
            System.out.print(mensaje);
            try {
                opt = Integer.parseInt(teclado.nextLine().trim());
            } catch (Exception o) {
                opt = null;
            }
            if (RepetirHastaCorrecto && opt == null) {
                System.out.println("Número entero incorrecto. Por favor, introduzca un número entero.");
            }
        } while (RepetirHastaCorrecto && opt == null);
        return opt;
    }

    /**
     * Lee un decimal desde consola. Si no puede leer el decimal o se produce un
     * error, entonces retorna null.
     *
     * @param mensaje Pequeño trozo de texto que se mostrará al usuario para
     * indicar que debe introducir.
     * @param RepetirHastaCorrecto Si este parámetro es true se pedirá una y
     * otra vez el número mientras este sea incorrecto.
     * @return El entero leido o null si no se ha podido procesar el número.
     */
    public static Float leerDecimal(String mensaje, boolean RepetirHastaCorrecto) {
        Float opt = null;
        do {
            System.out.print(mensaje);
            try {
                opt = Float.parseFloat(teclado.nextLine().trim());
            } catch (Exception o) {
                opt = null;
            }
            if (RepetirHastaCorrecto && opt == null) {
                System.out.println("Número decimal incorrecto. Por favor, introduzca un número decimal.");
            }
        } while (RepetirHastaCorrecto && opt == null);
        return opt;
    }

    /**
     * Lee una fecha desde consola. Si no puede leer la fecah o se produce un
     * error, entonces retorna null.
     *
     * @param mensaje Pequeño trozo de texto que se mostrará al usuario para
     * indicar que debe introducir.
     * @param RepetirHastaCorrecto Si este parámetro es true se pedirá una y
     * otra vez la fecha mientras esta sea incorrecta.
     * @return La fecha leida o null si no se ha podido procesar la fecha.
     */
    public static Date leerFecha(String mensaje, boolean RepetirHastaCorrecto) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/y");
        Date d = null;
        do {
            System.out.print(mensaje);
            try {
                d = sdf.parse(teclado.nextLine().trim());
            } catch (Exception o) {

                d = null;
            }
            if (RepetirHastaCorrecto && d == null) {
                System.out.println("Fecha incorrecta. Por favor, introduzca una fecha en formato dd/mm/yyyy.");
            }
        } while (RepetirHastaCorrecto && d == null);
        return d;

    }

    @Override
    protected void finalize() throws Throwable {
        teclado.close();
        super.finalize();
    }

}
