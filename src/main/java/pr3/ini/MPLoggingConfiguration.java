package pr3.ini;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.zip.Deflater;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
//import org.apache.logging.log4j.core.appender.SmtpAppender;
//import org.apache.logging.log4j.core.appender.db.ColumnMapping;
//import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
//import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
//import org.apache.logging.log4j.core.appender.db.jdbc.DataSourceConnectionSource;
//import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
//import org.apache.logging.log4j.core.layout.HtmlLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Created by Дмитрий on 22.05.2017.
 */
public class MPLoggingConfiguration {

    public static final String PR3_LOGGER_NAME = "pr3";
    public static final String FILE_PATTERN_LAYOUT = "%n[%d{yyyy-MM-dd HH:mm:ss}] [%-5p] [%l]%n\t%m%n%n";
    public static final String LOG_FILE_NAME = "awmonitor.log";
    public static final String LOG_FILE_NAME_PATTERN = "awmonitor-%i.log";

    /**
     * Just to make JVM visit this class to initialize the static parts.
     */
    public static void configure() {
    }

    @Plugin(category = ConfigurationFactory.CATEGORY, name = "MPConfigurationFactory")
    @Order(15)
    public static class MPConfigurationFactory  extends ConfigurationFactory {
        public static final String[] SUFFIXES = new String[] {".json", "*"};

        @Override
        protected String[] getSupportedTypes() {
            return SUFFIXES;
        }

        @Override
        public Configuration getConfiguration(LoggerContext arg0, ConfigurationSource arg1) {
            return new Log4j2Configuration(arg1);
        }
    }

    private static class Log4j2Configuration extends DefaultConfiguration {

