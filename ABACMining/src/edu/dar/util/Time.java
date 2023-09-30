/**
 *  Mining Attribute-Based Access Control Policies
 * Copyright (C) 2014 Zhongyuan Xu
 * Copyright (C) 2014 Scott D. Stoller
 * Copyright (c) 2014 Stony Brook University
 * Copyright (c) 2014 Research Foundation of SUNY
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package edu.dar.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
/**
 * Time class is used to record CPU time for experiments. 
 *
 */
public class Time {
	
	static long startCPUTime, startUserTime, startSystemTime;
	static long elapsedCPUTime, elapsedUserTime, elapsedSystemTime;
	
	/** Get CPU time in nanoseconds. */
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime( ) : 0L;
	}
	
	public static double getElapsedCPUTime(){
		return elapsedCPUTime / 1000000000;
	}
	 
	/** Get user time in nanoseconds. */
	public static long getUserTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadUserTime( ) : 0L;
	}
	
	public static long getElapsedUserTime(){
		return elapsedUserTime;
	}

	/** Get system time in nanoseconds. */
	public static long getSystemTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        (bean.getCurrentThreadCpuTime( ) - bean.getCurrentThreadUserTime( )) : 0L;
	}
	
	public static long getElapsedSystemTime(){
		return elapsedSystemTime;
	}
	
	/** Set time in nanoseconds. */
	public static void setStartTime(){
		startCPUTime = getCpuTime();
		startUserTime = getUserTime();
		startSystemTime = getSystemTime();
	}
	
	public static void setElapsedTime(){
		elapsedCPUTime = getCpuTime() - startCPUTime;
		startCPUTime = getCpuTime();
		elapsedUserTime = getUserTime() - startUserTime;
		startUserTime = getUserTime();
		elapsedSystemTime = getSystemTime() - startSystemTime;
		startSystemTime = getSystemTime();		
	}
}
