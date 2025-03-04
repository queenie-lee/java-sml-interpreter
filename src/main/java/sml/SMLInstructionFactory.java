package sml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * <p>Creates the SML program's instructions</p>
 * The general form of an SML instruction is: [label:] opcode parameter-list
 * @author Queenie Lee
 */
@Component("instruction-factory")
public class SMLInstructionFactory implements InstructionFactory {

    /**
     * The public variable used in the instruction classes, representing the actual instruction name (operation code)
     */
    private final String OP_CODE = "OP_CODE";

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = Map.of(
            boolean.class, Boolean.class,
            int.class, Integer.class,
            void.class, Void.class);

    private final List<Class<?>> instructionClasses;

    /**
     * The list of instruction classes is defined in the resources file (beans.xml)
     * @param instructionClasses list of instruction classes used in the SML program
     */
    public SMLInstructionFactory(List<Class<?>> instructionClasses) {
        this.instructionClasses = instructionClasses;
    }
    /**
     * Returns a program instruction
     * @param label optional label (can be null)
     * @param programInstruction list containing data from a single instruction
     * @return a program instruction
     * @throws BadProgramError should not occur these should have been checked prior to calling this method
     */
    @Override
    public Instruction createInstruction(Label label, List<String> programInstruction) throws BadProgramError {
        Field[] fields;
        String opcode = programInstruction.removeFirst();

        if (opcode.isEmpty())
            return null;

        for (Class<?> instruction : instructionClasses) {
            fields = instruction.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals(OP_CODE)) {
                    String OP_CODEval;
                    try {
                        OP_CODEval = String.valueOf(field.get(null));
                    } catch (IllegalArgumentException | IllegalAccessException e)
                    {
                        // Should not happen, as all Instruction implementations must have this field
                        throw new RuntimeException(e);
                    }
                    if (OP_CODEval.equals(opcode)){
                        return buildInstruction(label, programInstruction, instruction);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns an instruction subclass
     * @param label optional label (can be null)
     * @param parameterList list of parameters
     * @param className instruction class to be created
     * @return an instruction subclass
     * @throws BadProgramError if no constructors match the number of arguments, the arguments do not match the
     * required parameter type, or null arguments are passed into non-null parameters
     */
    private static Instruction buildInstruction(Label label, List<String> parameterList, Class<?> className)
            throws BadProgramError {

        Constructor<?> constructor = null;
        int numConstructorParams = parameterList.size() + 1;
        for (Constructor<?> candidateConstructor : className.getConstructors()) {
            if (candidateConstructor.getParameterCount() == numConstructorParams) {
                constructor = candidateConstructor;
            }
        }
        if (constructor == null) {
            throw new BadProgramError("There is no constructor that matches the number of arguments you are " +
                    "trying to include in this program line. ");
        }
        try {
            Object[] parameters = new Object[numConstructorParams];
            parameters[0] = label;
            Class<?>[] paramTypes = constructor.getParameterTypes();
            for (int i = 1; i < numConstructorParams; i++) {
                Class<?> paramObjectType = wrap(paramTypes[i]);
                Constructor<?> stringToParamFunction = paramObjectType.getConstructor(String.class);
                parameters[i] = stringToParamFunction.newInstance(parameterList.get(i - 1));
            }
            return (Instruction) constructor.newInstance(parameters);
        }
        catch (Exception e) {
            throw new BadProgramError("Unable to build the Instruction " + className.getName() + ".\n" +
                    "It could be the arguments do not match the parameter type required," +
                    "or null arguments are being passed into a non-null parameter.");
        }
    }

    private static Class<?> wrap(Class<?> theClass) {
        return PRIMITIVE_WRAPPERS.getOrDefault(theClass, theClass);
    }
}
