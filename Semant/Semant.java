package Semant;

import Absyn.*;
import Types.*;
import Symbol.Symbol;
import Symbol.Table;
import ErrorMsg.ErrorMsg;
import Frame.Access;
import Mips.MipsFrame;
import Temp.Label;
import Util.BoolList;
import FindEscape.FindEscape;

public class Semant {
    private ErrorMsg errorMsg;
    private Table venv = new Table();
    private Table tenv = new Table();
    
    public Semant(ErrorMsg err) {
        errorMsg = err;
    }
    
    public void transProg(Program prog) {
        FindEscape escape = new FindEscape();
        escape.findEscape(prog);
        
        initTypeEnv();
        transDecList(prog.decs, null);
    }
    
    private void initTypeEnv() {
    }
    
    private void transDecList(DecList decs, MipsFrame frame) {
        // First pass: register struct and union types
        for (DecList dl = decs; dl != null; dl = dl.tail) {
            if (dl.head instanceof StructDec) {
                registerStructDec((StructDec)dl.head);
            } else if (dl.head instanceof UnionDec) {
                registerUnionDec((UnionDec)dl.head);
            }
        }
        
        // Second pass: collect all function declarations
        for (DecList dl = decs; dl != null; dl = dl.tail) {
            if (dl.head instanceof FunctionDec) {
                collectFuncDec((FunctionDec)dl.head);
            }
        }
        
        // Third pass: process all declarations (including function bodies)
        for (; decs != null; decs = decs.tail) {
            transDec(decs.head, frame);
        }
    }
    
    private void collectFuncDec(FunctionDec dec) {
        // Create function entry without processing body
        Type returnType = transTypeSpec(dec.returnType);
        
        Type[] paramTypes = null;
        BoolList formals = null;
        BoolList formalsLast = null;
        
        int paramCount = 0;
        for (ParamList pl = dec.params; pl != null; pl = pl.tail) {
            paramCount++;
        }
        
        if (paramCount > 0) {
            paramTypes = new Type[paramCount];
            int i = 0;
            for (ParamList pl = dec.params; pl != null; pl = pl.tail, i++) {
                paramTypes[i] = transTypeSpec(pl.head.type);
                
                BoolList bl = new BoolList(pl.head.escape, null);
                if (formals == null) {
                    formals = bl;
                    formalsLast = bl;
                } else {
                    formalsLast.tail = bl;
                    formalsLast = bl;
                }
            }
        }
        
        FUNCTION funcType = new FUNCTION(returnType, paramTypes);
        Label label = new Label(dec.name);
        MipsFrame frame = new MipsFrame(label, formals);
        
        venv.put(dec.name, new FuncEntry(funcType, label, frame));
    }
    
    private void transDec(Dec dec, MipsFrame frame) {
        if (dec instanceof VarDec) {
            transDec((VarDec)dec, frame);
        } else if (dec instanceof FunctionDec) {
            transDec((FunctionDec)dec);
        } else if (dec instanceof StructDec) {
            transDec((StructDec)dec);
        } else if (dec instanceof UnionDec) {
            transDec((UnionDec)dec);
        }
    }
    
    private void transDec(VarDec dec, MipsFrame frame) {
        Type type = transTypeSpec(dec.type);
        
        if (dec.init != null) {
            if (dec.init instanceof InitList) {
                checkInitList((InitList)dec.init, type, frame);
            } else {
                ExpTy initTy = transExp(dec.init, frame);
                if (!initTy.ty.coerceTo(type)) {
                    error(dec.pos, "type mismatch in variable initialization");
                }
            }
        }
        
        Access access = null;
        if (frame != null) {
            int size = type.size();
            access = frame.allocLocal(dec.escape, size);
        }
        
        venv.put(dec.name, new VarEntry(type, access));
    }
    
    private void transDec(FunctionDec dec) {
        // Function already added to symbol table in collectFuncDec
        // Just retrieve it and process the body
        Object entry = venv.get(dec.name);
        if (entry == null || !(entry instanceof FuncEntry)) {
            error(dec.pos, "internal error: function not found");
            return;
        }
        
        FuncEntry funcEntry = (FuncEntry)entry;
        Type returnType = funcEntry.type.returnType;
        MipsFrame frame = funcEntry.frame;
        
        venv.beginScope();
        
        Access[] accesses = frame.formals();
        int i = 0;
        for (ParamList pl = dec.params; pl != null; pl = pl.tail, i++) {
            Type paramType = transTypeSpec(pl.head.type);
            venv.put(pl.head.name, new VarEntry(paramType, accesses[i]));
        }
        
        if (dec.body != null) {
            transStmt(dec.body, returnType, frame);
        }
        
        System.err.println(frame.toString());
        
        venv.endScope();
    }
    
