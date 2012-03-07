package com.coryf88.bukkit.annoyances.overridemoblib;

public class OverrideMobException extends Exception {
	private static final long serialVersionUID = 9113149993651196953L;

	public OverrideMobException() {
		super();
	}

	public OverrideMobException(String message) {
		super(message);
	}

	public OverrideMobException(Throwable cause) {
		super(cause);
	}

	public OverrideMobException(String message, Throwable cause) {
		super(message, cause);
	}
}
