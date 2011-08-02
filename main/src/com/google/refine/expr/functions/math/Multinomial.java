/*

Copyright 2010, Google Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.google.refine.expr.functions.math;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONWriter;

import com.google.refine.expr.EvalError;
import com.google.refine.grel.ControlFunctionRegistry;
import com.google.refine.grel.Function;

public class Multinomial implements Function {

    @Override
    public Object call(Properties bindings, Object[] args) {
        if (args.length < 1) {
            return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " expects one or more numbers");
        }
        int sum = 0;
        int product = 1;
        for(int i = 0; i < args.length; i++){
            if(args[i] == null && !(args[i] instanceof Number)) {
                return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " expects parameter " + (i + 1) + " to be a number");
            }
            int num = ((Number) args[i]).intValue();
            sum += num;
            product *= FactN.factorial(num, 1);
        }
        return FactN.factorial(sum, 1) / product;
    }

    @Override
    public void write(JSONWriter writer, Properties options)
        throws JSONException {

        writer.object();
        writer.key("description"); writer.value("Calculates the multinomial of a series of numbers");
        writer.key("params"); writer.value("one or more numbers");
        writer.key("returns"); writer.value("number");
        writer.endObject();
    }
}