    private void registerStructDec(StructDec dec) {
        // Register struct type in type environment (first pass)
        STRUCT structType = new STRUCT(dec.name);
        tenv.put(dec.name, structType);
    }
    
    private void registerUnionDec(UnionDec dec) {
        // Register union type in type environment (first pass)
        UNION unionType = new UNION(dec.name);
        tenv.put(dec.name, unionType);
    }
    
    private void transDec(StructDec dec) {
        // Already registered in first pass, nothing more to do
    }
    
    private void transDec(UnionDec dec) {
        // Already registered in first pass, nothing more to do
    }
    
    private Type transTypeSpec(TypeSpec spec) {
        if (spec == null) {
            return VOID.getInstance();
        }
        
        Type baseType = null;
        switch (spec.type) {
            case TypeSpec.INT:
                baseType = INT.getInstance();
                break;
            case TypeSpec.CHAR:
                baseType = CHAR.getInstance();
                break;
            case TypeSpec.VOID:
                baseType = VOID.getInstance();
                break;
            case TypeSpec.STRUCT:
                // Look up struct type by name
                if (spec.name != null) {
                    Object entry = tenv.get(spec.name);
                    if (entry instanceof STRUCT) {
                        baseType = (Type)entry;
                    } else if (entry instanceof UNION) {
                        error(spec.pos, "'" + spec.name + "' is a union, not a struct");
                        return INT.getInstance();
                    } else {
                        error(spec.pos, "undefined struct type: " + spec.name);
                        return INT.getInstance();
                    }
                } else {
                    error(spec.pos, "struct type without name");
                    return INT.getInstance();
                }
                break;
            case TypeSpec.UNION:
                // Look up union type by name
                if (spec.name != null) {
                    Object entry = tenv.get(spec.name);
                    if (entry instanceof UNION) {
                        baseType = (Type)entry;
                    } else if (entry instanceof STRUCT) {
                        error(spec.pos, "'" + spec.name + "' is a struct, not a union");
                        return INT.getInstance();
                    } else {
                        error(spec.pos, "undefined union type: " + spec.name);
                        return INT.getInstance();
                    }
                } else {
                    error(spec.pos, "union type without name");
                    return INT.getInstance();
                }
                break;
            default:
                error(spec.pos, "unknown type");
                return INT.getInstance();
        }
        
        for (int i = 0; i < spec.pointers; i++) {
            baseType = new POINTER(baseType);
        }
        
        if (spec.arrays != null) {
            for (ArrayDimList ad = spec.arrays; ad != null; ad = ad.tail) {
                int size = 0;
                if (ad.head.size instanceof IntExp) {
                    size = ((IntExp)ad.head.size).value;
                }
                baseType = new ARRAY(baseType, size);
            }
        }
        
        return baseType;
    }
    
    private void transStmt(Stmt stmt, Type returnType, MipsFrame frame) {
        if (stmt instanceof CompoundStmt) {
            CompoundStmt cs = (CompoundStmt)stmt;
            venv.beginScope();
            transDecList(cs.decs, frame);
            transStmtList(cs.stmts, returnType, frame);
            venv.endScope();
        } else if (stmt instanceof ExpStmt) {
            ExpStmt es = (ExpStmt)stmt;
            if (es.exp != null) {
                transExp(es.exp, frame);
            }
        } else if (stmt instanceof IfStmt) {
            IfStmt is = (IfStmt)stmt;
            if (is.test != null) {
                ExpTy testTy = transExp(is.test, frame);
            }
            if (is.thenclause != null) {
                transStmt(is.thenclause, returnType, frame);
            }
            if (is.elseclause != null) {
                transStmt(is.elseclause, returnType, frame);
            }
        } else if (stmt instanceof WhileStmt) {
            WhileStmt ws = (WhileStmt)stmt;
            if (ws.test != null) {
                transExp(ws.test, frame);
            }
            if (ws.body != null) {
                transStmt(ws.body, returnType, frame);
            }
        } else if (stmt instanceof ForStmt) {
            ForStmt fs = (ForStmt)stmt;
            venv.beginScope();
            if (fs.init != null) transExp(fs.init, frame);
            if (fs.test != null) transExp(fs.test, frame);
            if (fs.increment != null) transExp(fs.increment, frame);
            if (fs.body != null) transStmt(fs.body, returnType, frame);
            venv.endScope();
        } else if (stmt instanceof ReturnStmt) {
            ReturnStmt rs = (ReturnStmt)stmt;
            if (rs.exp != null) {
                ExpTy expTy = transExp(rs.exp, frame);
                if (!expTy.ty.coerceTo(returnType)) {
                    error(rs.pos, "return type mismatch");
                }
            } else {
                if (!(returnType instanceof VOID)) {
                    error(rs.pos, "missing return value");
                }
            }
        } else if (stmt instanceof BreakStmt) {
            // Break statement - no type checking needed
            // In a full implementation, would verify we're inside a loop
        } else if (stmt instanceof ContinueStmt) {
            // Continue statement - no type checking needed
            // In a full implementation, would verify we're inside a loop
        }
    }
    
