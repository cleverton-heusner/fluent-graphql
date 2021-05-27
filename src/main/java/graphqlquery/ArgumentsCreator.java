package graphqlquery;

import java.util.List;
import java.util.stream.Collectors;

public class ArgumentsCreator
{
    private final String LAST_COMMA_PATTERN = ",(?!.*,)";
    private final String LEFT_PARENTHESIS = "(";
    private final String RIGHT_PARENTHESIS = ")";

    public String createArguments(final List<Argument> arguments)
    {
        return LEFT_PARENTHESIS.concat(arguments.stream()
                                                .flatMap(argument -> argument.entrySet().stream())
                                                .map(arg -> arg.getKey() + ": \\\"" + arg.getValue() + "\\\", ")
                                                .collect(Collectors.joining())
                                                .split(LAST_COMMA_PATTERN)[0])
                               .concat(RIGHT_PARENTHESIS);
    }
}
