package splat.executor;

import splat.parser.elements.Type;

public abstract class Value {

	// TODO: Implement me!
    private Object value;

    private Type type;

    public Value(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