    private void transStmtList(StmtList stmts, Type returnType, MipsFrame frame) {
        for (; stmts != null; stmts = stmts.tail) {
            if (stmts.head != null) {
                transStmt(stmts.head, returnType, frame);
            }
        }
    }
    
    private ExpTy transExp(Exp exp, MipsFrame frame) {
        if (exp instanceof IntExp) {
            return new ExpTy(null, INT.getInstance());
        } else if (exp instanceof CharExp) {
            return new ExpTy(null, CHAR.getInstance());
        } else if (exp instanceof StringExp) {
            return new ExpTy(null, new POINTER(CHAR.getInstance()));
        } else if (exp instanceof IdExp) {
            IdExp ie = (IdExp)exp;
            Object entry = venv.get(ie.name);
            if (entry == null) {
                error(ie.pos, "undefined variable: " + ie.name);
                return new ExpTy(null, INT.getInstance());
            }
            if (entry instanceof VarEntry) {
                return new ExpTy(null, ((VarEntry)entry).type);
            } else if (entry instanceof FuncEntry) {
                return new ExpTy(null, ((FuncEntry)entry).type);
            }
            error(ie.pos, "not a variable: " + ie.name);
            return new ExpTy(null, INT.getInstance());
        } else if (exp instanceof BinOp) {
            return transBinOp((BinOp)exp, frame);
        } else if (exp instanceof UnaryOp) {
            return transUnaryOp((UnaryOp)exp, frame);
        } else if (exp instanceof AssignExp) {
            return transAssignExp((AssignExp)exp, frame);
        } else if (exp instanceof CallExp) {
            return transCallExp((CallExp)exp, frame);
        } else if (exp instanceof ArrayExp) {
            return transArrayExp((ArrayExp)exp, frame);
        } else if (exp instanceof InitList) {
            return transInitList((InitList)exp, frame);
        }
        
        return new ExpTy(null, INT.getInstance());
    }
    
    private ExpTy transBinOp(BinOp exp, MipsFrame frame) {
        ExpTy leftTy = transExp(exp.left, frame);
        ExpTy rightTy = transExp(exp.right, frame);
        
        switch (exp.oper) {
            case BinOp.PLUS:
            case BinOp.MINUS:
            case BinOp.MUL:
            case BinOp.DIV:
            case BinOp.MOD:
                // Rule 6: Math operations only accept INT (not CHAR)
                if (!(leftTy.ty instanceof INT)) {
                    error(exp.pos, "arithmetic operator requires int type, got " + leftTy.ty);
                }
                if (!(rightTy.ty instanceof INT)) {
                    error(exp.pos, "arithmetic operator requires int type, got " + rightTy.ty);
                }
                return new ExpTy(null, INT.getInstance());
                
            case BinOp.LT:
            case BinOp.LE:
            case BinOp.GT:
            case BinOp.GE:
            case BinOp.EQ:
            case BinOp.NE:
                // Comparison operators also require INT (not CHAR)
                if (!(leftTy.ty instanceof INT)) {
                    error(exp.pos, "comparison operator requires int type, got " + leftTy.ty);
                }
                if (!(rightTy.ty instanceof INT)) {
                    error(exp.pos, "comparison operator requires int type, got " + rightTy.ty);
                }
                return new ExpTy(null, INT.getInstance());
                
            case BinOp.AND:
            case BinOp.OR:
                return new ExpTy(null, INT.getInstance());
                
            default:
                return new ExpTy(null, INT.getInstance());
        }
    }
    
    private ExpTy transUnaryOp(UnaryOp exp, MipsFrame frame) {
        ExpTy expTy = transExp(exp.exp, frame);
        
        switch (exp.oper) {
            case UnaryOp.NEG:
            case UnaryOp.NOT:
                return new ExpTy(null, expTy.ty);
                
            default:
                return new ExpTy(null, expTy.ty);
        }
    }
    
