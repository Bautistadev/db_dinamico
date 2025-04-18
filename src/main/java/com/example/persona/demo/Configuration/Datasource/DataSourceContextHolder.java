package com.example.persona.demo.Configuration.Datasource;

public class DataSourceContextHolder {

    /**
     Esta línea declara un ThreadLocal que almacenará el nombre de la base de datos que se utilizará para el hilo actual.
     Un ThreadLocal proporciona un almacenamiento de variables que es local al hilo de ejecución. Esto significa que cada hilo
     tiene su propia copia del valor, evitando que los valores de diferentes hilos interfieran entre sí.
     * */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();


    /*
    Este método estático permite establecer el nombre de la base de datos para el hilo actual.
    El valor proporcionado se almacena en contextHolder, lo que significa que cuando el hilo vuelva
    a ejecutar algún código que consulte el contexto, obtendrá el mismo valor para ese hilo.
    * */
    // Establece la base de datos que se usará para el hilo actual.
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }




    /*
    Este método estático devuelve el valor almacenado en el ThreadLocal para el hilo actual.
    Si no se ha establecido previamente un valor para ese hilo, contextHolder.get() devolverá null.
    Este método se usa para obtener la base de datos que debe usarse en el hilo actual, por ejemplo,
    en el contexto de enrutamiento dinámico de múltiples fuentes de datos.
   */
    public static String getDataSource() {
        return contextHolder.get();
    }




    /*
    Este método estático limpia el valor almacenado en el ThreadLocal para el hilo actual.
    Se utiliza para garantizar que el contexto de la base de datos se borre una vez que se haya terminado de usar.
    Es útil cuando se termina una operación o cuando se quiere asegurarse de que no haya valores residuales en el contexto que
    puedan interferir con otros hilos.
    * */
    // Limpia el contexto actual de la base de datos.
    public static void clearDataSource() {
        contextHolder.remove();
    }
}
