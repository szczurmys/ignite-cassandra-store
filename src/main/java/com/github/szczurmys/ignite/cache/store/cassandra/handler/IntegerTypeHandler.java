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
 * Type handler for convert java {@link Integer} &lt;-&gt; cassandra 'int'
 */
public class IntegerTypeHandler implements TypeHandler<Integer, Integer> {
    private static final long serialVersionUID = -2254452892101448184L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer toJavaType(Row row, int index) {
        if(row.isNull(index)) {
            return null;
        }
        return row.getInt(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer toJavaType(Row row, String col) {
        if(row.isNull(col)) {
            return null;
        }
        return row.getInt(col);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer toCassandraPrimitiveType(Integer javaValue) {
        return javaValue;
    }

    /**
     * Get 'int' cassandra type.
     * @return 'int' cassandra type
     */
    @Override
    public String getDDLType() {
        return DataType.Name.INT.toString();
    }
}
