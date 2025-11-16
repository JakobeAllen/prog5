package Parse;

import java.io.IOException;
import java_cup.runtime.Symbol;

public interface Lexer {
   Symbol nextToken() throws IOException;
}
