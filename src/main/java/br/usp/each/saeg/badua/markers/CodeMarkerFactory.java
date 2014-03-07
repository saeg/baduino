package br.usp.each.saeg.badua.markers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;


public class CodeMarkerFactory {

	public static final String COVERED_MARKER = "br.usp.each.saeg.badua.markers.definition";
	public static final String UNCOVERED_MARKER = "br.usp.each.saeg.badua.markers.use";
	public static final String TARGET_MARKER = "br.usp.each.saeg.badua.markers.target";
	//public static final String SAMELINE_MARKER = "br.usp.each.saeg.badua.markers.sameLine";
	public static final String POINTER_MARKER = "br.usp.each.saeg.badua.markers.pointer";

	public static IMarker defMarker;
	public static IMarker useMarker;
	public static IMarker targetMarker;
	public static IMarker pointerMarker;


	public static void mark(final IResource resource, final int[] defOffset, final int[] useOffset, int[] targetOffset, String covered) throws PartInitException {

		try {
			//if Definition and use are in the same line
			//			if(defOffset[1] == useOffset[1]){
			//				defMarker = resource.createMarker(SAMELINE_MARKER);
			//				defMarker.setAttribute(IMarker.CHAR_START, defOffset[0]);
			//				defMarker.setAttribute(IMarker.CHAR_END, defOffset[1]);
			//				
			//			}else{
			//				defMarker = resource.createMarker(DEFINITION_MARKER);
			//				defMarker.setAttribute(IMarker.CHAR_START, defOffset[0]);
			//				defMarker.setAttribute(IMarker.CHAR_END, defOffset[1]);
			//				
			//				useMarker = resource.createMarker(USE_MARKER);
			//				useMarker.setAttribute(IMarker.CHAR_START, useOffset[0]);
			//				useMarker.setAttribute(IMarker.CHAR_END, useOffset[1]);
			//			}
			pointerMarker = resource.createMarker(POINTER_MARKER);
			
			if(covered.equals("true")){
				defMarker = resource.createMarker(COVERED_MARKER);
				useMarker = resource.createMarker(COVERED_MARKER);
			}else{
				defMarker = resource.createMarker(UNCOVERED_MARKER);
				useMarker = resource.createMarker(UNCOVERED_MARKER);
			}

			if(targetOffset != null){
				if(covered.equals("true")){
					targetMarker = resource.createMarker(COVERED_MARKER);
				}else{
					targetMarker = resource.createMarker(UNCOVERED_MARKER);
				}
				targetMarker.setAttribute(IMarker.CHAR_START, targetOffset[0]);
				targetMarker.setAttribute(IMarker.CHAR_END, targetOffset[1]);
			}
			
			defMarker.setAttribute(IMarker.CHAR_START, defOffset[0]);
			defMarker.setAttribute(IMarker.CHAR_END, defOffset[1]);

			useMarker.setAttribute(IMarker.CHAR_START, useOffset[0]);
			useMarker.setAttribute(IMarker.CHAR_END, useOffset[1]);

			pointerMarker.setAttribute(IMarker.CHAR_START,defOffset[0]);
			pointerMarker.setAttribute(IMarker.CHAR_END,defOffset[0]);


		} catch (CoreException e) {e.printStackTrace();}

		IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),pointerMarker); //open view

	}

	/* 
	 * Metodo com workspaceJob, porem dando bug 

	public static void scheduleMarkerCreation(final IResource resource, final int[] defOffset, final int[] useOffset) throws PartInitException {

		WorkspaceJob job = new WorkspaceJob("addMarkers " + new Date().toString()) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor arg0) throws CoreException {
				defMarker = resource.createMarker(DEFINITION_MARKER);
				defMarker.setAttribute(IMarker.CHAR_START, defOffset[0]);
				defMarker.setAttribute(IMarker.CHAR_END, defOffset[1]);


				IMarker useMarker = resource.createMarker(USE_MARKER);
				useMarker.setAttribute(IMarker.CHAR_START, useOffset[0]);
				useMarker.setAttribute(IMarker.CHAR_END, useOffset[1]);

				return Status.OK_STATUS;
			}
		};
		job.setPriority(WorkspaceJob.DECORATE);
		job.schedule();
		IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),defMarker);
	}
	 */

	//create a list with all markers 
	public static List<IMarker> findMarkers(IResource resource) {
		try {
			List<IMarker> result = new ArrayList<IMarker>();
			result.addAll(asList(resource.findMarkers(COVERED_MARKER, false, IResource.DEPTH_ZERO)));
			result.addAll(asList(resource.findMarkers(UNCOVERED_MARKER, false, IResource.DEPTH_ZERO)));
//			result.addAll(asList(resource.findMarkers(SAMELINE_MARKER, false, IResource.DEPTH_ZERO)));
			result.addAll(asList(resource.findMarkers(TARGET_MARKER, false, IResource.DEPTH_ZERO)));
			result.addAll(asList(resource.findMarkers(POINTER_MARKER, false, IResource.DEPTH_ZERO)));

			return result;
		} catch (CoreException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static List<IMarker> asList(IMarker[] arg) {
		if (arg == null || arg.length == 0) {
			return Collections.emptyList();
		}
		return Arrays.asList(arg);
	}

	//delete all markers from the resource
	public static void removeMarkers(final Collection<IMarker> toDelete) {
		if (toDelete == null || toDelete.isEmpty()) {
			return;
		}

		WorkspaceJob job = new WorkspaceJob("deleteMarkers " + new Date().toString()) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor arg0) throws CoreException {

				for (IMarker marker : toDelete) {
					try {
						marker.delete();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(WorkspaceJob.DECORATE);
		job.schedule();
	}


}
