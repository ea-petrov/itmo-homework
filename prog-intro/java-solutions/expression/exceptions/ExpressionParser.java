package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

public class ExpressionParser implements TripleParser {
    public TripleExpression parse(String expression) {
        return new ExprParser(new StringSource(expression)).checkBrackets();
    }

    private static final class ExprParser extends BaseParser {
        private int operationsCount = 0;
        private int openingBrackets = 0;
        private int closingBrackets = 0;

        private ExprParser(CharSource source) {
            super(source);
        }

        private AllExpressions checkBrackets() {
            AllExpressions a = parseGcdLcm();
            if (eof() && openingBrackets == closingBrackets) {
                return a;
            } else if (take(')')) {
                do {
                    closingBrackets++;
                } while (take(')'));
                if (openingBrackets == closingBrackets) {
                    return a;
                } else {
                    throw new BadBracketsError("no opening brackets");
                }
            }
            throw new UnsupportedCharacterError(take() + " -is unsupported");
        }


        private AllExpressions parseGcdLcm() {
            AllExpressions a = parseExpression();
            while (true) {
                if (test('g')) {
                    back();
                    if (between('0', '9')) {
                        throw new InvalidStructure("haven't space in gcd");
                    }
                    take();
                    take();
                    if (test('c')) {
                        take();
                        if (test('d')) {
                            take();
                            operationsCount++;
                            if (test('-') || test('(') || Character.isWhitespace(take())){
                                skipWhitespace();
                                a = parseBinaryOperations("gcd", a, parseExpression());
                                skipWhitespace();
                            } else {
                                throw new InvalidStructure("glg");
                            }
                        } else {
                            throw new UnsupportedOperationError(" gc - is not operation");
                        }
                    } else {
                        throw new UnsupportedOperationError(" g - is not operation");
                    }
                } else if (test('l')) {
                    back();
                    if (between('0', '9')) {
                        throw new InvalidStructure("haven't space in lcm");
                    }
                    take();
                    take();
                    if (test('c')) {
                        take();
                        if (test('m')) {
                            take();
                            operationsCount++;
                            if (test('-') || test('(') || Character.isWhitespace(take())){
                                skipWhitespace();
                                a = parseBinaryOperations("lcm", a, parseExpression());
                                skipWhitespace();
                            }else {
                                throw new InvalidStructure("ffff");
                            }
                        } else {
                            throw new UnsupportedOperationError(" lc - is not operation");
                        }
                    } else {
                        throw new UnsupportedOperationError(" l - is not operation");
                    }
                } else {
                    skipWhitespace();
                    return a;
                }
            }
        }


        private AllExpressions parseExpression() {
            skipWhitespace();
            AllExpressions argument = parseTerm();
            while (true) {
                skipWhitespace();
                if (test('+')) {
                    take();
                    operationsCount++;
                    argument = parseBinaryOperations("+", argument, parseTerm());
                } else if (test('-')) {
                    take();
                    operationsCount++;
                    argument = parseBinaryOperations("-", argument, parseTerm());
                } else {
                    return argument;
                }
            }
        }

        private AllExpressions parseTerm() {
            skipWhitespace();
            AllExpressions argument = parseSquare();
            while (true) {
                skipWhitespace();
                if (test('*')) {
                    take();
                    operationsCount++;
                    argument = parseBinaryOperations("*", argument, parseSquare());
                } else if (test('/')) {
                    take();
                    operationsCount++;
                    argument = parseBinaryOperations("/", argument, parseSquare());
                } else {
                    return argument;
                }
            }
        }

        private AllExpressions parseSquare() {
            skipWhitespace();
            AllExpressions argument = parseTetra();
            skipWhitespace();
            while (test('²')) {
                take();
                argument = new CheckedSquare(argument);
            }
            skipWhitespace();
            return argument;
        }

        private AllExpressions parseFactor() {
            skipWhitespace();
            if (take('²')) {
                return new Tetra(parseFactor());
            }
            if (take('(')) {
                operationsCount = 0;
                openingBrackets++;
                skipWhitespace();
                AllExpressions argument = parseGcdLcm();
                skipWhitespace();
                if (test(')')) {
                    take();
                    closingBrackets++;
                } else if (eof()) {
                    throw new BadBracketsError("no closing brackets");
                }
                return argument;
            } else if (between('a', 'z') || between('A', 'Z')) {
                StringBuilder name = new StringBuilder();
                while (between('a', 'z') || between('A', 'Z')) {
                    name.append(take());
                }
                if (String.valueOf(name).contains("x") || (String.valueOf(name).contains("y") || String.valueOf(name).contains("z"))) {
                    return new Variable(name.toString());
                } else if (name.toString().equals("gcd") || name.toString().equals("lcm")) {
                    if (name.toString().equals("gcd")) {
                        throw new MissingArgException("no arguments for gcd");
                    } else {
                        throw new MissingArgException("no arguments for lcm");
                    }
                }
                throw new VariableNameException("bad name for variable");
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
                return new CheckedNegate(result);
            } else if (between('0', '9')) {
                return new Const(toConstWithNegative(false));
            } else if ((eof() || take('*') || take('/') ||
                    take('+') || take('-') || take(')'))) {
                if (operationsCount == 0 && eof()) {
                    throw new MissingArgException("Only one symbol");
                }
                if (operationsCount == 0 && take(')')) {
                    closingBrackets++;
                    throw new BadBracketsError("Only brackets");
                }

                switch (operationsCount % 3) {
                    case 0 -> throw new MissingArgException("no first argument");
                    case 1 -> throw new MissingArgException("no middle argument");
                    case 2 -> throw new MissingArgException("no last argument");
                }
            }
            throw new UnsupportedCharacterError(take() + " -is unsupported");
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

        private AllExpressions parseTetra() {
            skipWhitespace();
            AllExpressions argument = parseFactor();
            skipWhitespace();
            while (test('²')) {
                take();
                argument = new Tetra(argument);
            }
            skipWhitespace();
            return argument;
        }


        private AllExpressions parseBinaryOperations(String operator, AllExpressions argument1, AllExpressions argument2) {
            return switch (operator) {
                case "+" -> new CheckedAdd(argument1, argument2);
                case "-" -> new CheckedSubtract(argument1, argument2);
                case "*" -> new CheckedMultiply(argument1, argument2);
                case "/" -> new CheckedDivide(argument1, argument2);
                case "gcd" -> new GCD(argument1, argument2);
                case "lcm" -> new LCM(argument1, argument2);
                default -> throw new UnsupportedOperationError("This operation not in list of operations");
            };
        }
    }
}