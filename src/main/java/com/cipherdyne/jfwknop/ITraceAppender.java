/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

/**
 *
 * @author franck
 */
public interface ITraceAppender {
    
    	/**
	 * Appends a trace message.
	 *
	 * @param trace Trace message.
	 */
	void append(String trace);
}
