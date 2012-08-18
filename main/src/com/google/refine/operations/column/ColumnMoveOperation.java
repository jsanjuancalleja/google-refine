/*

Copyright 2010,2012. Google Inc.
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

package com.google.refine.operations.column;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.google.refine.history.Change;
import com.google.refine.history.HistoryEntry;
import com.google.refine.model.AbstractOperation;
import com.google.refine.model.Project;
import com.google.refine.model.changes.ColumnMoveChange;
import com.google.refine.operations.OperationRegistry;

public class ColumnMoveOperation extends AbstractOperation {
    final protected String _columnName;
    final protected int    _index;

    static public AbstractOperation reconstruct(Project project, JSONObject obj) throws Exception {
        return new ColumnMoveOperation(
            obj.getString("columnName"),
            obj.getInt("index")
        );
    }
    
    public ColumnMoveOperation(
        String columnName,
        int index
    ) {
        _columnName = columnName;
        _index = index;
    }
    
    @Override
    public void write(JSONWriter writer, Properties options)
            throws JSONException {
       
        writer.object();
        writer.key("op"); writer.value(OperationRegistry.s_opClassToName.get(this.getClass()));
        writer.key("description"); writer.value("Move column " + _columnName + " to position " + _index);
        writer.key("columnName"); writer.value(_columnName);
        writer.key("index"); writer.value(_index);
        writer.endObject();
    }


    @Override
    protected String getBriefDescription(Project project) {
        return "Move column " + _columnName + " to position " + _index;
    }

    @Override
    protected HistoryEntry createHistoryEntry(Project project, long historyEntryID) throws Exception {
        if (project.columnModel.getColumnByName(_columnName) == null) {
            throw new Exception("No column named " + _columnName);
        }
        if (_index < 0 || _index >= project.columnModel.columns.size()) {
            throw new Exception("New column index out of range " + _index);
        }
        
        Change change = new ColumnMoveChange(_columnName, _index);
        
        return new HistoryEntry(historyEntryID, project, getBriefDescription(null), ColumnMoveOperation.this, change);
    }
}
