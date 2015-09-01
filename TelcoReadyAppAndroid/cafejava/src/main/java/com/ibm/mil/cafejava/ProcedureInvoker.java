/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.cafejava;

import com.worklight.wlclient.api.WLResponseListener;

/**
 * A type which is responsible for knowing how to invoke an MFP procedure. Since there are
 * multiple ways to invoke a procedure depending on its adapter implementation, this type serves
 * as a common interface for representing all forms of procedure invocation.
 *
 * @see CafeJava#invokeProcedure(ProcedureInvoker)
 * @see JavaProcedureInvoker
 * @see JSProcedureInvoker
 *
 * @author John Petitto  (github @jpetitto)
 * @author Tanner Preiss (github @t-preiss)
 */
public interface ProcedureInvoker {
    /**
     * Called internally by {@link CafeJava#invokeProcedure(ProcedureInvoker)} in order to invoke
     * a procedure. The implementation should trigger a procedure invocation, using the {@code
     * WLResponseListener} argument that is provided.
     *
     * @param wlResponseListener Passed in by {@link CafeJava#invokeProcedure(ProcedureInvoker)}
     *                           and will internally notify subscribers when a response is
     *                           emitted by the enclosing {@code Observable}. It is important that
     *                           this {@code WLResponseListener} is used with the procedure
     *                           invocation.
     */
    void invoke(WLResponseListener wlResponseListener);
}
