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

package com.github.szczurmys.ignite.testsuites.cassandra.store;

import com.github.szczurmys.ignite.tests.CassandraConfigTest;
import com.github.szczurmys.ignite.tests.CassandraDirectPersistenceTest;
import com.github.szczurmys.ignite.tests.CassandraSessionImplTest;
import com.github.szczurmys.ignite.tests.DDLGeneratorTest;
import com.github.szczurmys.ignite.tests.DatasourceSerializationTest;
import com.github.szczurmys.ignite.tests.IgnitePersistentStoreTest;
import com.github.szczurmys.ignite.tests.cache.store.cassandra.handler.TypeHandlerHelperTest;
import com.github.szczurmys.ignite.tests.utils.CassandraHelper;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Cache suite for Cassandra store.
 *
 * Running with -DforkMode=always is recommended
 */
@RunWith(Suite.class)
@SuiteClasses({
    CassandraConfigTest.class,
    CassandraDirectPersistenceTest.class,
    CassandraSessionImplTest.class,
    DatasourceSerializationTest.class,
    DDLGeneratorTest.class,
    IgnitePersistentStoreTest.class,
    TypeHandlerHelperTest.class})
public class IgniteCassandraStoreTestSuite {
    /** */
    private static final Logger LOGGER = Logger.getLogger(IgniteCassandraStoreTestSuite.class.getName());

    /** */
    @BeforeClass
    public static void setUpClass() {
        if (CassandraHelper.useEmbeddedCassandra()) {
            try {
                CassandraHelper.startEmbeddedCassandra(LOGGER);
            }
            catch (Throwable e) {
                throw new RuntimeException("Failed to start embedded Cassandra instance", e);
            }
        }
    }

    /** */
    @AfterClass
    public static void tearDownClass() {
        if (CassandraHelper.useEmbeddedCassandra()) {
            try {
                CassandraHelper.stopEmbeddedCassandra();
            }
            catch (Throwable e) {
                LOGGER.error("Failed to stop embedded Cassandra instance", e);
            }
        }
    }
}
