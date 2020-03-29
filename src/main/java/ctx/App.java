package ctx;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;


/**
 * Exposes the Spring context statically.
 *
 * You can get a bean object from the context like the following, without the
 * need to inject the bean into your class:
 *
 * <pre>
 *
 *     // Get a business object by name or class
 *     MyUserService myUserService = ctx.App.getBean(MyUserService.class);
 *
 *     // Get any configuration property, eg. the context-path of the app
 *     ctx.App.getProp("server.context-path")
 *     // return => "/api"
 *
 *    // Use the bean object to get a result and print out
 *    // in pretty print JSON format using the the same serialization
 *    // configuration your application has (ObjectMapper)
 *    ctx.App.ppjson(myUserService.findById(1234L))
 *    // Prints the following =>
 *    {
 *      "name" : "Jhon",
 *      "lastName" : "Due",
 *      "age" : null
 *    }
 *
 * </pre>
 */
public class App implements ApplicationContextAware {

    private static ApplicationContext context;

    private static ObjectMapper mapper;

    /**
     * The application context created by the Spring Framework,
     * with all the beans created
     */
    public static ApplicationContext getContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Returns an instance, which may be shared or independent, of the specified bean
     * hold by the application context.
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     * @throws NoSuchBeanDefinitionException if there is no bean definition
     * with the specified name
     * @throws BeansException if the bean could not be obtained
     * @see org.springframework.beans.factory.BeanFactory#getBean(String)
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * Returns the bean instance that uniquely matches the given object type, if any.
     * @param requiredType type the bean must match; can be an interface or superclass.
     * {@code null} is disallowed.
     * @return an instance of the single bean matching the required type
     * @throws NoSuchBeanDefinitionException if no bean of the given type was found
     * @throws BeansException if the bean could not be created
     * @see org.springframework.beans.factory.BeanFactory#getBean(Class)
     */
    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }

    /**
     * Returns the {@link Environment} associated with this component.
     * @see ApplicationContext#getEnvironment()
     */
    public static Environment getEnv() {
        return context.getEnvironment();
    }

    /**
     * Returns the property value associated with the given key, or {@code null}
     * if the key cannot be resolved.
     * @param key the property name to resolve
     * @see #getProp(String, String)
     * @see Environment#getProperty(String)
     */
    public static String getProp(String key) {
        return context.getEnvironment().getProperty(key);
    }

    /**
     * Returns the property value associated with the given key, or
     * {@code defaultValue} if the key cannot be resolved.
     * @param key the property name to resolve
     * @param defaultValue the default value to return if no value is found
     * @see #getProp(String)
     * @see Environment#getProperty(String, String)
     */
    public static String getProp(String key, String defaultValue) {
        return getEnv().getProperty(key, defaultValue);
    }

    /**
     * Returns the object mapper registered in the application context,
     * normally the default object mapper created by Spring Boot to serialize
     * from an to JSON format.
     *
     * Once you get the object you can use it to serialize object eg. in
     * a jshell console like this:
     *
     * <pre>
     * jshell> var objMapper = ctx.App.getObjectMapper()
     * objMapper ==> ObjectMapper
     *
     * jshell> objMapper.writeValueAsString(person)
     * {"name":"John","lastName":"Doe","age":null}
     *
     * jshell>
     * </pre>
     *
     * @see #pjson(Object)
     * @see #ppjson(Object)
     * @throws NoSuchBeanDefinitionException if there is no object
     *         mapper defined
     */
    public static ObjectMapper getObjectMapper() {
        return getBean(ObjectMapper.class);
    }

    // Get an object mapper used to print
    // in JSON format in the standard output that
    // is a copy of the one returned by getObjectMapper()
    // but suitable to write the standard output
    private static ObjectMapper getStdoutMapper() {
        if (mapper == null) {
            mapper = getObjectMapper().copy().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        }
        return mapper;
    }

    /**
     * Prints the object in JSON format and then terminate the line,
     * using the {@link ObjectMapper} registered in the Spring context
     *
     * <pre>
     *    ctx.App.pjson(personObj)
     *    // Prints the following =>
     *    {"name":"Jhon","lastName":"Due","age":null}
     *
     * </pre>
     *
     * @param obj the Object to be printed.
     * @see #ppjson(Object)
     * @see #getObjectMapper()
     */
    public static void pjson(Object obj) {
        try {
            getStdoutMapper().writeValue(System.out, obj);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error executing ctx.App.pjson(Object): " + e.getMessage());
        }
    }

    /**
     * Prints the object in pretty JSON format and then terminate the line,
     * using the {@link ObjectMapper} registered in the Spring context
     *
     * <pre>
     *    ctx.App.ppjson(personObj)
     *    // Prints the following =>
     *    {
     *      "name" : "Jhon",
     *      "lastName" : "Due",
     *      "age" : null
     *    }
     *
     * </pre>
     *
     * @param obj the Object to be printed.
     * @see #ppjson(Object)
     * @see #getObjectMapper()
     */
    public static void ppjson(Object obj) {
        try {
            getStdoutMapper().writerWithDefaultPrettyPrinter().writeValue(System.out, obj);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error executing ctx.App.ppjson(Object): " + e.getMessage());
        }
    }
}
