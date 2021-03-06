/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.szczurmys.ignite.cache.store.cassandra.handler;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;

/**
 * Type handler for convert java {@link Boolean} &lt;-&gt; cassandra 'boolean'
 */
public class PrimitiveBooleanTypeHandler implements TypeHandler<Boolean, Boolean> {
    private static final long serialVersionUID = 1646718016061072076L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean toJavaType(Row row, int index) {
        if(row.isNull(index)) {
            throw new IllegalArgumentException("Can't cast null value from Cassandra table column index '" + index +
                    "' to " + "boolean value used in domain object model");
        }
        return row.getBool(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean toJavaType(Row row, String col) {
        if(row.isNull(col)) {
            throw new IllegalArgumentException("Can't cast null value from Cassandra table column '" + col +
                    "' to " + "boolean value used in domain object model");
        }
        return row.getBool(col);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean toCassandraPrimitiveType(Boolean javaValue) {
        return javaValue;
    }

    /**
     * Get 'boolean' cassandra type.
     * @return 'boolean' cassandra type
     */
    @Override
    public String getDDLType() {
        return DataType.Name.BOOLEAN.toString();
    }
}
