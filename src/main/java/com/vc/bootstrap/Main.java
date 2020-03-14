package com.vc.bootstrap;

import java.util.Optional;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.vc.log.Log;

public class Main {
	private static final String HOST_NAME = Optional.ofNullable(getSystemHost()).orElse("localhost");
	private static final int PORT = Optional.ofNullable(getSystemPort()).orElse(8080);

	private static final String CONTEXT_PATH = "";
	private static final String APP_BASE = ".";

	public static void main(String[] args) {
		Tomcat tomcat = new Tomcat();
		tomcat.setHostname(HOST_NAME);
		tomcat.setPort(PORT);
		tomcat.getHost().setAppBase(APP_BASE);
		tomcat.setConnector(tomcat.getConnector());
		tomcat.setAddDefaultWebXmlToWebapp(false);
		ServletRegistry.load(tomcat.addWebapp(CONTEXT_PATH, APP_BASE));
		ServletRegistry.reload();

		try {
			Log.info("Starting Tomcat server (%s:%s)", HOST_NAME, PORT);
			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException e) {
			e.printStackTrace();
		} finally {
			try {
				tomcat.stop();
			} catch (LifecycleException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getSystemHost() {
		try {
			return System.getenv("HOSTNAME");
		} catch (Exception e) {
			Log.error("Error occurred while getting system hostname: %s", e.toString());
			return null;
		}
	}

	private static Integer getSystemPort() {
		try {
			String port = System.getenv("PORT");
			return port != null ? Integer.valueOf(port) : null;
		} catch (Exception e) {
			Log.error("Error occurred while getting system port: %s", e.toString());
			return null;
		}
	}
}
