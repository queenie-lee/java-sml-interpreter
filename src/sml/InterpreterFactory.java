package sml;

import java.lang.reflect.Constructor;
import java.util.Properties;

public final class InterpreterFactory {
    private static final InterpreterFactory instance = new InterpreterFactory();
    private Translator translator;
    private InstructionFactory instructionFactory;

    public static InterpreterFactory getInstance() { return instance; }

    public Translator getTranslator() {
        this.translator = new Translator();
        return translator; }

    public InstructionFactory getInstructionFactory() { return instructionFactory; }

    private InterpreterFactory() {
        Properties props = new Properties();

        try {
            try (var fis = InterpreterFactory.class.getResourceAsStream("/beans.properties")) {
                props.load(fis);
            }
            String translatorClass = props.getProperty("translator.class");
            translator = (Translator) newInstanceOf(translatorClass);

            String instructionClass = props.getProperty("instruction-factory.class");
            instructionFactory = newTypeSafeInstanceOf(instructionClass, InstructionFactory.class);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Object newInstanceOf(String className) throws ReflectiveOperationException {

        Class<?> classObject = Class.forName(className);
        Constructor<?> constructor = classObject.getDeclaredConstructor();
        return constructor.newInstance();
    }

    private <T> T newTypeSafeInstanceOf(String className, Class<T> klass) throws ReflectiveOperationException {

        Class<? extends T> classObject = Class.forName(className).asSubclass(klass);
        Constructor<? extends T> constructor = classObject.getDeclaredConstructor();
        return constructor.newInstance();
    }
}
