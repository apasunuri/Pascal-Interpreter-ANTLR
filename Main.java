import java.lang.Exception;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("No File Name Provided");
        }

        System.out.println("parsing: " + args[0]);
        
        project2Lexer lexer = new project2Lexer(new ANTLRFileStream(args[0]));
        project2Parser parser = new project2Parser(new CommonTokenStream(lexer));
        ParseTree tree = parser.start();
        Visitor visitor = new Visitor();
        visitor.visit(tree);
    }
}