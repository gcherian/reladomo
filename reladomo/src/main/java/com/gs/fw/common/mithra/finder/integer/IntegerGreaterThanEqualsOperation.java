/*
 Copyright 2016 Goldman Sachs.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package com.gs.fw.common.mithra.finder.integer;

import com.gs.fw.common.mithra.attribute.Attribute;
import com.gs.fw.common.mithra.attribute.IntegerAttribute;
import com.gs.fw.common.mithra.finder.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class IntegerGreaterThanEqualsOperation extends GreaterThanEqualsOperation
{

    private int parameter;

    public IntegerGreaterThanEqualsOperation(Attribute attribute, int parameter)
    {
        super(attribute);
        this.parameter = parameter;
    }

    public int getParameter()
    {
        return parameter;
    }

    protected Boolean matchesWithoutDeleteCheck(Object o)
    {
        IntegerAttribute IntegerAttribute = (IntegerAttribute)this.getAttribute();
        if (IntegerAttribute.isAttributeNull(o)) return false;
        return IntegerAttribute.intValueOf(o) >= parameter;
    }

    public void zToString(ToStringContext toStringContext)
    {
        this.getAttribute().zAppendToString(toStringContext);
        toStringContext.append(">=").append(this.parameter);
    }

    public int setSqlParameters(PreparedStatement pstmt, int startIndex, SqlQuery query) throws SQLException
    {
        pstmt.setInt(startIndex, parameter);
        return 1;
    }

    public Operation zCombinedAndWithAtomicEquality(AtomicEqualityOperation op)
    {
        if (op.getAttribute().equals(this.getAttribute()))
        {
            if (!op.zIsNullOperation() && ((IntegerEqOperation) op).getParameter() >= this.parameter)
            {
                return op;
            }
            return new None(this.getAttribute());
        }
        return null;
    }

    public Operation zCombinedAndWithAtomicGreaterThan(GreaterThanOperation op)
    {
        if (op.getAttribute().equals(this.getAttribute()))
        {
            int target = ((IntegerGreaterThanOperation) op).getParameter();
            if (target >= this.parameter) return op;
            return this;
        }
        return null;
    }

    public Operation zCombinedAndWithAtomicGreaterThanEquals(GreaterThanEqualsOperation op)
    {
        if (op.getAttribute().equals(this.getAttribute()))
        {
            int target = ((IntegerGreaterThanEqualsOperation) op).getParameter();
            if (target >= this.parameter) return op;
            return this;
        }
        return null;
    }

    public int hashCode()
    {
        return this.getAttribute().hashCode() ^ this.parameter ^ 0x0F0F0F;
    }

    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj instanceof IntegerGreaterThanEqualsOperation)
        {
            IntegerGreaterThanEqualsOperation other = (IntegerGreaterThanEqualsOperation) obj;
            return this.parameter == other.parameter && this.getAttribute().equals(other.getAttribute());
        }
        return false;
    }


}
