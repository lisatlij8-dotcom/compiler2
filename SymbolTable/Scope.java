package SymbolTable;

import java.util.*;

import java.util.HashMap;
import java.util.Map;



//class Scope{
//    private Map<String,String> identi;
//    public Scope()
//    {
//        identi=new HashMap<>();
//    }
//    public boolean contains (String name){
//        return identi.containsKey(name);
//    }
//    public void add (String name ,String kind){
//        identi.put(name,kind);
//    }
//    public String get(String nn)
//    {
//        return identi.get(nn);
//    }
//    public Map<String,String>getIdentis()
//    {
//        return identi;
//    }
//}

public class Scope {
    private Map<String, Symbol> symbols = new LinkedHashMap<>();
    private List<Scope> childScopes = new ArrayList<>();
    public Scope parent;
    public String scopeName;
    private String scopeType;

    public static List<Symbol> report = new ArrayList<>();

    public Scope(Scope parent, String scopeName) {
        this(parent, scopeName, scopeName);
    }

    public Scope(Scope parent, String scopeName, String scopeType) {
        this.parent = parent;
        this.scopeName = scopeName;
        this.scopeType = scopeType;
        if (parent != null) {
            parent.addChildScope(this);
        }
    }

    public Symbol define(String name, String type, int line) {

        Symbol existing = lookupLocal(name);
        if (existing != null) {
            return existing;
        }

        Symbol s = new Symbol(name, type, line, this.scopeName);
        symbols.put(name, s);
        report.add(s);
        return s;
    }

    public Symbol lookupLocal(String name) {
        return symbols.get(name);
    }

    public Symbol lookup(String name) {
        Symbol symbol = lookupLocal(name);
        if (symbol != null) {
            return symbol;
        }
        return parent != null ? parent.lookup(name) : null;
    }

    public boolean isDefinedLocally(String name) {
        return lookupLocal(name) != null;
    }

    public Scope getParent() {
        return parent;
    }

    public String getScopeName() {
        return scopeName;
    }

    public String getScopeType() {
        return scopeType;
    }

    public Map<String, Symbol> getSymbols() {
        return Collections.unmodifiableMap(symbols);
    }

    public List<Scope> getChildScopes() {
        return Collections.unmodifiableList(childScopes);
    }

    private void addChildScope(Scope scope) {
        childScopes.add(scope);
    }

    public static void clearReport() {
        report.clear();
    }

    public static void printFinalReport() {

        System.out.format("%-15s | %-12s | %-5s | %-20s\n", "Name", "Type", "Line", "Scope");
        System.out.println("-".repeat(60));
        for (Symbol s : report) System.out.println(s);

    }
}




