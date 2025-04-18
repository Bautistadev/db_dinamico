package com.example.persona.demo.Configuration.Datasource;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.*;

@Configuration
@EnableTransactionManagement
public class DataSourceManager {

    @Autowired
    private Environment env;

    private static final String DB_PREFIX = "db.datasource.db";


    /**
     Este bean crea y configura un DynamicRoutingDataSource, que es una clase personalizada que gestiona
     el enrutamiento dinámico entre múltiples fuentes de datos (bases de datos). En el código, primero se
     inicializa un Map<Object, Object> llamado dataSources, donde se almacenan las diferentes fuentes de datos.
     Se obtiene la cantidad de bases de datos que el sistema soportará (por medio del método getDbCount()) y, para cada base de datos,
     se construye una fuente de datos a través del método createDataSource(dbKey). Este DynamicRoutingDataSource utiliza el dataSources
     map como un conjunto de posibles fuentes de datos y selecciona la predeterminada como la primera base de datos (db1). Este enfoque es
     útil cuando se necesita cambiar dinámicamente entre diferentes bases de datos, por ejemplo, cuando se tienen bases de datos
     separadas para distintos entornos o funcionalidades.
     * */
    @Bean
    public DynamicRoutingDataSource dynamicDataSource() {
        Map<Object, Object> dataSources = new HashMap<>();
        int dbCount = getDbCount();

        for (int i = 1; i <= dbCount; i++) {
            String dbKey = "db" + i;
            dataSources.put(dbKey, createDataSource(dbKey));
        }

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        dynamicRoutingDataSource.setTargetDataSources(dataSources);
        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSources.get("db1")); // Por defecto
        return dynamicRoutingDataSource;
    }

    /*
    * Este bean configura un conjunto de LocalContainerEntityManagerFactoryBean, cada uno correspondiente a una
    * fuente de datos. El método entityManagerFactories() crea una lista de fábricas de gestores de entidades, una
    * para cada base de datos definida en el archivo de configuración. Para cada base de datos, se crea un
    * LocalContainerEntityManagerFactoryBean, se configura el DataSource correspondiente, y se ajustan las
    * propiedades de Hibernate como el dialecto, la estrategia para crear las tablas y las opciones de visualización de SQL.
    * Además, cada fábrica se configura para gestionar las entidades de una base de datos específica mediante el método
    * setPackagesToScan(), que asegura que solo se gestionen las entidades pertinentes para esa base de datos en particular.
    * Esto es útil cuando se trabaja con múltiples bases de datos en una aplicación.*/
    @Bean
    public List<LocalContainerEntityManagerFactoryBean> entityManagerFactories() {
        List<LocalContainerEntityManagerFactoryBean> factories = new ArrayList<>();
        int dbCount = getDbCount();

        for (int i = 1; i <= dbCount; i++) {
            String dbKey = "db" + i;

            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setDataSource(createDataSource(dbKey));
            factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
            factory.setJpaProperties(buildHibernateProperties(dbKey));
            factory.setPersistenceUnitName("unit_" + dbKey);
            factory.setPackagesToScan("com.example.persona.demo.Entity." + dbKey);

            factory.afterPropertiesSet();
            factories.add(factory);
        }

        return factories;
    }




    /*
    Este bean crea un JpaTransactionManager, que es responsable de gestionar las transacciones dentro de la aplicación.
    El JpaTransactionManager se configura con una instancia de EntityManagerFactory, lo que permite realizar operaciones de
    persistencia dentro de una transacción, asegurando la consistencia y la integridad de los datos. Este bean es fundamental
    para aplicaciones que necesitan gestionar transacciones en una base de datos, y la configuración del JpaTransactionManager
    asegura que las operaciones de la base de datos sean consistentes y se gestionen correctamente, especialmente cuando se
    están utilizando múltiples fuentes de datos.
    * */
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }





    /*
    Este es un método auxiliar que crea y configura un DataSource para una base de datos específica,
    identificado por su dbKey (por ejemplo, db1, db2, etc.). La configuración de la fuente de datos
    se obtiene a partir de las propiedades del entorno (env) utilizando claves construidas dinámicamente
    a partir de DB_PREFIX y el dbKey. El DataSource se configura con la URL, el nombre de usuario,
    la contraseña y el controlador JDBC para cada base de datos. Este método es utilizado tanto en el bean dynamicDataSource()
    como en el bean entityManagerFactories(), garantizando que cada base de datos tenga su propia fuente de datos bien configurada.
    * */
    private DataSource createDataSource(String dbKey) {
        return DataSourceBuilder.create()
                .url(env.getProperty(DB_PREFIX + dbKey.substring(2) + ".url"))
                .username(env.getProperty(DB_PREFIX + dbKey.substring(2) + ".username"))
                .password(env.getProperty(DB_PREFIX + dbKey.substring(2) + ".password"))
                .driverClassName(env.getProperty(DB_PREFIX + dbKey.substring(2) + ".driver-class-name"))
                .build();
    }





    /*
    Este método auxiliar se encarga de construir las propiedades específicas de Hibernate para cada base de datos,
    como el dialecto, las configuraciones de creación de tablas, la visualización de SQL y los comentarios en el SQL.
    Se obtienen propiedades del entorno para configurar dinámicamente el dialecto de Hibernate correspondiente a cada base de datos.
    También se establecen propiedades adicionales que son necesarias para que Hibernate gestione correctamente las operaciones
    de base de datos, como hibernate.hbm2ddl.auto, que controla la estrategia de creación de esquemas en la base de datos.
    Las propiedades de Hibernate se usan para personalizar el comportamiento del ORM y asegurarse de que las entidades sean
    gestionadas correctamente.
    * */
    private Properties buildHibernateProperties(String dbKey) {
        Properties properties = new Properties();
        String dialect = env.getProperty(DB_PREFIX + dbKey.substring(2) + ".hibernate.dialect");

        if (dialect != null) {
            properties.put("hibernate.dialect", dialect);
        }

        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true"); // Agregar comentarios en el SQL
        return properties;
    }





    /*
    Este es un método auxiliar que calcula cuántas bases de datos están configuradas en el entorno.
    Lo hace iterando sobre las propiedades de configuración y verificando cuántos valores existen
    para las claves db.datasource.dbX.url, donde X es un número que representa cada base de datos configurada.
    Si encuentra una propiedad para una base de datos (por ejemplo, db.datasource.db1.url), aumenta el contador count.
    Este método es esencial para determinar cuántas bases de datos se deben configurar y gestionar, ya que el número de bases de datos
    se obtiene dinámicamente y se utiliza en otras partes del código para definir la cantidad de fuentes de datos a crear y
    configurar.
    */
    private int getDbCount() {
        int count = 0;
        while (env.containsProperty(DB_PREFIX + (count + 1) + ".url")) {
            count++;
        }
        return count;
    }
}
