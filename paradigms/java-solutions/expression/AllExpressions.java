package expression;

public interface AllExpressions extends Expression, TripleExpression, LongMapExpression {
    void toMiniStringBuilder(StringBuilder sb);

    void toStringStringBuilder(StringBuilder sb);
}

