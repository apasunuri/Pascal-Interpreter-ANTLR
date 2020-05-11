import java.util.*;

public class Visitor extends project2BaseVisitor<Value> {
	Stack<Scope> scopes = new Stack<>();
	private Scope global = new Scope(ScopeType.GLOBAL);
	private Map<String, List<Value>> functionArgs = new HashMap<>();
	private List<project2Parser.FunctionsContext> functionLocations = new ArrayList<>();
	private List<project2Parser.ProceduresContext> procedureLocations = new ArrayList<>();
	private Scanner scan = new Scanner(System.in);
	private boolean isBreak = false;
	private boolean isContinue = false;

	@Override
	public Value visitProgram(project2Parser.ProgramContext ctx) {
		scopes.push(global);
		this.visit(ctx.program_name());
		if(ctx.program_variables() != null)
			this.visit(ctx.program_variables());
		for(int i = 0; i < ctx.functions().size(); i++) {
			this.visit(ctx.functions(i));
			functionLocations.add(ctx.functions(i));
		}
		for(int i = 0; i < ctx.procedures().size(); i++) {
			this.visit(ctx.procedures(i));
			procedureLocations.add(ctx.procedures(i));
		}
		this.visit(ctx.program_block());
		return Value.VOID;
	}
	@Override 
	public Value visitProgram_variables(project2Parser.Program_variablesContext ctx) { 
		for(int i = 0; i < ctx.vars().var().size(); i++) {
			for(int j = 0; j < ctx.vars().var(i).ID().size(); j++) {
				global.variableTypes.put(ctx.vars().var(i).ID(j).getText().toLowerCase(), ctx.vars().var(i).TYPE().getText().toLowerCase());
				global.variableValues.put(ctx.vars().var(i).ID(j).getText().toLowerCase(), Value.VOID);
			}
		}
		return Value.VOID; 
	}

