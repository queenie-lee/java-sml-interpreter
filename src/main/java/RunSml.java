import sml.*;


import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;


public class RunSml {
    /**
     * Initialises the system and executes the program.
     *
     * @param args name of the file containing the program text.
     */
    public static void main(String... args) {
        if (args.length != 1) {
            System.err.println("Incorrect number of arguments - RunSml <file> - required");
            System.exit(-1);
        }

        try {
            BeanFactory factory = getBeanFactory();

            Translator t = (Translator) factory.getBean("translator");
            InstructionFactory instructionFactory = (InstructionFactory) factory.getBean("instruction-factory");
            t.setInstructionFactory(instructionFactory);

            Collection<Method> instructions = t.readAndTranslate(args[0]);
            Machine m = new Machine();
            m.setProgram(instructions);

            System.out.println("Beginning program execution.");
            m.execute();
            System.out.println("Ending program execution.");
        }
        catch (BadProgramError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("Error reading the program from " + args[0]);
        }
    }

    private static BeanFactory getBeanFactory() throws IOException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        PropertiesBeanDefinitionReader rdr = new PropertiesBeanDefinitionReader(factory);
        Properties props = new Properties();
        try (var fis = RunSml.class.getResourceAsStream("/beans")) {
            props.load(fis);
        }
        rdr.registerBeanDefinitions(props);

        return factory;
    }
}