        public Log4j2Configuration(ConfigurationSource source) {
            super.doConfigure();
            setName("mp-log4j2");

//            String logFilePath = "/log/weblogic/wl-moniport/";
            String logFilePath = "C:\\Users\\Дмитрий\\IdeaProjects\\pr3\\log\\";

            // LOGGERS
            //      com.websitester
            AppenderRef[] refs = new AppenderRef[] {};
            Property[] properties = new Property[] {};
            LoggerConfig websitesterLoggerConfig = LoggerConfig.createLogger(true, Level.INFO, PR3_LOGGER_NAME, "true", refs, properties, this, null);
            addLogger(PR3_LOGGER_NAME, websitesterLoggerConfig);


            // APPENDERS
            final Charset charset = Charset.forName("UTF-8");

            //      MP ROLLING FILE
            TriggeringPolicy mpFileCompositePolicy = CompositeTriggeringPolicy.createPolicy(
                    SizeBasedTriggeringPolicy.createPolicy("3 M"),
                    OnStartupTriggeringPolicy.createPolicy(1));
            final DefaultRolloverStrategy mpFileRolloverStrategy = DefaultRolloverStrategy.createStrategy("9", "1", "max", Deflater.NO_COMPRESSION + "", null, true, this);
            Layout<? extends Serializable> mpFileLayout = PatternLayout.newBuilder()
                    .withPattern(FILE_PATTERN_LAYOUT)
                    .withPatternSelector(null)
                    .withConfiguration(this)
                    .withRegexReplacement(null)
                    .withCharset(charset)
                    .withAlwaysWriteExceptions(isShutdownHookEnabled)
                    .withNoConsoleNoAnsi(isShutdownHookEnabled)
                    .withHeader(null)
                    .withFooter(null)
                    .build();
            Appender mpFileAppender = RollingFileAppender.newBuilder()
                    .withAdvertise(Boolean.parseBoolean(null))
                    .withAdvertiseUri(null)
                    .withAppend(true)
                    .withBufferedIo(true)
                    .withBufferSize(8192)
                    .setConfiguration(this)
                    .withFileName(logFilePath + LOG_FILE_NAME)
                    .withFilePattern(logFilePath + LOG_FILE_NAME_PATTERN)
                    .withFilter(null)
                    .withIgnoreExceptions(true)
                    .withImmediateFlush(true)
                    .withLayout(mpFileLayout)
                    .withCreateOnDemand(false)
                    .withLocking(false)
                    .withName("File")
                    .withPolicy(mpFileCompositePolicy)
                    .withStrategy(mpFileRolloverStrategy)
                    .build();
            mpFileAppender.start();
            addAppender(mpFileAppender);
            getLogger(PR3_LOGGER_NAME).addAppender(mpFileAppender, Level.DEBUG, null);


            // JDBC
//            if (System.getProperty("log4jjdbcjndiName") != null){
//                ColumnConfig[] columnConfigs = new ColumnConfig[] {
//                        ColumnConfig.newBuilder()
//                                .setConfiguration(this)
//                                .setName("DATED")
//                                .setPattern(null)
//                                .setLiteral(null)
//                                .setEventTimestamp(true)
//                                .setUnicode(false)
//                                .setClob(false)
//                                .build(),
//                        ColumnConfig.newBuilder()
//                                .setConfiguration(this)
//                                .setName("LOGGER")
//                                .setPattern("%logger")
//                                .setLiteral(null)
//                                .setEventTimestamp(false)
//                                .setUnicode(false)
//                                .setClob(false)
//                                .build(),
//                        ColumnConfig.newBuilder()
//                                .setConfiguration(this)
//                                .setName("LOG_LEVEL")
//                                .setPattern("%level")
//                                .setLiteral(null)
//                                .setEventTimestamp(false)
//                                .setUnicode(false)
//                                .setClob(false)
//                                .build(),
//                        ColumnConfig.newBuilder()
//                                .setConfiguration(this)
//                                .setName("MESSAGE")
//                                .setPattern("%message")
//                                .setLiteral(null)
//                                .setEventTimestamp(false)
//                                .setUnicode(false)
//                                .setClob(false)
//                                .build(),
//                        ColumnConfig.newBuilder()
//                                .setConfiguration(this)
//                                .setName("NODE")
//                                .setPattern("" + System.getProperty("log4jmpserverid"))
//                                .setLiteral(null)
//                                .setEventTimestamp(false)
//                                .setUnicode(false)
//                                .setClob(false)
//                                .build()
//                };
//                ConnectionSource dataSourceConnectionSource = DataSourceConnectionSource.createConnectionSource(System.getProperty("log4jjdbcjndiName"));
//                if (dataSourceConnectionSource != null){
//                    Appender jdbcAppender = JdbcAppender.newBuilder()
//                            .setBufferSize(0)
//                            .setColumnConfigs(columnConfigs)
//                            .setColumnMappings(new ColumnMapping[]{})
//                            .setConnectionSource(dataSourceConnectionSource)
//                            .setTableName("MTDTLOGS")
//                            .withName("databaseAppender")
//                            .withIgnoreExceptions(true)
//                            .withFilter(null)
//                            .build();
//                    jdbcAppender.start();
//                    addAppender(jdbcAppender);
//                    getLogger(PR3_LOGGER_NAME).addAppender(jdbcAppender, Level.WARN, null);
//                }
//            }

            // SMTP
//            if (System.getProperty("log4jemailSubject") != null){
//                if (System.getProperty("log4jemailLevel").equalsIgnoreCase("error")) {
//                    Layout<? extends Serializable> mpHtmlLayout = HtmlLayout.createLayout(false, "Monitor de Portales", null, null, "x-small", null);
//
//                    Appender smtpAppender = SmtpAppender.createAppender(
//                            this,
//                            "SMTP",
//                            System.getProperty("log4jemailTo"),
//                            System.getProperty("log4jemailcc"),
//                            System.getProperty("log4jemailbcc"),
//                            System.getProperty("log4jemailFrom"),
//                            System.getProperty("log4jemailreplyTo"),
//                            System.getProperty("log4jemailSubject"),
//                            System.getProperty("log4jemailProtocol"),
//                            System.getProperty("log4jemailHost"),
//                            System.getProperty("log4jemailPort"),
//                            System.getProperty("log4jemailUserName"),
//                            System.getProperty("log4jemailPassword"),
//                            "false",
//                            "50",
//                            mpHtmlLayout,
//                            null,
//                            "true");
//                    smtpAppender.start();
//                    addAppender(smtpAppender);
//                    getLogger(PR3_LOGGER_NAME).addAppender(smtpAppender, Level.ERROR, null);
//                }
//            }
        }
    }
}