	@Override 
	public Value visitProcedures(project2Parser.ProceduresContext ctx) {
		scopes.push(new Scope(ScopeType.FUNCTION)); 
		if(!global.variableValues.containsKey(ctx.ID().getText().toLowerCase())) {
			global.variableValues.put(ctx.ID().getText().toLowerCase(), Value.VOID);
		}
		else {
			if(ctx.params() != null) {
				for(int i = 0; i < ctx.params().param_list().size(); i++) {
					for(int j = 0; j < ctx.params().param_list(i).ID().size(); j++) {
						if(ctx.params().param_list(i).TYPE().getText().toLowerCase().equals("boolean")) {
							scopes.peek().variableValues.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), new Value(functionArgs.get(ctx.ID().getText().toLowerCase()).get(i * ctx.params().param_list(i).ID().size() + j).toBoolean()));
							scopes.peek().variableTypes.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), "boolean");
						}
						else {
							scopes.peek().variableValues.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), new Value(functionArgs.get(ctx.ID().getText().toLowerCase()).get(i * ctx.params().param_list(i).ID().size() + j).toDouble()));
							scopes.peek().variableTypes.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), "real");
						}
					}
				}
			}
			if(ctx.function_variables() != null)
				this.visit(ctx.function_variables());
			this.visit(ctx.function_block());
			List<Value> newFunctionArgs = new ArrayList<>();
			if(ctx.params() != null) {
				for(int i = 0; i < ctx.params().param_list().size(); i++) {
					for(int j = 0; j < ctx.params().param_list(i).ID().size(); j++) {
						newFunctionArgs.add(scopes.peek().variableValues.get(ctx.params().param_list(i).ID(j).getText().toLowerCase()));
					}
				}
			}
			functionArgs.put(ctx.ID().getText().toLowerCase(), newFunctionArgs);
			global.variableValues.put(ctx.ID().getText().toLowerCase(), Value.VOID);
		}
		scopes.pop();
		return Value.VOID; 
	} 

	@Override 
	public Value visitFunctions(project2Parser.FunctionsContext ctx) {
		scopes.push(new Scope(ScopeType.FUNCTION)); 
		if(!global.variableValues.containsKey(ctx.ID().getText().toLowerCase())) {
			global.variableValues.put(ctx.ID().getText().toLowerCase(), Value.VOID);
			global.variableTypes.put(ctx.ID().getText().toLowerCase(), ctx.TYPE().getText().toLowerCase());
		}
		else {
			if(ctx.params() != null) {
				for(int i = 0; i < ctx.params().param_list().size(); i++) {
					for(int j = 0; j < ctx.params().param_list(i).ID().size(); j++) {
						if(ctx.params().param_list(i).TYPE().getText().toLowerCase().equals("boolean")) {
							scopes.peek().variableValues.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), new Value(functionArgs.get(ctx.ID().getText().toLowerCase()).get(i * ctx.params().param_list(i).ID().size() + j).toBoolean()));
							scopes.peek().variableTypes.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), "boolean");
						}
						else {
							scopes.peek().variableValues.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), new Value(functionArgs.get(ctx.ID().getText().toLowerCase()).get(i * ctx.params().param_list(i).ID().size() + j).toDouble()));
							scopes.peek().variableTypes.put(ctx.params().param_list(i).ID(j).getText().toLowerCase(), "real");
						}
					}
				}
			}
			if(ctx.function_variables() != null)
				this.visit(ctx.function_variables());
			this.visit(ctx.function_block());
			List<Value> newFunctionArgs = new ArrayList<>();
			if(ctx.params() != null) {
				for(int i = 0; i < ctx.params().param_list().size(); i++) {
					for(int j = 0; j < ctx.params().param_list(i).ID().size(); j++) {
						newFunctionArgs.add(scopes.peek().variableValues.get(ctx.params().param_list(i).ID(j).getText().toLowerCase()));
					}
				}
			}
			functionArgs.put(ctx.ID().getText().toLowerCase(), newFunctionArgs);
		}
		scopes.pop();
		return Value.VOID; 
	} 
	@Override 
	public Value visitFunction_variables(project2Parser.Function_variablesContext ctx) { 
		for(int i = 0; i < ctx.vars().var().size(); i++) {
			for(int j = 0; j < ctx.vars().var(i).ID().size(); j++) {
				scopes.peek().variableTypes.put(ctx.vars().var(i).ID(j).getText().toLowerCase(), ctx.vars().var(i).TYPE().getText().toLowerCase());
				scopes.peek().variableValues.put(ctx.vars().var(i).ID(j).getText().toLowerCase(), Value.VOID);
			}
		}
		return Value.VOID; 
	} 

	@Override 
	public Value visitProgram_block(project2Parser.Program_blockContext ctx) { 
		this.visit(ctx.program_statements());
		return Value.VOID; 
	}
	
	@Override 
	public Value visitFunction_call(project2Parser.Function_callContext ctx) {
		int index = 0;
		boolean isFunction = false;
		for(int i = 0; i < functionLocations.size(); i++) {
			if(ctx.ID().getText().toLowerCase().equals(functionLocations.get(i).ID().getText().toLowerCase())) {
				index = i;
				isFunction = true;
			}
		}
		for(int i = 0; i < procedureLocations.size(); i++) {
			if(ctx.ID().getText().toLowerCase().equals(procedureLocations.get(i).ID().getText().toLowerCase())) {
				index = i;
				isFunction = false;
			}
		}
		if(ctx.args() != null) {
			List<Value> argValues = new ArrayList<>();
			for(int i = 0; i < ctx.args().arg().size(); i++) {
				Value value = this.visit(ctx.args().arg(i));
				argValues.add(value);
			}
			functionArgs.put(ctx.ID().getText().toLowerCase(), argValues);
		}
		if(isFunction)
			this.visit(functionLocations.get(index));
		else
			this.visit(procedureLocations.get(index));
		if(ctx.args() != null) {
			for(int i = 0; i < ctx.args().arg().size(); i++) {
				if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
					if(scopes.peek().variableValues.containsKey(ctx.getText().toLowerCase()))
						scopes.peek().variableValues.put(ctx.args().arg(i).getText(), functionArgs.get(ctx.ID().getText().toLowerCase()).get(i));
					if(global.variableValues.containsKey(ctx.getText().toLowerCase()))
						global.variableValues.put(ctx.args().arg(i).getText(), functionArgs.get(ctx.ID().getText().toLowerCase()).get(i));
				}
				else if(scopes.peek().type.equals(ScopeType.LOOP)) {
					for(int j = scopes.size() - 1; j >= 0; j--) {
						if(scopes.get(j).variableValues.containsKey(ctx.getText().toLowerCase()))
							scopes.get(j).variableValues.put(ctx.args().arg(i).getText(), functionArgs.get(ctx.ID().getText().toLowerCase()).get(i));
					} 
				}
				else if(scopes.peek().type.equals(ScopeType.GLOBAL)) {
					global.variableValues.put(ctx.args().arg(i).getText(), functionArgs.get(ctx.ID().getText().toLowerCase()).get(i));
				}
			}
		}
		return global.variableValues.get(ctx.ID().getText().toLowerCase()); 
	} 
	@Override 
	public Value visitArg(project2Parser.ArgContext ctx) { 
		return this.visit(ctx.expr());
	} 

	@Override 
	public Value visitFor_loop(project2Parser.For_loopContext ctx) {
		scopes.push(new Scope(ScopeType.LOOP));
		if(ctx.loop_variables() != null)
			this.visit(ctx.loop_variables());
		Value startIndex = this.visit(ctx.index(0));
		Value endIndex = this.visit(ctx.index(1));
		int start = (int) Double.parseDouble(startIndex.toString());
		int end = (int) Double.parseDouble(endIndex.toString());
		for(int i = start; i <= end; i++) {
			double val = (double) i;
			if(ctx.VAR() != null) {
				scopes.peek().variableTypes.put(ctx.ID().getText().toLowerCase(), "real");
				scopes.peek().variableValues.put(ctx.ID().getText().toLowerCase(), new Value(val));
			}
			else
				global.variableValues.put(ctx.ID().getText().toLowerCase(), new Value(val));
			if(isContinue) {
				isContinue = false;
				continue;
			}
			if(isBreak) {
				isBreak = false;
				break;
			}
			this.visit(ctx.loop_block());
		}
		scopes.pop();
		return Value.VOID;
	} 
	@Override 
	public Value visitWhile_loop(project2Parser.While_loopContext ctx) {
		scopes.push(new Scope(ScopeType.LOOP));
		if(ctx.loop_variables() != null)
			this.visit(ctx.loop_variables());
		Value condition = this.visit(ctx.condition());
		while(condition.toBoolean()) {
			if(isContinue) {
				isContinue = false;
				continue;
			}
			if(isBreak) {
				isBreak = false;
				break;
			}
			this.visit(ctx.loop_block());
			condition = this.visit(ctx.condition());
		}
		scopes.pop(); 
		return Value.VOID;
	} 
	@Override 
	public Value visitLoop_variables(project2Parser.Loop_variablesContext ctx) {
		for(int i = 0; i < ctx.vars().var().size(); i++) {
			for(int j = 0; j < ctx.vars().var(i).ID().size(); j++) {
				scopes.peek().variableTypes.put(ctx.vars().var(i).ID(j).getText().toLowerCase(), ctx.vars().var(i).TYPE().getText().toLowerCase());
				scopes.peek().variableValues.put(ctx.vars().var(i).ID(j).getText().toLowerCase(), Value.VOID);
			}
		}
		return Value.VOID; 
	}
	@Override 
	public Value visitIndexId(project2Parser.IndexIdContext ctx) {
		for(int i = scopes.size() - 1; i >= 0; i--) {
			if(scopes.get(i).variableValues.containsKey(ctx.getText().toLowerCase()))
				return scopes.get(i).variableValues.get(ctx.getText().toLowerCase());
		}  
		return Value.VOID; 
	}
	@Override 
	public Value visitIndexRealNum(project2Parser.IndexRealNumContext ctx) { 
		return new Value(ctx.REAL_NUM().getText()); 
	}
	@Override 
	public Value visitLoopStatementBlock(project2Parser.LoopStatementBlockContext ctx) { 
		Value value = this.visit(ctx.loop_statement());
		if(value != null && value.toString().equals("break")) {
			isBreak = true;
			return Value.VOID;
		}
		if(value != null && value.toString().equals("continue")) {
			isContinue = true;
			return Value.VOID;
		}
		else
			return value; 
	}
	@Override 
	public Value visitLoopStatementsBlock(project2Parser.LoopStatementsBlockContext ctx) { 
		return this.visit(ctx.loop_statements());
	}
	@Override 
	public Value visitLoop_statements(project2Parser.Loop_statementsContext ctx) { 
		for(int i = 0; i < ctx.loop_statement().size(); i++) {
			Value value = this.visit(ctx.loop_statement(i));
			if(value != null && value.toString().equals("break")) {
				isBreak = true;
				return Value.VOID;
			}
			if(value != null && value.toString().equals("continue")) {
				isContinue = true;
				return Value.VOID;
			}
		}
		return Value.VOID;
	}
	@Override 
	public Value visitLoopAssignment(project2Parser.LoopAssignmentContext ctx) { 
		return this.visit(ctx.assignment());
	} 
	@Override 
	public Value visitLoopConditional(project2Parser.LoopConditionalContext ctx) { 
		return this.visit(ctx.conditional_loop());
	} 
	@Override 
	public Value visitLoopCase(project2Parser.LoopCaseContext ctx) { 
		return this.visit(ctx.r_case_loop());
	} 
	@Override 
	public Value visitLoopFunctionCall(project2Parser.LoopFunctionCallContext ctx) { 
		return this.visit(ctx.function_call());
	} 
	@Override 
	public Value visitLoopWriteln(project2Parser.LoopWritelnContext ctx) { 
		return this.visit(ctx.writeln()); 
	} 
	@Override 
	public Value visitLoopReadln(project2Parser.LoopReadlnContext ctx) { 
		return this.visit(ctx.readln());
	} 
	@Override 
	public Value visitLoopBreak(project2Parser.LoopBreakContext ctx) { 
		return new Value("break"); 
	} 
	@Override 
	public Value visitLoopContinue(project2Parser.LoopContinueContext ctx) { 
		return new Value("continue"); 
	}
	@Override 
	public Value visitR_case_loop(project2Parser.R_case_loopContext ctx) { 
		List<project2Parser.Case_labelContext> caseList = ctx.case_label();
		Value condition = this.visit(ctx.case_expression());
		for(int i = 0; i < caseList.size(); i++) {
			Value caseValue = this.visit(ctx.case_label(i));
			try {
				if(Math.abs(condition.toDouble() - caseValue.toDouble()) < 0.000000001) {
					this.visit(ctx.conditional_block_loop(i));
					break;
				}
			}
			catch(Exception e) {
				if(condition.toBoolean() == caseValue.toBoolean()) {
					this.visit(ctx.conditional_block_loop(i));
					break;
				}
			}
		}
		return Value.VOID;
	} 
	@Override 
	public Value visitConditional_loop(project2Parser.Conditional_loopContext ctx) { 
		Value value = this.visit(ctx.condition());
		if(value.toBoolean())
			this.visit(ctx.conditional_block_loop(0));
		else
			this.visit(ctx.conditional_block_loop(1));
		return Value.VOID;	 
	}
	@Override 
	public Value visitConditionalBlockStatementLoop(project2Parser.ConditionalBlockStatementLoopContext ctx) { 
		Value value = this.visit(ctx.block_statement_loop());
		if(value != null && value.toString().equals("break")) {
			isBreak = true;
			return Value.VOID;
		}
		if(value != null && value.toString().equals("continue")) {
			isContinue = true;
			return Value.VOID;
		}
		else
			return value;
	}
	@Override 
	public Value visitConditionalBlockStatementsLoop(project2Parser.ConditionalBlockStatementsLoopContext ctx) { 
		return this.visit(ctx.block_statements_loop());
	}
	@Override 
	public Value visitBlock_statements_loop(project2Parser.Block_statements_loopContext ctx) { 
		for(int i = 0; i < ctx.block_statement_loop().size(); i++) {
			Value value = this.visit(ctx.block_statement_loop(i));
			if(value != null && value.toString().equals("break")) {
				isBreak = true;
				return Value.VOID;
			}
			if(value != null && value.toString().equals("continue")) {
				isContinue = true;
				return Value.VOID;
			}
		}
		return Value.VOID;
	}
	@Override 
	public Value visitConditionalLoopAssignment(project2Parser.ConditionalLoopAssignmentContext ctx) { 
		return this.visit(ctx.assignment()); 
	}
	@Override 
	public Value visitConditionalLoopForLoop(project2Parser.ConditionalLoopForLoopContext ctx) { 
		return this.visit(ctx.for_loop());
	}
	@Override 
	public Value visitConditionalLoopWhileLoop(project2Parser.ConditionalLoopWhileLoopContext ctx) { 
		return this.visit(ctx.while_loop()); 
	}
	@Override 
	public Value visitConditionalLoopWritelnLoop(project2Parser.ConditionalLoopWritelnLoopContext ctx) { 
		return this.visit(ctx.writeln());
	}
	@Override 
	public Value visitConditionalLoopReadlnLoop(project2Parser.ConditionalLoopReadlnLoopContext ctx) { 
		return this.visit(ctx.readln()); 
	}
	@Override 
	public Value visitConditionalLoopBreakLoop(project2Parser.ConditionalLoopBreakLoopContext ctx) { 
		return new Value("break");
	}
	@Override 
	public Value visitConditionalLoopContinueLoop(project2Parser.ConditionalLoopContinueLoopContext ctx) { 
		return new Value("continue");
	}

	@Override 
	public Value visitAssignment(project2Parser.AssignmentContext ctx) {
		String id = ctx.ID().getText().toLowerCase();
		Value value = this.visit(ctx.expr());
		if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
			if(scopes.peek().variableValues.containsKey(ctx.ID().getText().toLowerCase()))
				scopes.peek().variableValues.put(id, value);
			else if(global.variableValues.containsKey(ctx.ID().getText().toLowerCase()))
				global.variableValues.put(id, value);
		}
		else if(scopes.peek().type.equals(ScopeType.LOOP)) {
			for(int i = scopes.size() - 1; i >= 0; i--) {
				if(scopes.get(i).variableValues.containsKey(ctx.ID().getText().toLowerCase()))
					scopes.get(i).variableValues.put(id, value);
			} 
		}
		else if(scopes.peek().type.equals(ScopeType.GLOBAL)) {
			global.variableValues.put(id, value);
		}
		return value;
	} 
	
	@Override 
	public Value visitArithmeticExpr(project2Parser.ArithmeticExprContext ctx) { 
		return this.visit(ctx.arithmetic_expr());
	}
	@Override 
	public Value visitLogicalExpr(project2Parser.LogicalExprContext ctx) { 
		return this.visit(ctx.logical_expr());
	} 
	@Override 
	public Value visitRelationalExprStart(project2Parser.RelationalExprStartContext ctx) { 
		return this.visit(ctx.relational_expr());
	} 
	
	@Override 
	public Value visitRelationalExprParen(project2Parser.RelationalExprParenContext ctx) {
		return this.visit(ctx.relational_expr());
	} 
	@Override 
	public Value visitEqualExpr(project2Parser.EqualExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(Math.abs(left.toDouble() - right.toDouble()) < 0.000000001);
	} 
	@Override 
	public Value visitNotEqualExprArithmetic(project2Parser.NotEqualExprArithmeticContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(Math.abs(left.toDouble() - right.toDouble()) >= 0.000000001);
	} 
	@Override 
	public Value visitLessThanExpr(project2Parser.LessThanExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() < right.toDouble());
	} 
	@Override 
	public Value visitLessEqualExpr(project2Parser.LessEqualExprContext ctx) {
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() <= right.toDouble());
	} 
	@Override 
	public Value visitGreaterThanExpr(project2Parser.GreaterThanExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() > right.toDouble());
	} 
	@Override 
	public Value visitGreaterEqualExpr(project2Parser.GreaterEqualExprContext ctx) {
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() >= right.toDouble());
	} 
	
	@Override 
	public Value visitIdExprLogical(project2Parser.IdExprLogicalContext ctx) {
		if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
			if(scopes.peek().variableValues.containsKey(ctx.getText().toLowerCase()))
				return scopes.peek().variableValues.get(ctx.getText().toLowerCase());
			else if(global.variableValues.containsKey(ctx.getText().toLowerCase()))
				return global.variableValues.get(ctx.getText().toLowerCase());
		}
		else if(scopes.peek().type.equals(ScopeType.LOOP) || scopes.peek().type.equals(ScopeType.GLOBAL)) {
			for(int i = scopes.size() - 1; i >= 0; i--) {
				if(scopes.get(i).variableValues.containsKey(ctx.getText().toLowerCase()))
					return scopes.get(i).variableValues.get(ctx.getText().toLowerCase());
			}
		}
		return Value.VOID;
 	} 
	@Override 
	public Value visitEqualExprLogical(project2Parser.EqualExprLogicalContext ctx) { 
		Value left = this.visit(ctx.logical_expr(0));
		Value right = this.visit(ctx.logical_expr(1));
		return new Value(left.toBoolean() == right.toBoolean());
	} 
	@Override 
	public Value visitNotExpr(project2Parser.NotExprContext ctx) { 
		Value value = this.visit(ctx.logical_expr());
		return new Value(!value.toBoolean());
	} 
	@Override 
	public Value visitTrueExpr(project2Parser.TrueExprContext ctx) { 
		return new Value(Boolean.parseBoolean(ctx.getText()));
	} 
	@Override 
	public Value visitNotEqualExprLogical(project2Parser.NotEqualExprLogicalContext ctx) { 
		Value left = this.visit(ctx.logical_expr(0));
		Value right = this.visit(ctx.logical_expr(1));
		return new Value(left.toBoolean() != right.toBoolean());
	} 
	@Override 
	public Value visitNotLogicalExprParen(project2Parser.NotLogicalExprParenContext ctx) { 
		Value value = this.visit(ctx.logical_expr());
		return new Value(!value.toBoolean());
	} 
	@Override 
	public Value visitOrExpr(project2Parser.OrExprContext ctx) { 
		Value left = this.visit(ctx.logical_expr(0));
		Value right = this.visit(ctx.logical_expr(1));
		return new Value(left.toBoolean() || right.toBoolean());
	} 
	@Override 
	public Value visitRelationalExpr(project2Parser.RelationalExprContext ctx) { 
		return this.visit(ctx.relational_expr());
	} 
	@Override 
	public Value visitFalseExpr(project2Parser.FalseExprContext ctx) { 
		return new Value(Boolean.parseBoolean(ctx.getText()));
	} 
	@Override 
	public Value visitLogicalExprParen(project2Parser.LogicalExprParenContext ctx) { 
		return this.visit(ctx.logical_expr());
	} 
	@Override 
	public Value visitAndExpr(project2Parser.AndExprContext ctx) { 
		Value left = this.visit(ctx.logical_expr(0));
		Value right = this.visit(ctx.logical_expr(1));
		return new Value(left.toBoolean() && right.toBoolean());
	}
	@Override
	public Value visitFunctionCallLogicalExpr(project2Parser.FunctionCallLogicalExprContext ctx) {
		return this.visit(ctx.function_call());
	} 

	@Override 
	public Value visitRealNumExpr(project2Parser.RealNumExprContext ctx) { 
		return new Value(Double.parseDouble(ctx.getText()));
	} 
	@Override 
	public Value visitCosExpr(project2Parser.CosExprContext ctx) { 
		Value value = this.visit(ctx.arithmetic_expr());
		return new Value(Math.cos(value.toDouble()));
	} 
	@Override 
	public Value visitExpExpr(project2Parser.ExpExprContext ctx) { 
		Value value = this.visit(ctx.arithmetic_expr());
		return new Value(Math.exp(value.toDouble()));
	} 
	@Override 
	public Value visitSinExpr(project2Parser.SinExprContext ctx) { 
		Value value = this.visit(ctx.arithmetic_expr());
		return new Value(Math.sin(value.toDouble()));
	} 
	@Override 
	public Value visitSqrtExpr(project2Parser.SqrtExprContext ctx) { 
		Value value = this.visit(ctx.arithmetic_expr());
		return new Value(Math.sqrt(value.toDouble()));
	} 
	@Override 
	public Value visitAdditionExpr(project2Parser.AdditionExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() + right.toDouble());
	} 
	@Override 
	public Value visitMultiplicationExpr(project2Parser.MultiplicationExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() * right.toDouble());
	} 
	@Override 
	public Value visitArithmeticExprParen(project2Parser.ArithmeticExprParenContext ctx) { 
		return this.visit(ctx.arithmetic_expr());
	} 
	@Override 
	public Value visitLnExpr(project2Parser.LnExprContext ctx) { 
		Value value = this.visit(ctx.arithmetic_expr());
		return new Value(Math.log(value.toDouble()));
	} 
	@Override 
	public Value visitDivisionExpr(project2Parser.DivisionExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() / right.toDouble());
	} 
	@Override 
	public Value visitSubtractionExpr(project2Parser.SubtractionExprContext ctx) { 
		Value left = this.visit(ctx.arithmetic_expr(0));
		Value right = this.visit(ctx.arithmetic_expr(1));
		return new Value(left.toDouble() - right.toDouble());
	} 
	@Override 
	public Value visitIdExprArithmetic(project2Parser.IdExprArithmeticContext ctx) {
		if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
			if(scopes.peek().variableValues.containsKey(ctx.getText().toLowerCase()))
				return scopes.peek().variableValues.get(ctx.getText().toLowerCase());
			else if(global.variableValues.containsKey(ctx.getText().toLowerCase()))
				return global.variableValues.get(ctx.getText().toLowerCase());
		}
		else if(scopes.peek().type.equals(ScopeType.LOOP) || scopes.peek().type.equals(ScopeType.GLOBAL)) {
			for(int i = scopes.size() - 1; i >= 0; i--) {
				if(scopes.get(i).variableValues.containsKey(ctx.getText().toLowerCase()))
					return scopes.get(i).variableValues.get(ctx.getText().toLowerCase());
			}
		}
		return Value.VOID;
	}
	@Override
	public Value visitFunctionCallArithmeticExpr(project2Parser.FunctionCallArithmeticExprContext ctx) {
		return this.visit(ctx.function_call());
	} 
	
	@Override 
	public Value visitConditionParen(project2Parser.ConditionParenContext ctx) { 
		return this.visit(ctx.condition());
	}
	@Override 
	public Value visitConditionLogicalExpr(project2Parser.ConditionLogicalExprContext ctx) { 
		return this.visit(ctx.logical_expr());
	}
	@Override 
	public Value visitConditionRelationExpr(project2Parser.ConditionRelationExprContext ctx) { 
		return this.visit(ctx.relational_expr());
	}   	 
	@Override 
	public Value visitConditional(project2Parser.ConditionalContext ctx) { 
		Value value = this.visit(ctx.condition());
		if(value.toBoolean())
			this.visit(ctx.conditional_block(0));
		else
			this.visit(ctx.conditional_block(1));
		return Value.VOID;
	} 

	@Override 
	public Value visitCaseExpressionParen(project2Parser.CaseExpressionParenContext ctx) { 
		return this.visit(ctx.case_expression());
	}
	@Override 
	public Value visitCaseExpressionId(project2Parser.CaseExpressionIdContext ctx) {
		if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
			if(scopes.peek().variableValues.containsKey(ctx.getText().toLowerCase()))
				return scopes.peek().variableValues.get(ctx.getText().toLowerCase());
			else if(global.variableValues.containsKey(ctx.getText().toLowerCase()))
				return global.variableValues.get(ctx.getText().toLowerCase());
		}
		else if(scopes.peek().type.equals(ScopeType.LOOP) || scopes.peek().type.equals(ScopeType.GLOBAL)) {
			for(int i = scopes.size() - 1; i >= 0; i--) {
				if(scopes.get(i).variableValues.containsKey(ctx.getText().toLowerCase()))
					return scopes.get(i).variableValues.get(ctx.getText().toLowerCase());
			} 
		}
		return Value.VOID;
	} 
	@Override 
	public Value visitCaseExpressionRealNum(project2Parser.CaseExpressionRealNumContext ctx) { 
		return new Value(Double.parseDouble(ctx.getText()));
	} 
	@Override 
	public Value visitCaseExpressionTrue(project2Parser.CaseExpressionTrueContext ctx) { 
		return new Value(Boolean.parseBoolean(ctx.getText()));
	}
	@Override 
	public Value visitCaseExpressionFalse(project2Parser.CaseExpressionFalseContext ctx) { 
		return new Value(Boolean.parseBoolean(ctx.getText()));
	}    
	@Override 
	public Value visitCaseLabelRealNum(project2Parser.CaseLabelRealNumContext ctx) { 
		return new Value(Double.parseDouble(ctx.getText()));
	} 
	@Override 
	public Value visitCaseLabelTrue(project2Parser.CaseLabelTrueContext ctx) { 
		return new Value(Boolean.parseBoolean(ctx.getText()));
	} 
	@Override 
	public Value visitCaseLabelFalse(project2Parser.CaseLabelFalseContext ctx) { 
		return new Value(Boolean.parseBoolean(ctx.getText()));
	} 
	@Override 
	public Value visitR_case(project2Parser.R_caseContext ctx) { 
		List<project2Parser.Case_labelContext> caseList = ctx.case_label();
		Value condition = this.visit(ctx.case_expression());
		for(int i = 0; i < caseList.size(); i++) {
			Value caseValue = this.visit(ctx.case_label(i));
			try {
				if(Math.abs(condition.toDouble() - caseValue.toDouble()) < 0.000000001) {
					this.visit(ctx.conditional_block(i));
					break;
				}
			}
			catch(Exception e) {
				if(condition.toBoolean() == caseValue.toBoolean()) {
					this.visit(ctx.conditional_block(i));
					break;
				}
			}
		}
		return Value.VOID;
	} 

	@Override 
	public Value visitValueId(project2Parser.ValueIdContext ctx) {
		Value value = null;
		boolean isBoolean = false;
		if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
			if(scopes.peek().variableValues.containsKey(ctx.getText().toLowerCase())) {
				if(scopes.peek().variableTypes.get(ctx.getText()).equals("boolean"))
					isBoolean = true;
				value = scopes.peek().variableValues.get(ctx.getText().toLowerCase());
			}
			else if(global.variableValues.containsKey(ctx.getText().toLowerCase())) {
				if(global.variableTypes.get(ctx.getText()).equals("boolean"))
					isBoolean = true;
				value = global.variableValues.get(ctx.getText().toLowerCase());
			}
		}
		else if(scopes.peek().type.equals(ScopeType.LOOP)) {
			for(int i = scopes.size() - 1; i >= 0; i--) {
				if(scopes.get(i).variableValues.containsKey(ctx.getText().toLowerCase())) {
					value = scopes.get(i).variableValues.get(ctx.getText().toLowerCase());
					if(scopes.get(i).variableTypes.get(ctx.getText()).equals("boolean"))
						isBoolean = true;
				}
			}
		}
		else if(scopes.peek().type.equals(ScopeType.GLOBAL)) {
			value = global.variableValues.get(ctx.getText().toLowerCase());
			if(global.variableTypes.get(ctx.getText()).equals("boolean"))
				isBoolean = true;
		}
		if(value != null) {
			if(isBoolean)
				System.out.println(value.toBoolean());
			else
				System.out.println(value.toDouble());
		}
		return value;
	}
	@Override 
	public Value visitValueArithmetic(project2Parser.ValueArithmeticContext ctx) { 
		Value value = this.visit(ctx.arithmetic_expr());
		System.out.println(value.toDouble());
		return value;
	} 
	@Override 
	public Value visitValueLogical(project2Parser.ValueLogicalContext ctx) { 
		Value value = this.visit(ctx.logical_expr());
		System.out.println(value.toBoolean());
		return value;
	} 
	@Override 
	public Value visitValueRelational(project2Parser.ValueRelationalContext ctx) { 
		Value value = this.visit(ctx.relational_expr());
		System.out.println(value.toBoolean());
		return value;
	} 
	@Override 
	public Value visitValueFunction(project2Parser.ValueFunctionContext ctx) { 
		Value value = this.visit(ctx.function_call());
		if(global.variableTypes.get(ctx.function_call().ID().getText().toLowerCase()).equals("boolean"))
			System.out.println(value.toBoolean());
		else
			System.out.println(value.toDouble());
		return value;
	} 
	@Override 
	public Value visitValueStr(project2Parser.ValueStrContext ctx) { 
		Value value = new Value(ctx.getText().substring(1, ctx.getText().length() - 1));
		System.out.println(ctx.getText().substring(1, ctx.getText().length() - 1));
		return value;
	}  
	@Override 
	public Value visitWriteln(project2Parser.WritelnContext ctx) { 
		if(ctx.value() == null)
			System.out.println();
		else
			return this.visit(ctx.value());
		return Value.VOID;
	} 
	@Override 
	public Value visitReadln(project2Parser.ReadlnContext ctx) { 
		if(ctx.ID() != null) {
			if(scopes.peek().type.equals(ScopeType.FUNCTION)) {
				if(scopes.peek().variableValues.containsKey(ctx.getText().toLowerCase())) {
					if(scopes.peek().variableTypes.get(ctx.ID().getText().toLowerCase()).equals("boolean")) {
						boolean value = scan.nextBoolean();
						return scopes.peek().variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
					}
					if(scopes.peek().variableTypes.get(ctx.ID().getText().toLowerCase()).equals("real")) {
						double value = scan.nextDouble();
						return scopes.peek().variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
					}
				}
				else if(global.variableValues.containsKey(ctx.getText().toLowerCase())) {
					if(global.variableTypes.get(ctx.ID().getText().toLowerCase()).equals("boolean")) {
						boolean value = scan.nextBoolean();
						return global.variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
					}
					if(global.variableTypes.get(ctx.ID().getText().toLowerCase()).equals("real")) {
						double value = scan.nextDouble();
						return global.variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
					}
				}
			}
			else if(scopes.peek().type.equals(ScopeType.LOOP)) {
				for(int i = scopes.size() - 1; i >= 0; i--) {
					if(scopes.get(i).variableValues.containsKey(ctx.getText().toLowerCase())) {
						if(scopes.get(i).variableTypes.get(ctx.ID().getText().toLowerCase()).equals("boolean")) {
							boolean value = scan.nextBoolean();
							return scopes.get(i).variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
						}
						if(scopes.get(i).variableTypes.get(ctx.ID().getText().toLowerCase()).equals("real")) {
							double value = scan.nextDouble();
							return scopes.get(i).variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
						}
					}
				} 
			}
			else if(scopes.peek().type.equals(ScopeType.GLOBAL)) {
				if(global.variableTypes.get(ctx.ID().getText().toLowerCase()).equals("boolean")) {
					boolean value = scan.nextBoolean();
					return global.variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
				}
				if(global.variableTypes.get(ctx.ID().getText().toLowerCase()).equals("real")) {
					double value = scan.nextDouble();
					return global.variableValues.put(ctx.ID().getText().toLowerCase(), new Value(value));
				}
			}
		}
		else {
			scan.nextLine();
		}
		return Value.VOID;
	} 
}
