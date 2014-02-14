package br.usp.each.saeg.badua.markers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public static final String DEFINITION_MARKER = "br.usp.each.saeg.badua.markers.definition";
	public static final String USE_MARKER = "br.usp.each.saeg.badua.markers.use";
	public static IMarker defMarker;

	public static void mark(final IResource resource, final int[] defOffset, final int[] useOffset) throws PartInitException {

		try {
			defMarker = resource.createMarker(DEFINITION_MARKER);
			defMarker.setAttribute(IMarker.CHAR_START, defOffset[0]);
			defMarker.setAttribute(IMarker.CHAR_END, defOffset[1]);


			IMarker useMarker = resource.createMarker(USE_MARKER);
			useMarker.setAttribute(IMarker.CHAR_START, useOffset[0]);
			useMarker.setAttribute(IMarker.CHAR_END, useOffset[1]);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),defMarker);
		
		
	}
	
	
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



				//                for (Entry<IResource, List<Map<String, Object>>> entry : resourceMarkerProps.entrySet()) {
				//                    for (Map<String, Object> prop : entry.getValue()) {
				//                        try {
				//                            String markerName = getMarkerNameOf((String)prop.get("score"));
				//                            IMarker marker = entry.getKey().createMarker(markerName);
				//                            marker.setAttributes(prop);
				//
				//                        } catch (Exception e) {
				//                            e.printStackTrace();
				//                        }
				//                    }
				//                }
				return Status.OK_STATUS;
			}
		};
		job.setPriority(WorkspaceJob.DECORATE);
		job.schedule();
		IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),defMarker);
	}
	
	public static List<IMarker> findMarkers(IResource resource) {
        try {
            List<IMarker> result = new ArrayList<IMarker>();
            result.addAll(asList(resource.findMarkers(DEFINITION_MARKER, false, IResource.DEPTH_ZERO)));
            result.addAll(asList(resource.findMarkers(USE_MARKER, false, IResource.DEPTH_ZERO)));

            return result;
        } catch (CoreException e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
	
	private static List<IMarker> asList(IMarker[] arg) {
        if (arg == null || arg.length == 0) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(arg);
    }


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
