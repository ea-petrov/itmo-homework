"use strict";

const variable = (name) => {
    return (x, y, z, t) => {
        switch (name) {
            case "x": return x;
            case "y": return y;
            case "z": return z;
            case "t": return t;
            default: throw new Error("valid variable names: x y z t " + name + " - is not valid");
        }
    };
};
const binOp = (operation) => (first, second) => (...args) => operation(first(...args), second(...args));
const unaryOp = (operation) => (value) => (...args) => operation(value(...args));
const cnst = (value) => () => Number(value);
const tau = cnst(2 * Math.PI);
const phi = cnst((1 + Math.sqrt(5)) / 2);
const add = binOp((a, b) => a + b);
const subtract = binOp((a, b) => a - b);
const multiply = binOp((a, b) => a * b);
const divide = binOp((a, b) => a / b);
const negate = unaryOp((a) => -a);
const sinh = unaryOp((a) => Math.sinh(a));
const cosh = unaryOp((a) => Math.cosh(a));
