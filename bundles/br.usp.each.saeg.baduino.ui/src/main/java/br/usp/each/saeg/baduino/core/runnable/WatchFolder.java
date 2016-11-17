package br.usp.each.saeg.baduino.core.runnable;

import java.io.File;
import java.util.List;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import br.usp.each.saeg.baduino.core.launching.VMListener;

public class WatchFolder implements Runnable {
	
	private static final Logger logger = Logger.getLogger(WatchFolder.class);
	
	private final File dir;
	private final File file;
	private final List<VMListener> listeners;
	private Thread thread;
	
	public WatchFolder(File file, List<VMListener> listeners) {
		this.dir = file.getParentFile();
		this.file = file;
		this.listeners = listeners;
	}

	@Override
	public void run() {
		final FileAlterationObserver fao = new FileAlterationObserver(dir);
		final FileAlterationMonitor monitor = new FileAlterationMonitor(100);
		monitor.addObserver(fao);
		fao.addListener(new FileAlterationListener() {
			@Override
			public void onStart(FileAlterationObserver observer) {}

			@Override
			public void onDirectoryCreate(File directory) {}

			@Override
			public void onDirectoryChange(File directory) {}

			@Override
			public void onDirectoryDelete(File directory) {}

			@Override
			public void onFileCreate(File f) {
				if (file.equals(f)) {
					logger.debug(f.getName() + " created");
					for (VMListener listener : listeners) {
						listener.terminated();
					}
					
					try {
						monitor.stop();
					} catch (Exception e) {/*ignored */}
				}
			}

			@Override
			public void onFileChange(File f) {
				if (file.equals(f)) {
					logger.debug(f.getName() + " changed");
					for (VMListener listener : listeners) {
						listener.terminated();
					}
					
					try {
						monitor.stop();
					} catch (Exception e) {/*ignored */}
				}
			}

			@Override
			public void onFileDelete(File f) {}

			@Override
			public void onStop(FileAlterationObserver observer) {}
			
		});
		
		try {
			monitor.start();
		} 
		catch (Exception e) {/*ignored */}
	}
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

}
