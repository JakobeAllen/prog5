package Parse;

public class Main {

  public static void main(String argv[]) throws java.io.IOException {
    for (int i = 0; i < argv.length; ++i) {
      String filename = argv[i];
      if (argv.length > 1)
        System.out.println("***Processing: " + filename);
      Parse parse = new Parse(filename);
      
      if (!parse.errorMsg.anyErrors) {
        System.out.println("Parse successful!");
        System.out.println("\n=== Abstract Syntax Tree ===");
        System.out.println(parse.absyn);
      } else {
        System.out.println("Parse failed with errors.");
      }
    }
  }

}
