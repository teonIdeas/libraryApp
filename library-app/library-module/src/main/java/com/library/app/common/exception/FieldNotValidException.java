package com.library.app.common.exception;

public class FieldNotValidException extends RuntimeException{
	private static final long serialVersionUID = 4525821332583716666L;
	
	private final String fieldName;

	public FieldNotValidException(String fieldName, final String message) {
		super(message);
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String toString() {
		return "FieldNotValidException [fieldName=" + fieldName + "]";
	}

}
