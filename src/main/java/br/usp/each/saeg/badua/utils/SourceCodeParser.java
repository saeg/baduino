package br.usp.each.saeg.badua.utils;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class SourceCodeParser extends ASTVisitor{
	@Override
	public boolean visit(MethodDeclaration node) {
		
		return false;

	}
}