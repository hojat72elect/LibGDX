
package com.talosvfx.talos.runtime.vfx;

import com.talosvfx.talos.runtime.vfx.values.NumericalValue;

public abstract class Expression {

    static public final Expression sum = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.sum(b, out);
        }
    };
    static public final Expression substract = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.sub(b, out);
        }
    };
    static public final Expression multiply = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.mul(b, out);
        }
    };
    static public final Expression cos = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.cos(out);
            out.mul(b, out);
        }
    };
    static public final Expression sin = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.sin(out);
            out.mul(b, out);
        }
    };
    static public final Expression pow = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.pow(b, out);
        }
    };
    static public final Expression abs = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.abs(out);
        }
    };
    static public final Expression divide = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.div(b, out);
        }
    };
    static public final Expression mod = new Expression() {
        @Override
        public void apply(NumericalValue a, NumericalValue b, NumericalValue out) {
            a.mod(b, out);
        }
    };

    abstract public void apply(NumericalValue a, NumericalValue b, NumericalValue out);
}
