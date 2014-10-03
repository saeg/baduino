package br.usp.each.saeg.badua.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import br.usp.each.saeg.badua.markers.CodeMarkerFactory;


public class CleanAllHighlightHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CodeMarkerFactory.removeAllMarkers();
		return null;
	}

}
