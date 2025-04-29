package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.util.Stack;

public class ExpressionParser implements TripleParser {
    public TripleExpression parse(String expression) throws ParserErrors {
        return new ExprParser(new StringSource(expression)).Balance();
    }

    private static final class ExprParser extends BaseParser {
        private int operationsCount = 0;
        private final StringBuilder brackets = new StringBuilder();
        private int openingBrackets = 0;
        private int closingBrackets = 0;

        private ExprParser(CharSource source) {
            super(source);
        }

        private AllExpressions Balance() throws ParserErrors {
            skipWhitespace();
            AllExpressions a = parseGeom();
            skipWhitespace();
            if (test(')') || test(']') || test('}')){
                closingBrackets++;
                brackets.append(take());
            }
            if (eof()) {
                if (isPSP(brackets.toString())) {
                    return a;
                } else {
                    if (openingBrackets < closingBrackets) {
                        throw new BadBracketsError("need opening bracket for closing bracket in " + " " + error("position"));
                    }
                    throw new BadBracketsError("not balanced brackets(invalid sequence)" + " " + brackets);
                }
            }
            throw new UnsupportedCharacterError(error("position, is unsupported - ") + " " + take());
        }

        private AllExpressions parseGeom() throws ParserErrors {
            AllExpressions a = parseExpression();
            String name1 = "rea";
            String name2 = "erimeter";
            while (true) {
                if (test('a')) {
                    back();
                    if (between('0', '9')) {
                        throw new InvalidStructure(error("haven't space before area operation"));
                    }
                    take();
                    take();
                    int i = 0;
                    while (i < name1.length()) {
                        if (name1.charAt(i) != take()) {
                            throw new InvalidStructure(error("it is not area operation"));
                        }
                        i++;
                    }
                    if (!test('-') && !test('(') && !Character.isWhitespace(take())) {
                        throw new InvalidStructure(error("haven't space after area operation"));
                    }
                    a = parseBinaryOperations("◣", a, parseExpression());
                } else if (test('p')) {
                    back();
                    if (between('0', '9')) {
                        throw new InvalidStructure(error("haven't space before perimeter operation"));
                    }
                    take();
                    take();
                    int i = 0;
                    while (i < name2.length()) {
                        if (name2.charAt(i) != take()) {
                            throw new InvalidStructure(error("it is not perimeter operation"));
                        }
                        i++;
                    }
                    if (!test('-') && !test('(') && !Character.isWhitespace(take())) {
                        throw new InvalidStructure(error("haven't space after perimeter operation"));
                    }
                    a = parseBinaryOperations("▯", a, parseExpression());
                } else {
                    skipWhitespace();
                    return a;
                }
            }
        }

        private AllExpressions parseExpression() throws ParserErrors {
            skipWhitespace();
            AllExpressions argument = parseTerm();
            while (true) {
                skipWhitespace();
                if (take('+')) {
                    operationsCount++;
                    argument = parseBinaryOperations("+", argument, parseTerm());
                } else if (take('-')) {
                    operationsCount++;
                    argument = parseBinaryOperations("-", argument, parseTerm());
                } else {
                    return argument;
                }
            }
        }

        private boolean isPSP(String s) {
            Stack<Character> stack = new Stack<>();
            for (char c : s.toCharArray()) {
                switch (c) {
                    case '(':
                        stack.push(')');
                        break;
                    case '[':
                        stack.push(']');
                        break;
                    case '{':
                        stack.push('}');
                        break;
                    default:
                        if (stack.isEmpty() || stack.pop() != c) {
                            return false;
                        }
                }
            }
            return stack.isEmpty();
        }

        private AllExpressions parseTerm() throws ParserErrors {
            skipWhitespace();
            AllExpressions argument = parseFactor();
            while (true) {
                skipWhitespace();
                if (take('*')) {
                    operationsCount++;
                    argument = parseBinaryOperations("*", argument, parseFactor());
                } else if (take('/')) {
                    operationsCount++;
                    argument = parseBinaryOperations("/", argument, parseFactor());
                } else {
                    return argument;
                }
            }
        }

        private AllExpressions parseFactor() throws ParserErrors {
            skipWhitespace();
            if (test('(') || test('[') || test('{')) {
                brackets.append(take());
                openingBrackets++;
                operationsCount = 0;
                skipWhitespace();
                AllExpressions argument = parseGeom();
                skipWhitespace();
                if (test(')') || test(']') || test('}')) {
                    brackets.append(take());
                    closingBrackets++;
                } else if (eof()) {
                    throw new BadBracketsError("need closing brackets in" + " " + error("position"));
                }
                return argument;
            } else if (between('a', 'z') || between('A', 'Z')) {
                StringBuilder name = new StringBuilder();
                while (between('a', 'z') || between('A', 'Z')) {
                    name.append(take());
                }
                if (String.valueOf(name).contains("x") || (String.valueOf(name).contains("y") || String.valueOf(name).contains("z"))) {
                    return new Variable(name.toString());
                } else if (name.toString().equals("area") || name.toString().equals("perimeter")) {
                    if (name.toString().equals("area")) {
                        throw new MissingArgException(error("no arguments for area"));
                    } else {
                        throw new MissingArgException(error("no arguments for perimeter"));
                    }
                }
                throw new VariableNameException(error("bad name for variable"));
            } else if (take('-')) {
                if (between('0', '9')) {
                    return new Const(toConstWithNegative(true));
                }
                AllExpressions result = parseFactor();
                return new CheckedNegate(result);
            } else if (between('0', '9')) {
                return new Const(toConstWithNegative(false));
            } else if ((eof() || take('*') || take('/') ||
                    take('+') || take('-') || take(')'))) {
                if (operationsCount == 0 && eof() && openingBrackets == 0) {
                    throw new MissingArgException("Only one symbol");
                }
                skipWhitespace();
                if (operationsCount == 0 && take(')')) {
                    throw new BadBracketsError("Only brackets");
                }
                switch (operationsCount % 3) {
                    case 0 -> throw new MissingArgException(error("no first argument"));
                    case 1 -> throw new MissingArgException(error("no middle argument"));
                    case 2 -> throw new MissingArgException(error("no last argument"));
                }
            }
            throw new UnsupportedCharacterError(error("position, is unsupported - ") + " " + take());
        }


        private int toConstWithNegative(boolean negative) {
            StringBuilder number = new StringBuilder();
            if (negative) {
                number.append('-');
            }
            while (between('0', '9')) {
                number.append(take());
            }
            final int t;
            try {
                t = Integer.parseInt(number.toString());
            } catch (NumberFormatException e) {
                throw new OverflowException("overflow in Const");
            }
            return t;
        }

        private AllExpressions parseBinaryOperations(String operator, AllExpressions argument1, AllExpressions argument2) throws ParserErrors {
            return switch (operator) {
                case "+" -> new CheckedAdd(argument1, argument2);
                case "-" -> new CheckedSubtract(argument1, argument2);
                case "*" -> new CheckedMultiply(argument1, argument2);
                case "/" -> new CheckedDivide(argument1, argument2);
                case "◣" -> new CheckedTriangle(argument1, argument2);
                case "▯" -> new CheckedRectangle(argument1, argument2);
                default -> throw new UnsupportedOperationError(error("This operation not in list of operations"));
            };
        }
    }
}