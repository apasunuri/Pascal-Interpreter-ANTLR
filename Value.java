public class Value {
    public static Value VOID = new Value(new Object());
    Object value;
    public Value(Object value) {
        this.value = value;
    }
    public Boolean toBoolean() {
        return (Boolean) value;
    }
    public Double toDouble() {
        return (Double) value;
    }
    public String toString() {
        return String.valueOf(value);
    }
}