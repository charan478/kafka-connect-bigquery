package com.wepay.kafka.connect.bigquery.utils;

/*
 * Copyright 2016 WePay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import com.google.cloud.bigquery.TableId;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class PartitionedTableIdTest {

  @Test
  public void testBasicBuilder() {
    final String dataset = "dataset";
    final String table = "table";

    final PartitionedTableId tableId = new PartitionedTableId.Builder(dataset, table).build();

    Assert.assertEquals(dataset, tableId.getDataset());
    Assert.assertEquals(table, tableId.getBaseTableName());
    Assert.assertEquals(table, tableId.getFullTableName());

    TableId expectedTableId = TableId.of(dataset, table);
    Assert.assertEquals(expectedTableId, tableId.getBaseTableId());
    Assert.assertEquals(expectedTableId, tableId.getFullTableId());
  }

  @Test
  public void testTableIdBuilder() {
    final String project = "project";
    final String dataset = "dataset";
    final String table = "table";
    final TableId tableId = TableId.of(project, dataset, table);

    final PartitionedTableId partitionedTableId = new PartitionedTableId.Builder(tableId).build();

    Assert.assertEquals(project, partitionedTableId.getProject());
    Assert.assertEquals(dataset, partitionedTableId.getDataset());
    Assert.assertEquals(table, partitionedTableId.getBaseTableName());
    Assert.assertEquals(table, partitionedTableId.getFullTableName());

    Assert.assertEquals(tableId, partitionedTableId.getBaseTableId());
    Assert.assertEquals(tableId, partitionedTableId.getFullTableId());
  }

  @Test
  public void testWithPartition() {
    final String dataset = "dataset";
    final String table = "table";
    final LocalDate partitionDate = LocalDate.of(2016, 10, 21);

    final PartitionedTableId partitionedTableId =
        new PartitionedTableId.Builder(dataset, table).setDayPartition(partitionDate).build();

    final String expectedPartition = "20161021";

    Assert.assertEquals(dataset, partitionedTableId.getDataset());
    Assert.assertEquals(table, partitionedTableId.getBaseTableName());
    Assert.assertEquals(table + "$" + expectedPartition, partitionedTableId.getFullTableName());

    final TableId expectedBaseTableId = TableId.of(dataset, table);
    final TableId expectedFullTableId = TableId.of(dataset, table + "$" + expectedPartition);

    Assert.assertEquals(expectedBaseTableId, partitionedTableId.getBaseTableId());
    Assert.assertEquals(expectedFullTableId, partitionedTableId.getFullTableId());
  }
}