package com.example.persona.demo.Logs;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;

import static com.example.persona.demo.Logs.Color.*;
import static java.awt.Color.MAGENTA;
@Component
public class SqlLogFormatter implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Logger sqlLogger = (Logger) LoggerFactory.getLogger("org.hibernate.SQL");
        Logger bindLogger = (Logger) LoggerFactory.getLogger("org.hibernate.type.descriptor.sql");
        sqlLogger.setLevel(Level.DEBUG);
        bindLogger.setLevel(Level.TRACE);

        PrettySqlAppender prettyAppender = new PrettySqlAppender();
        prettyAppender.setContext(sqlLogger.getLoggerContext());
        prettyAppender.start();

        PrettyBindAppender prettyBindAppender = new PrettyBindAppender();
        prettyBindAppender.setContext(bindLogger.getLoggerContext());
        prettyBindAppender.start();

        sqlLogger.addAppender(prettyAppender);
        bindLogger.addAppender(prettyBindAppender);
    }

    static class PrettySqlAppender extends AppenderBase<ILoggingEvent> {
        @Override
        protected void append(ILoggingEvent event) {
            String rawSql = event.getFormattedMessage();
            System.out.println(format(rawSql));
        }

        private String format(String sql) {
            if (sql == null) return null;

            // Colores ANSI
            final String RESET = "\u001B[0m";
            final String BLUE = "\u001B[34m";
            final String GREEN = "\u001B[32m";
            final String YELLOW = "\u001B[33m";
            final String MAGENTA = "\u001B[35m";
            final String CYAN = "\u001B[36m";

            return "\n-------------------------------------------------------------------------------------\n" +
                    "\n🟢 FORMATTED SQL:\n" +
                    sql.trim()
                            .replaceAll("\\s+", " ")  // Eliminar espacios extra
                            .replaceAll("(?i)\\b(select)\\b", BLUE + "$1" + RESET)  // SELECT en azul
                            .replaceAll("(?i)\\b(from)\\b", GREEN + "$1" + RESET)  // FROM en verde
                            .replaceAll("(?i)\\b(where)\\b", YELLOW + "$1" + RESET)  // WHERE en amarillo
                            .replaceAll("(?i)\\b(group by)\\b", MAGENTA + "$1" + RESET)  // GROUP BY en magenta
                            .replaceAll("(?i)\\b(order by)\\b", MAGENTA + "$1" + RESET)  // ORDER BY en magenta
                            .replaceAll("(?i)\\b(insert into)\\b", CYAN + "$1" + RESET)  // INSERT INTO en cian
                            .replaceAll("(?i)\\b(values)\\b", CYAN + "$1" + RESET)  // VALUES en cian
                            .replaceAll("(?i)\\b(update)\\b", GREEN + "$1" + RESET)  // UPDATE en verde
                            .replaceAll("(?i)\\b(set)\\b", GREEN + "$1" + RESET)  // SET en verde
                            .replaceAll("(?i)\\b(delete from)\\b", YELLOW + "$1" + RESET)  // DELETE FROM en amarillo
                            .replaceAll("(?i)\\b(create table)\\b", BLUE + "$1" + RESET)  // CREATE TABLE en azul
                            .replaceAll("(?i)\\b(left join)\\b", GREEN + "$1" + RESET)  // LEFT JOIN en verde
                            .replaceAll("(?i)\\b(inner join)\\b", GREEN + "$1" + RESET)  // INNER JOIN en verde
                            .replaceAll("(?i)\\b(on)\\b", YELLOW + "$1" + RESET)  // ON en amarillo
                            .replaceAll("(?i)\\b(as)\\b", MAGENTA + "$1" + RESET)
                            .replaceAll(",", ",\n    ") + // Agregar salto de línea después de las comas
                    ";\n-------------------------------------------------------------------------------------";
        }
    }
    static class PrettyBindAppender extends AppenderBase<ILoggingEvent> {
        @Override
        protected void append(ILoggingEvent event) {
            String message = event.getFormattedMessage();

            if (message.toLowerCase().startsWith("select") ||
                    message.toLowerCase().startsWith("insert") ||
                    message.toLowerCase().startsWith("update") ||
                    message.toLowerCase().startsWith("delete") ||
                    message.contains("bind")) {

                String pretty = formatSql(message);
                System.out.println("\n📝 SQL LOG:\n" + pretty + "\n");
            }
        }

        private String formatSql(String sql) {
            sql = sql.replaceAll("(?i)select", BLUE + "SELECT" + RESET);
            sql = sql.replaceAll("(?i)from", GREEN + "FROM" + RESET);
            sql = sql.replaceAll("(?i)where", YELLOW + "WHERE" + RESET);
            sql = sql.replaceAll("(?i)order by", MAGENTA + "ORDER BY" + RESET);
            sql = sql.replaceAll("(?i)group by", MAGENTA + "GROUP BY" + RESET);
            sql = sql.replaceAll("(?i)inner join", GREEN + "INNER JOIN" + RESET);
            sql = sql.replaceAll("(?i)left join", GREEN + "LEFT JOIN" + RESET);
            sql = sql.replaceAll("(?i)right join", GREEN + "RIGHT JOIN" + RESET);
            sql = sql.replaceAll("(?i)on", YELLOW + "ON" + RESET);
            sql = sql.replaceAll(",", YELLOW + ", " + RESET);
            sql = sql.replaceAll("\\s+", " ").trim();

            return sql;
        }
    }
}