    private ExpTy transAssignExp(AssignExp exp, MipsFrame frame) {
        ExpTy lhsTy = transExp(exp.var, frame);
        ExpTy rhsTy = transExp(exp.exp, frame);
        
        if (!rhsTy.ty.coerceTo(lhsTy.ty)) {
            error(exp.pos, "type mismatch in assignment");
        }
        
        return new ExpTy(null, lhsTy.ty);
    }
    
    private ExpTy transCallExp(CallExp exp, MipsFrame frame) {
        Object entry = venv.get(exp.func);
        if (entry == null) {
            error(exp.pos, "undefined function: " + exp.func);
            return new ExpTy(null, INT.getInstance());
        }
        
        if (!(entry instanceof FuncEntry)) {
            error(exp.pos, "not a function: " + exp.func);
            return new ExpTy(null, INT.getInstance());
        }
        
        FuncEntry funcEntry = (FuncEntry)entry;
        FUNCTION funcType = funcEntry.type;
        
        int argCount = 0;
        for (ExpList el = exp.args; el != null; el = el.tail) {
            argCount++;
        }
        
        if (funcType.paramTypes != null && argCount != funcType.paramTypes.length) {
            error(exp.pos, "wrong number of arguments");
        }
        
        int i = 0;
        for (ExpList el = exp.args; el != null; el = el.tail, i++) {
            ExpTy argTy = transExp(el.head, frame);
            if (funcType.paramTypes != null && i < funcType.paramTypes.length) {
                if (!argTy.ty.coerceTo(funcType.paramTypes[i])) {
                    error(exp.pos, "argument type mismatch");
                }
            }
        }
        
        return new ExpTy(null, funcType.returnType);
    }
    
    private ExpTy transArrayExp(ArrayExp exp, MipsFrame frame) {
        ExpTy arrayTy = transExp(exp.array, frame);
        transExp(exp.index, frame);  // Type check index but don't use result
        
        if (arrayTy.ty instanceof ARRAY) {
            return new ExpTy(null, ((ARRAY)arrayTy.ty).element);
        } else if (arrayTy.ty instanceof POINTER) {
            return new ExpTy(null, ((POINTER)arrayTy.ty).target);
        }
        
        error(exp.pos, "subscript of non-array type");
        return new ExpTy(null, INT.getInstance());
    }
    
    private ExpTy transInitList(InitList exp, MipsFrame frame) {
        // For now, treat initializer list as having unknown type
        // The actual type checking happens in transDec when we know the target type
        
        // Type check all elements in the list
        for (ExpList el = exp.exps; el != null; el = el.tail) {
            if (el.head != null) {
                transExp(el.head, frame);
            }
        }
        
        // Return a placeholder type - actual type will be determined by context
        return new ExpTy(null, INT.getInstance());
    }
    
    private void checkInitList(InitList init, Type targetType, MipsFrame frame) {
        if (targetType instanceof ARRAY) {
            ARRAY arrayType = (ARRAY)targetType;
            
            // Count elements in the initializer list
            int count = 0;
            for (ExpList el = init.exps; el != null; el = el.tail) {
                count++;
            }
            
            // Check if count matches array size
            if (arrayType.size > 0 && count != arrayType.size) {
                error(init.pos, "initializer list size mismatch: expected " + 
                      arrayType.size + " elements, got " + count);
                return;
            }
            
            // Check each element
            for (ExpList el = init.exps; el != null; el = el.tail) {
                if (el.head instanceof InitList) {
                    // Nested initializer list
                    checkInitList((InitList)el.head, arrayType.element, frame);
                } else {
                    // Regular expression
                    ExpTy elemTy = transExp(el.head, frame);
                    if (!elemTy.ty.coerceTo(arrayType.element)) {
                        error(init.pos, "initializer element type mismatch");
                    }
                }
            }
        } else if (targetType instanceof STRUCT) {
            // For structs, just accept the initializer list for now
            // Full implementation would check member types
            for (ExpList el = init.exps; el != null; el = el.tail) {
                if (el.head != null) {
                    transExp(el.head, frame);
                }
            }
        } else if (targetType instanceof UNION) {
            // For unions, accept a single value matching any member type
            // Simplified: just check that there's one element
            int count = 0;
            for (ExpList el = init.exps; el != null; el = el.tail) {
                count++;
                if (el.head != null) {
                    transExp(el.head, frame);
                }
            }
        } else {
            error(init.pos, "initializer list used for non-aggregate type");
        }
    }
    
    private void error(int pos, String msg) {
        errorMsg.error(pos, msg);
    }
    
    static class ExpTy {
        Translate.Exp exp;
        Type ty;
        
        ExpTy(Translate.Exp e, Type t) {
            exp = e;
            ty = t;
        }
    }
}
