package expression.parser;

import expression.*;
import expression.exceptions.UnsupportedCharacterError;

public class ExpressionParser implements TripleParser {
    public TripleExpression parse(String expression) {
        return new ExprParser(new StringSource(expression)).parseExpression();
    }

    private static final class ExprParser extends BaseParser {

        private ExprParser(CharSource source) {
            super(source);
        }

        private AllExpressions parseExpression() {
            skipWhitespace();
            AllExpressions argument = parseTerm();
            while (true) {
                skipWhitespace();
                if (test('+')) {
                    take();
                    argument = parseBinaryOperations("+", argument, parseTerm());
                } else if (test('-')) {
                    take();
                    argument = parseBinaryOperations("-", argument, parseTerm());
                } else {
                    return argument;
                }
            }
        }

        private AllExpressions parseTerm() {
            skipWhitespace();
            AllExpressions argument = parsePowLog();
            while (true) {
                skipWhitespace();
                if (test('*')) {
                    take();
                    argument = parseBinaryOperations("*", argument, parsePowLog());
                } else if (test('/')) {
                    take();
                    argument = parseBinaryOperations("/", argument, parsePowLog());
                } else {
                    return argument;
                }
            }
        }

        private AllExpressions parsePowLog() {
            skipWhitespace();
            AllExpressions argument = parseSquare();
            while (true) {
                skipWhitespace();
                if (test('*')) {
                    take();
                    if (test('*')) {
                        take();
                        argument = parseBinaryOperations("**", argument, parseSquare());
                    } else {
                        back();
                        return argument;
                    }
                } else if (test('/')) {
                    take();
                    if (test('/')) {
                        take();
                        argument = parseBinaryOperations("//", argument, parseSquare());
                    } else {
                        back();
                        return argument;
                    }
                } else {
                    return argument;
                }
            }
        }

        private AllExpressions parseSquare() {
            skipWhitespace();
            AllExpressions argument = parseFactor();
            skipWhitespace();
            while (test('²')) {
                take();
                argument = new Square(argument);
            }
            skipWhitespace();
            return argument;
        }

        private AllExpressions parseFactor() {
            skipWhitespace();
            if (take('(')) {
                skipWhitespace();
                AllExpressions argument = parseExpression();
                skipWhitespace();
                expect();
                return argument;
            } else if (between('a', 'z') || between('A', 'Z')) {
                StringBuilder name = new StringBuilder();
                while (between('a', 'z') || between('A', 'Z')) {
                    name.append(take());
                }
                return new Variable(name.toString());
            } else if (take('-')) {
                if (between('0', '9')) {
                    return new Const(toConstWithNegative(true));
                }
                AllExpressions result = parseFactor();
                while (test('²')) {
                    take();
                    result = new Square(result);
                }
                skipWhitespace();
                return new Negative(result);
            } else {
                return new Const(toConstWithNegative(false));
            }
        }

        private int toConstWithNegative(boolean negative) {
            StringBuilder number = new StringBuilder();
            if (negative) {
                number.append('-');
            }
            while (between('0', '9')) {
                number.append(take());
            }
            return Integer.parseInt(number.toString());
        }


        private AllExpressions parseBinaryOperations(String operator, AllExpressions argument1, AllExpressions argument2) {
            return switch (operator) {
                case "+" -> new Add(argument1, argument2);
                case "-" -> new Subtract(argument1, argument2);
                case "*" -> new Multiply(argument1, argument2);
                case "/" -> new Divide(argument1, argument2);
                case "//" -> new Log(argument1, argument2);
                case "**" -> new Pow(argument1, argument2);
                default -> throw new IllegalStateException("Unexpected value: " + operator);
            };
        }
    }
}