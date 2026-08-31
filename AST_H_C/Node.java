package AST_H_C;

public abstract class Node {
    protected String Name;
    protected int numberOfLine;

    public Node(String Name, int numberOfLine) {
        this.Name = Name;
        this.numberOfLine = numberOfLine;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getNumberOfLine() {
        return numberOfLine;
    }

    public void setNumberOfLine(int numberOfLine) {
        this.numberOfLine = numberOfLine;
    }

    @Override
    public String toString() {
        return "Node{" +
                "\nName='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
